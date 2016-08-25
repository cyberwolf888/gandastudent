package com.gandaedukasi.gandaedukasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.onesignal.OneSignal;

public class LoginActivity extends AppCompatActivity {
    Button btnLoggin, btnReg;
    EditText textEmail, textPassword;
    ProgressDialog pDialog;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(LoginActivity.this);
        if (session.isLoggedIn()){
            Intent i = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(i);
            finish();
        }
        setContentView(R.layout.activity_login);

        btnLoggin = (Button)findViewById(R.id.buttonLogin);
        btnReg = (Button)findViewById(R.id.buttonReg);
        textEmail = (EditText)findViewById(R.id.editEmail);
        textPassword = (EditText)findViewById(R.id.editPassword);

        btnLoggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
    private void attemptLogin() {
        textEmail.setError(null);
        textPassword.setError(null);

        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            textPassword.setError(getString(R.string.id_error_password_empty));
            focusView = textPassword;
            cancel = true;
        }else if (!isPasswordValid(password)){
            textPassword.setError(getString(R.string.id_error_passowrd_length));
            focusView = textPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            textEmail.setError(getString(R.string.id_error_email_empty));
            focusView = textEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            textEmail.setError(getString(R.string.id_error_email_invalid));
            focusView = textEmail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if(isNetworkAvailable()){
                pDialog = new ProgressDialog(LoginActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

                String url = new RequestServer().getServer_url()+"login";
                Log.d("Login Url",">"+url);

                JsonObject jsonReq = new JsonObject();
                jsonReq.addProperty("email", email);
                jsonReq.addProperty("password", password);
                jsonReq.addProperty("type", "SW");

                Ion.with(LoginActivity.this)
                        .load(url)
                        //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                        .setJsonObjectBody(jsonReq)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                try{
                                    String status = result.get("status").toString();
                                    if (status.equals("1")){
                                        JsonObject data = result.getAsJsonObject("data");
                                        Log.d("Response",">"+data);
                                        String photo = "";
                                        if(!data.get("photo").isJsonNull()){
                                            photo = data.get("photo").getAsString();
                                        }
                                        session.createLoginSession(data.get("user_id").getAsString(),data.get("fullname").getAsString(),photo,data.get("alamat").getAsString());

                                        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                                            @Override
                                            public void idsAvailable(String userId, String registrationId) {
                                                if (registrationId != null){
                                                    Log.d("Onesignal debug", "registrationId:" + registrationId);

                                                    String url = new RequestServer().getServer_url()+"createNotif";
                                                    Ion.with(LoginActivity.this)
                                                            .load(url)
                                                            .setMultipartParameter("user_id", session.getUserId())
                                                            .setMultipartParameter("onesignal_id", userId)
                                                            .asJsonObject()
                                                            .setCallback(new FutureCallback<JsonObject>() {
                                                                @Override
                                                                public void onCompleted(Exception e, JsonObject result) {

                                                                }
                                                            });
                                                }
                                            }
                                        });

                                        Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                                        startActivity(i);
                                        finish();
                                        //Toast.makeText(getApplicationContext(), data.get("fullname").toString(), Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), getString(R.string.id_error_login), Toast.LENGTH_LONG).show();
                                    }
                                }catch (Exception ex){
                                    Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                }

                                pDialog.dismiss();
                            }});

            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
            }
            /*
            Intent i = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(i);
            */
        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
