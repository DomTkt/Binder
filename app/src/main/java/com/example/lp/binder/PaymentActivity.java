package com.example.lp.binder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.stripe.android.model.Card;

import java.util.regex.Pattern;

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
                }else{
                    Toast.makeText(ctx,"Vous n'avez pas rempli tout les champs correctement", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isConform(){
        boolean isConform = true;

        Pattern expDatePattern = Pattern.compile("^[0-9][0-9]/[0-9][0-9]$");
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
            etExp.setError("Ce champ doit etre du type : \"01/12\" avec 01 pour le mois et 12 pour l'année");
        }

        return isConform;
    }
}
