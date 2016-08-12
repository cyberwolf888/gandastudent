package com.gandaedukasi.gandaedukasi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.gandaedukasi.gandaedukasi.utility.Session;

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
        Log.d("FULLNAME",session.getFullname());
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
                Intent i = new Intent(MenuActivity.this, ProgramEdukasiActivity.class);
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
                Intent i = new Intent(MenuActivity.this, ProgramEdukasiActivity.class);
                startActivity(i);
            }
        });
    }
}
