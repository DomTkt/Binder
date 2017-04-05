package com.example.lp.binder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.Calendar;
import java.util.regex.Pattern;

import static java.security.AccessController.getContext;

public class PaymentActivity extends AppCompatActivity {

    private String number;
    private int expMonth;
    private int expYear;
    private String CVC;
    private Card card;
    private Button validate;
    private EditText etNumber;
    private EditText etExp;
    private EditText etCVC;
    private Context ctx;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ctx = this;
        validate = (Button) findViewById(R.id.submit);
        etNumber = (EditText) findViewById(R.id.et_num);
        etExp = (EditText) findViewById(R.id.et_exp);
        etCVC = (EditText) findViewById(R.id.et_cvc);
        back = (ImageView) findViewById(R.id.iv_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentActivity.super.onBackPressed();
            }
        });


        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConform()){
                    number = etNumber.getText().toString();
                    expMonth = Integer.valueOf(etExp.getText().toString().split("/")[0]);
                    expYear = Integer.valueOf(etExp.getText().toString().split("/")[1]);
                    CVC = etCVC.getText().toString();
                    Card card = new Card(number, expMonth, expYear, CVC);
                    if(!card.validateCard()){
                        if(!card.validateNumber())
                            etNumber.setError("Ce numéro n'existe pas");
                        if(!card.validateExpMonth())
                            etExp.setError("Cette date n'est pas valide");
                        if(!card.validateExpYear())
                            etExp.setError("Cette date n'est pas valide");
                        if(!card.validateCVC())
                            etCVC.setError("Le CVC n'est pas valide");
                    }
                    try {
                        createToken(card);
                    } catch (AuthenticationException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(ctx,"Vous n'avez pas rempli tout les champs correctement", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isConform(){
        boolean isConform = true;

        Pattern expDatePattern = Pattern.compile("^[0-9][0-9]/[0-9][0-9][0-9][0-9]$");

        if(etNumber.getText().toString().isEmpty()){
            isConform = false;
            etNumber.setError("Veuillez remplir ce champs");
        }

        if(etExp.getText().toString().isEmpty()){
            isConform = false;
            etExp.setError("Veuillez remplir ce champs");
        }

        if(etCVC.getText().toString().isEmpty()){
            isConform = false;
            etCVC.setError("Veuillez remplir ce champs");
        }

        if(!expDatePattern.matcher(etExp.getText().toString()).matches()){
            isConform = false;
            etExp.setError("Ce champ doit etre du type : \"01/2018\" avec 01 pour le mois et 2018 pour l'année");
        }else {
            if (Integer.valueOf(etExp.getText().toString().split("/")[0]) > 12 || Integer.valueOf(etExp.getText().toString().split("/")[0]) < 1) {
                isConform = false;
                etExp.setError("Le mois doit etre compris entre 01 et 12");
            }
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int validityMaxYear = Calendar.getInstance().get(Calendar.YEAR) + 4;
            int yearWritten = Integer.valueOf(etExp.getText().toString().split("/")[1]);
            if (yearWritten < currentYear || yearWritten > validityMaxYear) {
                isConform = false;
                etExp.setError("L'année doit etre comprise entre " + currentYear + " et " + validityMaxYear);
            }
        }


        return isConform;
    }

    public void createToken(Card card) throws AuthenticationException {
        Stripe stripe = new Stripe(ctx, "PUBLIC KEY");
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        // Send token to your server
                    }
                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(ctx,error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
