package com.example.lp.binder;

import android.content.Context;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.regex.Pattern;

import static android.R.attr.data;
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
    private String tok;

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
        Stripe stripe = new Stripe(ctx, "pk_test_duyer4UlMe33noGxo2jYayuY");
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {

                        tok = token.getId();
                        System.out.println("tok = " + tok);

                        AsyncPay asyncPay = new AsyncPay();
                        asyncPay.execute(this);
                        // Send POST data request



                        // Send token to your server
                    }
                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(ctx,error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

private void connectForPay(){

    URL url = null;
    try {
        url = new URL("http://lucien.appsolute-preprod.fr/stripe/charge_user.php");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("stripeToken",tok);
        jsonBody.put("stripeAmount","1000");
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", "application/json");
        connection.setDoOutput(true);


                            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                            dStream.writeBytes(URLEncoder.encode(jsonBody.toString(),"UTF-8"));
                            dStream.flush();
                            dStream.close();
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (ProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
        e.printStackTrace();
    }


}

    public class AsyncPay extends AsyncTask<Object, Integer, String[]> {

        @Override
        protected String[] doInBackground(Object... params) {
                connectForPay();
            return new String[0];
        }
    }

}
