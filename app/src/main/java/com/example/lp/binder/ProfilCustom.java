package com.example.lp.binder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfilCustom extends AppCompatActivity {

    int requestCodeSelectPicture = 1;
    @BindView(R.id.iv_back_account) ImageView back;
    @BindView(R.id.isWoman) CheckBox isWoman;
    @BindView(R.id.isMan) CheckBox isMan;
    @BindView(R.id.searchWoman) CheckBox searchWoman;
    @BindView(R.id.searchMan) CheckBox searchMan;
    @BindView(R.id.warningSexe) ImageView warningSexe;
    @BindView(R.id.warningSearch) ImageView warningSearch;
    private ImageView warningNom;
    private ImageView warningPrenom;
    @BindView(R.id.locationUser) TextView locationUser;
    @BindView(R.id.choosePicture) ImageView imageView;


    @BindView(R.id.submit_account) Button submit;
    @BindView(R.id.editNickname) EditText editNom;
    @BindView(R.id.editAge) EditText editOld;
    @BindView(R.id.editBiography) EditText editBiography;
    private String genre;
    private String recherche;
    private DatabaseReference databaseFirebase;
    private String userUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_custom);
        ButterKnife.bind(this);

        databaseFirebase = FirebaseDatabase.getInstance().getReference();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseFirebase.child("users").child(userUid).getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                editNom.setText(dataSnapshot.child(User.NICKNAME).getValue(String.class));
                String gender = dataSnapshot.child(User.GENDER).getValue(String.class);
                if(gender.equals("gender1")) {
                    isMan.setChecked(true);
                    genre = isMan.getText().toString();
                }
                else{
                    isWoman.setChecked(true);
                    genre = isWoman.getText().toString();
                }

                String searchGender = dataSnapshot.child(User.GENDER_PREF).child(User.GENDER_PREF1).getValue(String.class);
                checkGenderSearch(searchGender);
                if(dataSnapshot.child(User.GENDER_PREF).child(User.GENDER_PREF2).exists()){
                    searchGender = dataSnapshot.child(User.GENDER_PREF).child(User.GENDER_PREF2).getValue(String.class);
                    checkGenderSearch(searchGender);
                }
                editOld.setText(dataSnapshot.child(User.AGE).getValue(Integer.class).toString());
                editBiography.setText(dataSnapshot.child(User.DESCRIPTION).getValue(String.class));
                String url = dataSnapshot.child(User.URL_PICTURE).getValue(String.class);
                Picasso.with(getApplicationContext()).load(url).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Geocoder gcd=new Geocoder(getBaseContext(), Locale.getDefault());

        List<Address> addresses;
        double latitude = 46.214988299999995;
        double longitude = 5.2418403;

        try {

            addresses=gcd.getFromLocation(latitude,longitude,1);

            System.out.println("addresses = " + addresses);
            locationUser.setText(addresses.get(0).getLocality());

        } catch (IOException e) {
            e.printStackTrace();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isMan.isChecked()==false && isWoman.isChecked()==false){
                    //Toast.makeText(ProfilCustom.this, "Veuillez indiquer votre sexe.", Toast.LENGTH_SHORT).show();

                    warningSexe.setVisibility(View.VISIBLE);
                }else{
                    warningSexe.setVisibility(View.GONE);
                }

                if(searchMan.isChecked()==false && isWoman.isChecked()==false){
                    //Toast.makeText(ProfilCustom.this, "Veuillez indiquer ce que vous rechercher.", Toast.LENGTH_SHORT).show();
                    warningSearch.setVisibility(View.VISIBLE);
                }else{
                    warningSearch.setVisibility(View.GONE);
                }

                if(editOld.getText().toString().isEmpty()){
                    editOld.setError("Veuillez indiquez votre Age");
                    //warningNom.setVisibility(View.VISIBLE);
                    //Toast.makeText(ProfilCustom.this, "Veuillez indiquer votre nom", Toast.LENGTH_SHORT).show();
                }
                if(Integer.valueOf(editOld.getText().toString()) <18){
                    editOld.setError("Il faut avoir minimum 18 ans pour utiliser cette application");
                }


                if(editNom.getText().toString().isEmpty()){
                    editNom.setError("Veuillez indiquez votre surnom");
                    //warningNom.setVisibility(View.VISIBLE);
                    //Toast.makeText(ProfilCustom.this, "Veuillez indiquer votre nom", Toast.LENGTH_SHORT).show();
                }

                if(searchMan.isChecked() && searchWoman.isChecked()){
                    recherche = searchMan.getText().toString() +" et "+ searchWoman.getText().toString();
                }else {

                    if (searchMan.isChecked()) {
                        recherche = searchMan.getText().toString();
                    }

                    if (searchWoman.isChecked()) {
                        recherche = searchWoman.getText().toString();
                    }

                }

                if((isMan.isChecked() || isWoman.isChecked()) && (searchWoman.isChecked() || searchMan.isChecked())) {
                    try{
                        DatabaseReference refUser = databaseFirebase.child("users").child(userUid).getRef();
                        refUser.child(User.NICKNAME).getRef().setValue(editNom.getText().toString());
                        refUser.child(User.DESCRIPTION).getRef().setValue(editBiography.getText().toString());
                        if(isMan.isChecked())
                            refUser.child(User.GENDER).getRef().setValue("gender1");
                        else
                            refUser.child(User.GENDER).getRef().setValue("gender2");

                        if(searchMan.isChecked() && searchWoman.isChecked()){
                            refUser.child(User.GENDER_PREF).child(User.GENDER_PREF1).getRef().setValue("gender1");
                            refUser.child(User.GENDER_PREF).child(User.GENDER_PREF2).getRef().setValue("gender2");
                        }
                        else if(searchMan.isChecked()){
                            refUser.child(User.GENDER_PREF).child(User.GENDER_PREF1).getRef().setValue("gender1");
                            refUser.child(User.GENDER_PREF).child(User.GENDER_PREF2).getRef().removeValue();
                        }
                        else if(searchWoman.isChecked()){
                            refUser.child(User.GENDER_PREF).child(User.GENDER_PREF1).getRef().setValue("gender2");
                            refUser.child(User.GENDER_PREF).child(User.GENDER_PREF2).getRef().removeValue();
                        }else{
                            refUser.child(User.GENDER_PREF).child(User.GENDER_PREF1).getRef().removeValue();
                            refUser.child(User.GENDER_PREF).child(User.GENDER_PREF2).getRef().removeValue();
                        }
                        refUser.child(User.AGE).getRef().setValue(Integer.valueOf(editOld.getText().toString()));
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Erreur",Toast.LENGTH_SHORT).show();
                        String erreur = e.getMessage();
                    }

            }
            }

        });

        isWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWoman.setChecked(true);
                isMan.setChecked(false);
                warningSexe.setVisibility(View.GONE);
                genre = isWoman.getText().toString();
            }
        });

        isMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMan.setChecked(true);
                isWoman.setChecked(false);
                warningSearch.setVisibility(View.GONE);
                genre = isMan.getText().toString();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilCustom.super.onBackPressed();
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.choosePicture);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCodeSelectPicture);

            }
        });
    }










    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == requestCodeSelectPicture) {

            if (resultCode == RESULT_OK) {
                if (intent != null) {
                    // Get the URI of the selected file
                    final Uri uri = intent.getData();
                    try {
                        useImage(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            super.onActivityResult(requestCode, resultCode, intent);

        }
    }

    void useImage(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        //use the bitmap as you like
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120, 120, false));
    }

    private void checkGenderSearch(String gender){
        if(gender.equals("gender1"))
            searchMan.setChecked(true);
        if(gender.equals("gender2"))
            searchWoman.setChecked(true);
    }
}
