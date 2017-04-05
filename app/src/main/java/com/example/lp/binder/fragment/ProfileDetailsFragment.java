package com.example.lp.binder.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lp.binder.R;
import com.example.lp.binder.adapter.PicturesGridAdapter;

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
    private ImageView matchIcon;
    private ImageView chatIcon;
    private ImageView rdvIcon;
    private RecyclerView picturesGridView;

    public static ProfileDetailsFragment newInstance() {
        ProfileDetailsFragment fragment = new ProfileDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mainPicture = (CircleImageView) view.findViewById(R.id.fragment_profile_details_main_picture);
        nameTextView = (TextView) view.findViewById(R.id.fragment_profile_details_name);
        ageTextView = (TextView) view.findViewById(R.id.fragment_profile_details_age);
        lookingForTextView = (TextView) view.findViewById(R.id.fragment_profile_details_looking_for);
        descriptionTextView = (TextView) view.findViewById(R.id.fragment_profile_details_description_text);
        matchIcon = (ImageView) view.findViewById(R.id.fragment_profile_details_match_icon);
        chatIcon = (ImageView) view.findViewById(R.id.fragment_profile_details_chat_icon);
        rdvIcon = (ImageView) view.findViewById(R.id.fragment_profile_details_rdv_icon);
        picturesGridView = (RecyclerView) view.findViewById(R.id.fragment_profile_details_pictures_grid);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        picturesGridView.setLayoutManager(layoutManager);
        PicturesGridAdapter adapter = new PicturesGridAdapter();
        picturesGridView.setAdapter(adapter);

        return view;
    }
}
