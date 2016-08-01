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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ProfileActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    TextView studentName, school, address, parent, phone, email;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(ProfileActivity.this);

        setContentView(R.layout.activity_profile);
        studentName = (TextView) findViewById(R.id.studentName);
        school = (TextView) findViewById(R.id.studentSchool);
        address = (TextView) findViewById(R.id.studentAddress);
        parent = (TextView) findViewById(R.id.studentParent);
        phone = (TextView) findViewById(R.id.studentPhone);
        email = (TextView) findViewById(R.id.studentEmail);

        pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        ImageView btnEdit;
        btnEdit = (ImageView)findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(i);
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

            Ion.with(ProfileActivity.this)
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
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
            Intent i = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(i);
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
