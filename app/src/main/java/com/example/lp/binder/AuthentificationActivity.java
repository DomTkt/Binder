package com.example.lp.binder;

import android.content.Intent;
import android.graphics.Region;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthentificationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    @BindView(R.id.editTextEmail) EditText editTextEmail;
    @BindView(R.id.editTextPassword) EditText editTextPassword;
    @BindView(R.id.btSignIn) Button btSignIn;
    @BindView(R.id.btSignUp) Button btSignUp;
    @BindView(R.id.signInButtonGoogle) SignInButton btSignInGoogle;

    //Error message
    @BindString(R.string.error_email_non_compliant) String errorLogin;
    @BindString(R.string.error_password_empty) String errorPasswordEmpty;
    @BindString(R.string.error_password_lenght) String errorPasswordLenght;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient googleApiClient;
    private FirebaseUser user;
    private static final int GOOGLE_INTENT_RESULT = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        ButterKnife.bind(this);

        //FirebaseAuth.getInstance().signOut();

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(AuthentificationActivity.this,MainActivity.class);
                    AuthentificationActivity.this.startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Sign out",Toast.LENGTH_LONG).show();
                }
            }
        };


        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


    }


    //region Click listener
    @OnClick(R.id.btSignUp)
    protected void emailSignUp(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if(inputIsCompliant(email,password)){
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Error",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @OnClick(R.id.btSignIn)
    protected void emailSignIn(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if(inputIsCompliant(email,password)){
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Vous n'avez pas de compte cliquer sur SIGN UP",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @OnClick(R.id.signInButtonGoogle)
    protected void googleSignIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, GOOGLE_INTENT_RESULT);
    }
    //endregion

    protected boolean inputIsCompliant(String email, String password){
        if(email == null || email.isEmpty()){
            editTextEmail.setError(errorLogin);
            return false;
        }
        String regexEmail = "^[a-z0-9_\\-\\.]{2,}@[a-z0-9_\\-\\.]{2,}\\.[a-z]{2,}$";
        Pattern patternEmail = Pattern.compile(regexEmail);
        Matcher matcherEmail = patternEmail.matcher(email);
        if(!matcherEmail.find()){
            editTextEmail.setError(errorLogin);
            return false;
        }
        if(password == null || password.isEmpty()){
            editTextPassword.setError(errorPasswordEmpty);
            return false;
        }
        if(password.length() < 6){
            editTextPassword.setError(errorPasswordLenght);
            return false;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_INTENT_RESULT) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    Toast.makeText(AuthentificationActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } else {
                Toast.makeText(getApplicationContext(),"Nous avons pas pu vous connecter avec votre compte google",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
