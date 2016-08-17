package com.gandaedukasi.gandaedukasi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BuatJadwalActivity extends AppCompatActivity {
    private String pengajar_id,mapel_id,paket_id,tarif_id,jumlah_pertemuan,harga;
    private LinearLayout li;
    private DatePickerDialog tgl_pertemuan;
    private TimePickerDialog waktu_pertemuan;
    private Session session;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pengajar_id = getIntent().getStringExtra("pengajar_id");
        mapel_id = getIntent().getStringExtra("mapel_id");
        paket_id = getIntent().getStringExtra("paket_id");
        tarif_id = getIntent().getStringExtra("tarif_id");
        jumlah_pertemuan = getIntent().getStringExtra("jumlah_pertemuan");
        harga = getIntent().getStringExtra("harga");
        session = new Session(BuatJadwalActivity.this);
        //Log.d("Data",">"+pengajar_id+" "+mapel_id+" "+paket_id+" "+tarif_id+" "+jumlah_pertemuan+" "+harga+" ");

        setContentView(R.layout.activity_buat_jadwal);

        li = (LinearLayout) findViewById(R.id.linierLayout);

        for (int i=0; i<Integer.valueOf(jumlah_pertemuan); i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 0, 3);
            TextInputLayout til = new TextInputLayout(BuatJadwalActivity.this);
            til.setLayoutParams(params);
            til.setHint("Tanggal Pertemuan "+String.valueOf(i+1));
            li.addView(til);

            final EditText etTglPertemuan = new EditText(BuatJadwalActivity.this);
            etTglPertemuan.setLayoutParams(params);
            etTglPertemuan.setId(30+i);
            etTglPertemuan.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
            etTglPertemuan.setFocusable(false);
            til.addView(etTglPertemuan);
            etTglPertemuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDateField(etTglPertemuan);
                    tgl_pertemuan.show();
                }
            });

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params2.setMargins(0, 0, 0, 10);
            TextInputLayout til2 = new TextInputLayout(BuatJadwalActivity.this);
            til2.setLayoutParams(params2);
            til2.setHint("Waktu Pertemuan "+String.valueOf(i+1));
            li.addView(til2);

            final EditText etWaktuPertemuan = new EditText(BuatJadwalActivity.this);
            etWaktuPertemuan.setLayoutParams(params);
            etWaktuPertemuan.setId(40+i);
            etWaktuPertemuan.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
            etWaktuPertemuan.setFocusable(false);
            til2.addView(etWaktuPertemuan);
            etWaktuPertemuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTimeField(etWaktuPertemuan);
                    waktu_pertemuan.show();
                }
            });

        }

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params3.setMargins(0, 0, 0, 0);
        Button btnSubmit = new Button(BuatJadwalActivity.this);
        btnSubmit.setLayoutParams(params3);
        btnSubmit.setText("Selesai");
        btnSubmit.setTextSize(18);
        btnSubmit.setTextColor(getResources().getColor(R.color.colorPrimary));
        //btnSubmit.setBackground(getResources().getDrawable(R.drawable.buttonround));
        li.addView(btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempBuatJadwal();
            }
        });

    }
    private void attempBuatJadwal(){
        boolean cancel = false;
        View focusView = null;

        JsonObject jsonReq = new JsonObject();
        jsonReq.addProperty("user_id", session.getUserId());
        jsonReq.addProperty("pengajar_id", pengajar_id);
        jsonReq.addProperty("mapel_id", mapel_id);
        jsonReq.addProperty("paket_id", paket_id);

        for (int i=0; i<Integer.valueOf(jumlah_pertemuan); i++){
            EditText etTglPertemuan = (EditText) findViewById(30+i);
            EditText etWaktuPertemuan = (EditText) findViewById(40+i);

            etTglPertemuan.setError(null);
            etWaktuPertemuan.setError(null);

            if (TextUtils.isEmpty(etTglPertemuan.getText().toString())) {
                etTglPertemuan.setError("Tanggal pertemuan tidak boleh kosong!");
                focusView = etTglPertemuan;
                cancel = true;
            }
            if (TextUtils.isEmpty(etWaktuPertemuan.getText().toString())) {
                etWaktuPertemuan.setError("Waktu pertemuan tidak boleh kosong!");
                focusView = etWaktuPertemuan;
                cancel = true;
            }
            jsonReq.addProperty("tgl_pertemuan"+i, etTglPertemuan.getText().toString());
            jsonReq.addProperty("waktu_pertemuan"+i, etWaktuPertemuan.getText().toString());
        }
        Log.d("Request",">"+jsonReq);
        if (cancel) {
            focusView.requestFocus();
        }else {
            if(isNetworkAvailable()){
                pDialog = new ProgressDialog(BuatJadwalActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

                String url = new RequestServer().getServer_url()+"buatJadwal";
                Log.d("test Url",">"+url);

                Ion.with(BuatJadwalActivity.this)
                        .load(url)
                        //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                        .setJsonObjectBody(jsonReq)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                Log.d("Response",result.toString());
                                try{
                                    String status = result.get("status").toString();
                                    if (status.equals("1")){
                                        Toast.makeText(getApplicationContext(), "Berhasil membuat jadwal, mohon tunggu verfikasi dari pengajar!", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(BuatJadwalActivity.this, MenuActivity.class);
                                        startActivity(i);
                                        finish();
                                    }else{
                                        String error = result.get("error").toString();
                                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
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
    }
    private void setDateField(final EditText et) {
        Calendar newCalendar = Calendar.getInstance();
        tgl_pertemuan = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String formatted = formater.format(newDate.getTime());
                et.setText(formatted);
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    private void setTimeField(final EditText et) {
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        waktu_pertemuan = new TimePickerDialog(BuatJadwalActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                et.setText( hourOfDay + ":" + minuteOfDay);
            }
        }, hour, minute, true);
        waktu_pertemuan.setTitle("Waktu Pertemuan");
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
