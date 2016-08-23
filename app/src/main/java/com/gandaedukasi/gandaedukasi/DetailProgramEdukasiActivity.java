package com.gandaedukasi.gandaedukasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gandaedukasi.gandaedukasi.utility.Session;

public class DetailProgramEdukasiActivity extends AppCompatActivity {
    public String id, name, biaya, desk;
    private TextView programName, programDesk, programBiaya;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(DetailProgramEdukasiActivity.this);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        biaya = getIntent().getStringExtra("biaya");
        desk = getIntent().getStringExtra("desk");

        setContentView(R.layout.activity_detail_program_edukasi);

        programName = (TextView) findViewById(R.id.programName);
        programDesk = (TextView) findViewById(R.id.programDesk);
        programBiaya = (TextView) findViewById(R.id.programBiaya);

        programName.setText(name);
        programDesk.setText(desk);
        programBiaya.setText(biaya);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
