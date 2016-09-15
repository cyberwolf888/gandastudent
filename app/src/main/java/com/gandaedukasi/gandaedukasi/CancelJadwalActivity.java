package com.gandaedukasi.gandaedukasi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gandaedukasi.gandaedukasi.utility.Session;

public class CancelJadwalActivity extends AppCompatActivity {
    private String jadwal_id,pertemuan,label_mapel,label_tanggal,label_waktu,label_tempat;
    private Button btnKirim;
    private EditText kekterangan;
    private TextView studentPertemuan,studentLecture,studentDate,studentTime,studentPlace;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(CancelJadwalActivity.this);
        jadwal_id = getIntent().getStringExtra("jadwal_id");
        pertemuan = getIntent().getStringExtra("pertemuan");
        label_mapel = getIntent().getStringExtra("label_mapel");
        label_tanggal = getIntent().getStringExtra("label_tanggal");
        label_waktu = getIntent().getStringExtra("label_waktu");
        label_tempat = getIntent().getStringExtra("label_tempat");

        setContentView(R.layout.activity_cancel_jadwal);

        btnKirim = (Button) findViewById(R.id.btnKirim);
        studentPertemuan = (TextView) findViewById(R.id.studentPertemuan);
        studentLecture = (TextView) findViewById(R.id.studentLecture);
        studentDate = (TextView) findViewById(R.id.studentDate);
        studentTime = (TextView) findViewById(R.id.studentTime);
        studentPlace = (TextView) findViewById(R.id.studentPlace);

        kekterangan = (EditText) findViewById(R.id.kekterangan);

        studentPertemuan.setText(pertemuan);
        studentLecture.setText(label_mapel);
        studentDate.setText(label_tanggal);
        studentTime.setText(label_waktu);
        studentPlace.setText(label_tempat);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_jadwal();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void cancel_jadwal(){
        kekterangan.setError(null);

        String ket = kekterangan.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(ket)) {
            kekterangan.setError(getString(R.string.id_error_kode_pos_empty));
            focusView = kekterangan;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }else {

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
