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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProfilCustom extends AppCompatActivity {

    int requestCodeSelectPicture = 1;
    private ImageView back;
    private CheckBox isWoman;
    private CheckBox isMan;
    private CheckBox searchWoman;
    private CheckBox searchMan;
    private ImageView warningSexe;
    private ImageView warningSearch;
    private ImageView warningNom;
    private ImageView warningPrenom;
    private TextView locationUser;


    private Button submit;
    private EditText editNom;
    private EditText editPrenom;
    private EditText editOld;
    private String genre;
    private String recherche;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_custom);


        editOld = (EditText) findViewById(R.id.editAge);
        isWoman = (CheckBox) findViewById(R.id.isWoman);
        isMan = (CheckBox) findViewById(R.id.isMan);
        searchWoman = (CheckBox) findViewById(R.id.searchWoman);
        searchMan = (CheckBox) findViewById(R.id.searchMan);
        warningSearch = (ImageView) findViewById(R.id.warningSearch);
        warningSexe = (ImageView) findViewById(R.id.warningSexe);


        editNom = (EditText) findViewById(R.id.editNom);
        editPrenom = (EditText) findViewById(R.id.editPrenom);
        locationUser = (TextView) findViewById(R.id.locationUser);



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


        submit = (Button) findViewById(R.id.submit_account);

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


                if(editNom.getText().toString().isEmpty()){
                    editNom.setError("Veuillez indiquez votre nom");
                    //warningNom.setVisibility(View.VISIBLE);
                    //Toast.makeText(ProfilCustom.this, "Veuillez indiquer votre nom", Toast.LENGTH_SHORT).show();
                }

                if(editPrenom.getText().toString().isEmpty()){
                    editPrenom.setError("Veuillez indiquez votre Prénom");
                    //warningPrenom.setVisibility(View.VISIBLE);
                    //Toast.makeText(ProfilCustom.this, "Veuillez indiquer votre prénom ", Toast.LENGTH_SHORT).show();
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

                if((isMan.isChecked() || isWoman.isChecked()) && (!editPrenom.getText().toString().isEmpty()&& !editPrenom.getText().toString().isEmpty()) && (searchWoman.isChecked() || searchMan.isChecked())) {


                Toast.makeText(ProfilCustom.this,
                        "Nom : " + editNom.getText().toString() + ","+
                        "Prenom : " + editPrenom.getText().toString() + ","+
                                "Genre :" + genre + ","+
                                "Recherche :" + recherche
                        , Toast.LENGTH_SHORT).show();



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



        back = (ImageView) findViewById(R.id.iv_back_account);

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
        ImageView imageView = (ImageView) findViewById(R.id.choosePicture);
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        //use the bitmap as you like
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120, 120, false));
    }
}
