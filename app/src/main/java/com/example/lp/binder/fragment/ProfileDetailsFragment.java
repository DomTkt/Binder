package com.example.lp.binder.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.data.User;
import com.example.lp.binder.ProfilCustom;
import com.example.lp.binder.R;
import com.example.lp.binder.adapter.PicturesGridAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by romai on 05/04/2017.
 */

public class ProfileDetailsFragment extends Fragment {

    private CircleImageView mainPicture;
    private TextView nameTextView;
    private TextView ageTextView;
    private TextView lookingForTextView;
    private TextView descriptionTextView;
    private ImageView editButton;
    private ImageView matchIcon;
    private ImageView chatIcon;
    private ImageView rdvIcon;
    private RecyclerView picturesGridView;
    private ImageView imageView;

    public static ProfileDetailsFragment newInstance() {
        ProfileDetailsFragment fragment = new ProfileDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_details, container, false);
        mainPicture = (CircleImageView) view.findViewById(R.id.fragment_profile_details_main_picture);
        nameTextView = (TextView) view.findViewById(R.id.fragment_profile_details_name);
        ageTextView = (TextView) view.findViewById(R.id.fragment_profile_details_age);
        lookingForTextView = (TextView) view.findViewById(R.id.fragment_profile_details_looking_for);
        descriptionTextView = (TextView) view.findViewById(R.id.fragment_profile_details_description_text);
        editButton = (ImageView) view.findViewById(R.id.fragment_profile_details_edit_icon);
        matchIcon = (ImageView) view.findViewById(R.id.fragment_profile_details_match_icon);
        chatIcon = (ImageView) view.findViewById(R.id.fragment_profile_details_chat_icon);
        rdvIcon = (ImageView) view.findViewById(R.id.fragment_profile_details_rdv_icon);
        picturesGridView = (RecyclerView) view.findViewById(R.id.fragment_profile_details_pictures_grid);
        imageView = (ImageView) view.findViewById(R.id.profilPicture);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        picturesGridView.setLayoutManager(layoutManager);
        PicturesGridAdapter adapter = new PicturesGridAdapter();
        picturesGridView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getRef();
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(User.NICKNAME).exists()){
                    nameTextView.setText(dataSnapshot.child(User.NICKNAME).getValue(String.class));
                }
                if(dataSnapshot.child(User.AGE).exists()){
                    ageTextView.setText(Long.toString(dataSnapshot.child(User.AGE).getValue(Long.class))   + "y.o");
                }
                String genderString = "";
                if(dataSnapshot.child(User.GENDER).exists()){
                    String gender = dataSnapshot.child(User.GENDER).getValue(String.class);
                    genderString = getGender(gender);
                }
                if(dataSnapshot.child(User.GENDER_PREF).child(User.GENDER_PREF1).exists()){
                    String gender = dataSnapshot.child(User.GENDER_PREF).child(User.GENDER_PREF1).getValue(String.class);
                    genderString += " looking for a " + getGender(gender);
                }
                if(dataSnapshot.child(User.GENDER_PREF).child(User.GENDER_PREF2).exists()){
                    String gender = dataSnapshot.child(User.GENDER_PREF).child(User.GENDER_PREF2).getValue(String.class);
                    genderString += " and a " + getGender(gender);
                }
                lookingForTextView.setText(genderString);
                if(dataSnapshot.child(User.DESCRIPTION).exists()){
                    descriptionTextView.setText(dataSnapshot.child(User.DESCRIPTION).getValue(String.class));
                }
                if(dataSnapshot.child(User.URL_PICTURE).exists()){
                    String url = dataSnapshot.child(User.URL_PICTURE).getValue(String.class);
                    Picasso.with(getActivity().getApplicationContext()).load(url).resize(100,100).into(imageView);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProfilCustom.class);
                startActivity(i);
            }
        });


    }

    private String getGender(String gender){
        if(gender.equals("gender1"))
            return "man";
        else
            return  "woman";
    }
}
