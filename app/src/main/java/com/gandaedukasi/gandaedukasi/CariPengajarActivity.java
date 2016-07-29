package com.gandaedukasi.gandaedukasi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CariPengajarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_cari_pengajar);

        Button btnTry;

        btnTry = (Button)findViewById(R.id.btnTryCariPengajar);

        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CariPengajarActivity.this, DetailPengajarActivity.class);
                startActivity(i);
            }
        });
    }
}
