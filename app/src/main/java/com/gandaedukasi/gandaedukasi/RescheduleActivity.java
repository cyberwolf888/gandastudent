package com.gandaedukasi.gandaedukasi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.Session;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RescheduleActivity extends AppCompatActivity {

    private String jadwal_id,pertemuan,label_mapel,label_tanggal,label_waktu,label_tempat;
    private Button btnKirim;
    private EditText tglPertemuan,tempatPertemuan,kekterangan;
    private TextView studentPertemuan,studentLecture,studentDate,studentTime,studentPlace;
    private Session session;
    private DatePickerDialog tgl_pertemuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(RescheduleActivity.this);
        jadwal_id = getIntent().getStringExtra("jadwal_id");
        pertemuan = getIntent().getStringExtra("pertemuan");
        label_mapel = getIntent().getStringExtra("label_mapel");
        label_tanggal = getIntent().getStringExtra("label_tanggal");
        label_waktu = getIntent().getStringExtra("label_waktu");
        label_tempat = getIntent().getStringExtra("label_tempat");

        setContentView(R.layout.activity_reschedule);

        btnKirim = (Button) findViewById(R.id.btnKirim);
        studentPertemuan = (TextView) findViewById(R.id.studentPertemuan);
        studentLecture = (TextView) findViewById(R.id.studentLecture);
        studentDate = (TextView) findViewById(R.id.studentDate);
        studentTime = (TextView) findViewById(R.id.studentTime);
        studentPlace = (TextView) findViewById(R.id.studentPlace);

        tglPertemuan = (EditText) findViewById(R.id.tglPertemuan);
        tempatPertemuan = (EditText) findViewById(R.id.tempatPertemuan);
        kekterangan = (EditText) findViewById(R.id.kekterangan);

        studentPertemuan.setText(pertemuan);
        studentLecture.setText(label_mapel);
        studentDate.setText(label_tanggal);
        studentTime.setText(label_waktu);
        studentPlace.setText(label_tempat);

        tglPertemuan.setFocusable(false);

        setDateTimeField();

        tglPertemuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tgl_pertemuan.show();
            }
        });

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubah_jadwal();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void ubah_jadwal(){
        tglPertemuan.setError(null);
        tempatPertemuan.setError(null);
        kekterangan.setError(null);

        String tanggal = tglPertemuan.getText().toString();
        String tempat = tempatPertemuan.getText().toString();
        String ket = kekterangan.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(tanggal)) {
            tglPertemuan.setError(getString(R.string.id_error_kode_pos_empty));
            focusView = tglPertemuan;
            cancel = true;
        }

        if (TextUtils.isEmpty(tempat)) {
            tempatPertemuan.setError(getString(R.string.id_error_kode_pos_empty));
            focusView = tempatPertemuan;
            cancel = true;
        }

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

    private void setDateTimeField() {
        final Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)+7);
        tgl_pertemuan = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String formatted = formater.format(newDate.getTime());
                if(newDate.after(newCalendar)){
                    tglPertemuan.setText(formatted);
                }else{
                    Toast.makeText(getApplicationContext(), "Pertemuan minimal 7 hari kedepan", Toast.LENGTH_LONG).show();
                }

            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

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
