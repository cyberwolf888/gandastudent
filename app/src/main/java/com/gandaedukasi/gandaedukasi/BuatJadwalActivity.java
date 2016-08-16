package com.gandaedukasi.gandaedukasi;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class BuatJadwalActivity extends AppCompatActivity {
    String pengajar_id,mapel_id,paket_id,tarif_id,jumlah_pertemuan,harga;
    LinearLayout li;
    Integer x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pengajar_id = getIntent().getStringExtra("pengajar_id");
        mapel_id = getIntent().getStringExtra("mapel_id");
        paket_id = getIntent().getStringExtra("paket_id");
        tarif_id = getIntent().getStringExtra("tarif_id");
        jumlah_pertemuan = getIntent().getStringExtra("jumlah_pertemuan");
        harga = getIntent().getStringExtra("harga");
        //Log.d("Data",">"+pengajar_id+" "+mapel_id+" "+paket_id+" "+tarif_id+" "+jumlah_pertemuan+" "+harga+" ");

        setContentView(R.layout.activity_buat_jadwal);

        li = (LinearLayout) findViewById(R.id.linierLayout);

        x=0;
        for (int i=0; i<Integer.valueOf(jumlah_pertemuan); i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 0, 3);
            TextInputLayout til = new TextInputLayout(BuatJadwalActivity.this);
            til.setLayoutParams(params);
            til.setHint("Tanggal Pertemuan "+String.valueOf(i+1));
            li.addView(til);

            EditText title = new EditText(this);
            title.setLayoutParams(params);
            title.setId(30+i);
            title.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
            title.setFocusable(false);
            til.addView(title);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "TGL Pertemuan  "+String.valueOf(x), Toast.LENGTH_SHORT).show();
                }
            });

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params2.setMargins(0, 0, 0, 10);
            TextInputLayout til2 = new TextInputLayout(BuatJadwalActivity.this);
            til2.setLayoutParams(params2);
            til2.setHint("Waktu Pertemuan "+String.valueOf(i+1));
            li.addView(til2);

            EditText title2 = new EditText(this);
            title2.setLayoutParams(params);
            title2.setId(40+i);
            title2.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
            title2.setFocusable(false);
            til2.addView(title2);
            title2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"Waktu pertemuan "+String.valueOf(x),Toast.LENGTH_SHORT).show();
                }
            });

            x++;
        }


    }
}
