package com.gandaedukasi.gandaedukasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class EditProfileActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    Session session;
    EditText studentName, school, address, parent, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(EditProfileActivity.this);

        setContentView(R.layout.activity_edit_profile);

        studentName = (EditText) findViewById(R.id.studentName);
        school = (EditText) findViewById(R.id.studentSchool);
        address = (EditText) findViewById(R.id.studentAddress);
        parent = (EditText) findViewById(R.id.studentParent);
        phone = (EditText) findViewById(R.id.studentPhone);
        email = (EditText) findViewById(R.id.studentEmail);

        pDialog = new ProgressDialog(EditProfileActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        Button btnSave;
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(i);
                */
            }
        });
    }
    public void onResume (){
        super.onResume();
        if(isNetworkAvailable()){
            pDialog.show();
            String url = new RequestServer().getServer_url()+"getProfile/siswa";
            Log.d("Profile Url",">"+url);

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());

            Ion.with(EditProfileActivity.this)
                    .load(url)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            String status = result.get("status").toString();
                            if (status.equals("1")){
                                JsonObject data = result.getAsJsonObject("data");
                                Log.d("Response",">"+data);
                                studentName.setText(data.get("fullname").getAsString());
                                school.setText(data.get("siswa_pendidikan").getAsString());
                                address.setText(data.get("alamat").getAsString());
                                parent.setText(data.get("siswa_wali").getAsString());
                                phone.setText(data.get("siswa_cp").getAsString());
                                email.setText(data.get("email").getAsString());
                            }else {
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });
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
}
