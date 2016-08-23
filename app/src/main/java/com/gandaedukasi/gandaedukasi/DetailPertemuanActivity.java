package com.gandaedukasi.gandaedukasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailPertemuanActivity extends AppCompatActivity {
    private String jadwal_id, detail_jadwal_id, nama_siswa, no_telp, photo, label_mapel, label_tanggal, label_waktu, label_tempat, pertemuan, keterangan, kelebihan_jam, durasi, biaya;
    TextView lectureName, studentTime, studentPlace, studentPertemuan, teacherMessages, additionalDuration, additionalFee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jadwal_id = getIntent().getStringExtra("jadwal_id");
        detail_jadwal_id = getIntent().getStringExtra("detail_jadwal_id");
        nama_siswa = getIntent().getStringExtra("nama_siswa");
        no_telp = getIntent().getStringExtra("no_telp");
        photo = getIntent().getStringExtra("photo");
        label_mapel = getIntent().getStringExtra("label_mapel");
        label_tanggal = getIntent().getStringExtra("label_tanggal");
        label_waktu = getIntent().getStringExtra("label_waktu");
        label_tempat = getIntent().getStringExtra("label_tempat");
        pertemuan = getIntent().getStringExtra("pertemuan");
        keterangan = getIntent().getStringExtra("keterangan");
        kelebihan_jam = getIntent().getStringExtra("kelebihan_jam");
        durasi = getIntent().getStringExtra("durasi");
        biaya = getIntent().getStringExtra("biaya");

        setContentView(R.layout.activity_detail_pertemuan);

        lectureName = (TextView) findViewById(R.id.lectureName);
        studentTime = (TextView) findViewById(R.id.studentTime);
        studentPlace = (TextView) findViewById(R.id.studentPlace);
        studentPertemuan = (TextView) findViewById(R.id.studentPertemuan);
        teacherMessages = (TextView) findViewById(R.id.teacherMessages);
        additionalDuration = (TextView) findViewById(R.id.additionalDuration);
        additionalFee = (TextView) findViewById(R.id.additionalFee);

        lectureName.setText(label_mapel);
        studentTime.setText(label_waktu);
        studentPlace.setText(label_tempat);
        studentPertemuan.setText(pertemuan);
        teacherMessages.setText(keterangan);
        additionalDuration.setText(durasi);
        additionalFee.setText(biaya);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
