package com.gandaedukasi.gandaedukasi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MenuActivity extends AppCompatActivity {

    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(MenuActivity.this);
        if(!session.isLoggedIn()){
            Intent i = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        Log.d("FULLNAME",session.getUserAlamat());
        setContentView(R.layout.activity_menu);

        ImageView btnProgramEdukasi, btnPembayaran, btnCariPengajar, btnJadwalLes, btnProfile, btnPertemuan;

        btnProgramEdukasi = (ImageView)findViewById(R.id.btnProgramEdukasi);
        btnPembayaran = (ImageView)findViewById(R.id.btnPembayaran);
        btnCariPengajar = (ImageView)findViewById(R.id.btnCariPengajar);
        btnJadwalLes = (ImageView)findViewById(R.id.btnJadwalLes);
        btnProfile = (ImageView)findViewById(R.id.btnProfile);
        btnPertemuan = (ImageView)findViewById(R.id.btnLihatPertemuan);

        btnProgramEdukasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ProgramEdukasiActivity.class);
                startActivity(i);
            }
        });
        btnPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, PaymentActivity.class);
                startActivity(i);
            }
        });
        btnCariPengajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ListMapelJadwalActivity.class);
                startActivity(i);
            }
        });
        btnJadwalLes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, JadwalActivity.class);
                startActivity(i);
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });
        btnPertemuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, PertemuanActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        cekRating();
    }

    private void cekRating(){
        if(isNetworkAvailable()){
            String url = new RequestServer().getServer_url() + "cekRating";

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());

            Ion.with(MenuActivity.this)
                    .load(url)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                String status = result.get("status").getAsString();
                                if(status.equals("1")){
                                    JsonObject data = result.get("data").getAsJsonObject();
                                    Intent i = new Intent(MenuActivity.this, RatingActivity.class);
                                    i.putExtra("cek_id",data.get("cek_id").getAsString());
                                    i.putExtra("siswa_id",data.get("siswa_id").getAsString());
                                    i.putExtra("pengajar_id",data.get("pengajar_id").getAsString());
                                    i.putExtra("jadwal_id",data.get("jadwal_id").getAsString());
                                    i.putExtra("nama_pengajar",data.get("nama_pengajar").getAsString());
                                    i.putExtra("photo",data.get("photo").getAsString());
                                    i.putExtra("telp",data.get("telp").getAsString());
                                    i.putExtra("alamat",data.get("alamat").getAsString());
                                    i.putExtra("label_mapel",data.get("label_mapel").getAsString());
                                    startActivity(i);
                                    //Toast.makeText(MenuActivity.this, "Rating tersedia", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception ex){
                                Log.e("Roooooor",ex.toString());
                                //Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent i = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
