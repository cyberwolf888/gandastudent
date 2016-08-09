package com.gandaedukasi.gandaedukasi;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
    TextView studentName, studentEmail, studentPhone, studentTempatLahir, studentTanggalLahir,
            studentParent, studentAddress, studentTingkatPendidikan, studentZona;

    ImageView studentPhoto;
    String photo;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(ProfileActivity.this);

        setContentView(R.layout.activity_profile);
        studentName = (TextView) findViewById(R.id.studentName);
        studentEmail = (TextView) findViewById(R.id.studentEmail);
        studentPhone = (TextView) findViewById(R.id.studentPhone);
        studentTempatLahir = (TextView) findViewById(R.id.studentTempatLahir);
        studentTanggalLahir = (TextView) findViewById(R.id.studentTanggalLahir);
        studentParent = (TextView) findViewById(R.id.studentParent);
        studentAddress = (TextView) findViewById(R.id.studentAddress);
        studentTingkatPendidikan = (TextView) findViewById(R.id.studentTingkatPendidikan);
        studentZona = (TextView) findViewById(R.id.studentZona);

        studentPhoto = (ImageView) findViewById(R.id.studentPhoto);

        ImageView btnEdit;
        btnEdit = (ImageView)findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
                i.putExtra("studentName",studentName.getText().toString());
                i.putExtra("studentEmail",studentEmail.getText().toString());
                i.putExtra("studentPhone",studentPhone.getText().toString());
                i.putExtra("studentTempatLahir",studentTempatLahir.getText().toString());
                i.putExtra("studentTanggalLahir",studentTanggalLahir.getText().toString());
                i.putExtra("studentParent",studentParent.getText().toString());
                i.putExtra("studentAddress",studentAddress.getText().toString());
                i.putExtra("studentTingkatPendidikan",studentTingkatPendidikan.getText().toString());
                i.putExtra("studentZona",studentZona.getText().toString());
                i.putExtra("studentPhoto", photo);
                startActivity(i);
            }
        });
    }
    public void onResume (){
        super.onResume();
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
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
                            try{
                                String status = result.get("status").toString();
                                if (status.equals("1")){
                                    JsonObject data = result.getAsJsonObject("data");
                                    Log.d("Response",">"+data);
                                    studentName.setText(data.get("fullname").getAsString());
                                    studentEmail.setText(data.get("email").getAsString());
                                    studentPhone.setText(data.get("siswa_cp").getAsString());
                                    studentTempatLahir.setText(data.get("tempat_lahir").getAsString());
                                    studentTanggalLahir.setText(data.get("tgl_lahir").getAsString());
                                    studentParent.setText(data.get("siswa_wali").getAsString());
                                    studentAddress.setText(data.get("alamat").getAsString());
                                    studentTingkatPendidikan.setText(data.get("tingkat_pendidikan").getAsString());
                                    studentZona.setText(data.get("cabang").getAsString());
                                    if(data.get("photo").isJsonNull()){
                                        photo = "";
                                    }else{
                                        photo = data.get("photo").getAsString();
                                        String url_photo = new RequestServer().getPhotoUrl()+"/siswa/"+photo;
                                        Ion.with(studentPhoto).load(url_photo);
                                    }
                                    session.editName(data.get("fullname").getAsString());
                                    session.editPhoto(photo);
                                }else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception ex){
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProfileActivity.this, MenuActivity.class);
        ComponentName cn = i.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        startActivity(mainIntent);
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
