package com.gandaedukasi.gandaedukasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class DetailPengajarActivity extends AppCompatActivity {
    String mapel_id, pengajar_id;
    Session session;
    ProgressDialog pDialog;
    TextView teacherName, teacherPhone, teacherEdu, teacherAddress, teacherLecture, teacherLevel, teacherZone;
    ImageView teacherPhoto;
    JsonObject data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pengajar_id = getIntent().getStringExtra("pengajar_id");
        mapel_id = getIntent().getStringExtra("mapel_id");
        session = new Session(DetailPengajarActivity.this);
        setContentView(R.layout.activity_detail_pengajar);

        teacherName = (TextView) findViewById(R.id.teacherName);
        teacherPhone = (TextView) findViewById(R.id.teacherPhone);
        teacherEdu = (TextView) findViewById(R.id.teacherEdu);
        teacherAddress = (TextView) findViewById(R.id.teacherAddress);
        teacherLecture = (TextView) findViewById(R.id.teacherLecture);
        teacherLevel = (TextView) findViewById(R.id.teacherLevel);
        teacherZone = (TextView) findViewById(R.id.teacherZone);
        teacherPhoto = (ImageView) findViewById(R.id.teacherPhoto);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume(){
        super.onResume();
        getProfile();

    }

    private void getProfile(){
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(DetailPengajarActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getApplicationContext(), "Proses dibatalkan!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });

            String url = new RequestServer().getServer_url()+"getPengajar";
            Log.d("Login Url",">"+url);

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("pengajar_id", pengajar_id);
            Log.d("Req Data",">"+jsonReq);

            Ion.with(DetailPengajarActivity.this)
                    .load(url)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            Log.d("Response Data",">"+result);
                            try{
                                String status = result.get("status").toString();
                                Log.d("status",">"+status);
                                if (status.equals("1")){
                                    data = result.getAsJsonObject("data");
                                    teacherName.setText(data.get("fullname").getAsString());
                                    teacherPhone.setText(data.get("pengajar_cp").getAsString());
                                    teacherEdu.setText(data.get("pengajar_pendidikan").getAsString());
                                    teacherAddress.setText(data.get("pengajar_alamat").getAsString());
                                    teacherLecture.setText(data.get("lecture").getAsString());
                                    teacherLevel.setText(data.get("jenjang_mengajar").getAsString());
                                    teacherZone.setText(data.get("label_cabang").getAsString());
                                    if(!data.get("photo").isJsonNull()){
                                        Ion.with(DetailPengajarActivity.this)
                                                .load(new RequestServer().getPhotoUrl()+"/pengajar/"+data.get("photo").getAsString())
                                                .withBitmap()
                                                .placeholder(R.drawable.guest)
                                                .error(R.drawable.guest)
                                                .intoImageView(teacherPhoto);
                                    }
                                }
                            }catch (Exception ex){
                                Log.d("Eroor",ex.toString());
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });

        }else {
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
