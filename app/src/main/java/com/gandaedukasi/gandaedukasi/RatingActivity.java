package com.gandaedukasi.gandaedukasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.w3c.dom.Text;

public class RatingActivity extends AppCompatActivity {
    String cek_id,siswa_id,pengajar_id,jadwal_id,nama_pengajar,photo,telp,alamat,label_mapel;
    TextView teacherName,teacherPhone,teacherAddress,teacherLecture;
    ImageView teacherPhoto;
    RatingBar ratingBar;
    Button buttonSubmit;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cek_id = getIntent().getStringExtra("cek_id");
        siswa_id = getIntent().getStringExtra("siswa_id");
        pengajar_id = getIntent().getStringExtra("pengajar_id");
        jadwal_id = getIntent().getStringExtra("jadwal_id");
        nama_pengajar = getIntent().getStringExtra("nama_pengajar");
        photo = getIntent().getStringExtra("photo");
        telp = getIntent().getStringExtra("telp");
        alamat = getIntent().getStringExtra("alamat");
        label_mapel = getIntent().getStringExtra("label_mapel");

        setContentView(R.layout.activity_rating);

        teacherName = (TextView) findViewById(R.id.teacherName);
        teacherPhone = (TextView) findViewById(R.id.teacherPhone);
        teacherAddress = (TextView) findViewById(R.id.teacherAddress);
        teacherLecture = (TextView) findViewById(R.id.teacherLecture);
        teacherPhoto = (ImageView) findViewById(R.id.teacherPhoto);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        teacherName.setText(nama_pengajar);
        teacherPhone.setText(telp);
        teacherAddress.setText(alamat);
        teacherLecture.setText(label_mapel);

        String url_photo = new RequestServer().getPhotoUrl()+"pengajar/"+photo;
        Ion.with(this)
                .load(url_photo)
                .withBitmap()
                .placeholder(R.drawable.logo)
                .error(R.drawable.guest)
                .intoImageView(teacherPhoto);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crateRating();
            }
        });
    }

    private void crateRating(){
        if (isNetworkAvailable()){
            pDialog = new ProgressDialog(RatingActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getApplicationContext(), "Proses dibatalkan!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            pDialog.show();

            String url = new RequestServer().getServer_url()+"createRating";
            Log.d("Test Url",">"+url);

            float rating = ratingBar.getRating();

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("cek_id", cek_id);
            jsonReq.addProperty("siswa_id", siswa_id);
            jsonReq.addProperty("pengajar_id", pengajar_id);
            jsonReq.addProperty("jadwal_id", jadwal_id);
            jsonReq.addProperty("rating", rating);
            Log.d("jsonReq",">"+jsonReq);

            Ion.with(RatingActivity.this)
                    .load(url)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                String status = result.get("status").getAsString();
                                if (status.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "Rating berhasil diberikan!", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(RatingActivity.this, MenuActivity.class);
                                    finish();
                                }else{
                                    new AlertDialog.Builder(RatingActivity.this)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle("Gagal!")
                                            .setMessage("Gagal memberi rating. Silahkan coba lagi.")
                                            .setNegativeButton("Cancel",null)
                                            .show();
                                    //Toast.makeText(getApplicationContext(), "Jadwal sedang kosong", Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception ex){
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
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
