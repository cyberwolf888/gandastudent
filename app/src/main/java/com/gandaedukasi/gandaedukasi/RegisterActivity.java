package com.gandaedukasi.gandaedukasi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    EditText studentName,editEmail,editPassword,studentPhone,studenTempatLahir,studentTglLahir,studentWali,studentAddress, studentKodePos;
    Spinner studentPendidikan,studentZone;
    TextView tvKodePos;

    DatePickerDialog tglLahir;

    Button buttonReg;

    ProgressDialog pDialog;
    List<String> listZone = new ArrayList<String>();
    List<String> listTingkat = new ArrayList<String>();
    ArrayAdapter<String> dataAdapterZone;
    ArrayAdapter<String> dataAdapterTingkat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        studentName = (EditText) findViewById(R.id.studentName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        studentPhone = (EditText) findViewById(R.id.studentPhone);
        studenTempatLahir = (EditText) findViewById(R.id.studenTempatLahir);
        studentTglLahir = (EditText) findViewById(R.id.studentTglLahir);
        studentWali = (EditText) findViewById(R.id.studentWali);
        studentAddress = (EditText) findViewById(R.id.studentAddress);
        studentKodePos = (EditText) findViewById(R.id.studentKodePos);
        tvKodePos = (TextView) findViewById(R.id.tvKodePos);

        studentPendidikan = (Spinner) findViewById(R.id.studentPendidikan);
        //studentZone = (Spinner) findViewById(R.id.studentZone);

        buttonReg = (Button) findViewById(R.id.buttonReg);

        studentTglLahir.setFocusable(false);

        tvKodePos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, CariKodePosActivity.class);
                startActivity(i);
            }
        });

        setDateTimeField();

        dataAdapterTingkat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listTingkat);
        dataAdapterTingkat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentPendidikan.setAdapter(dataAdapterTingkat);

        /*dataAdapterZone = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listZone);
        dataAdapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentZone.setAdapter(dataAdapterZone);*/

        studentTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tglLahir.show();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempRegister();
            }
        });
    }

    protected void onStart(){
        super.onStart();
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            String url_cabang = new RequestServer().getServer_url()+"cabang";
            String url_tingkat = new RequestServer().getServer_url()+"tingkatpendidikan";

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("register", true);

            /*Ion.with(RegisterActivity.this)
                    .load(url_cabang)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try {
                                String status = result.get("status").toString();
                                if (status.equals("1")){
                                    JsonArray data = result.getAsJsonArray("data");
                                    for (int i=0; i<data.size(); i++){
                                        JsonObject objData = data.get(i).getAsJsonObject();
                                        listZone.add(objData.get("nama").getAsString());
                                    }
                                    dataAdapterZone.notifyDataSetChanged();
                                }else{
                                    Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception ex){
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                        }
                    });*/

            Ion.with(RegisterActivity.this)
                    .load(url_tingkat)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try {
                                String status = result.get("status").toString();
                                if (status.equals("1")){
                                    JsonArray data = result.getAsJsonArray("data");
                                    for (int i=0; i<data.size(); i++){
                                        JsonObject objData = data.get(i).getAsJsonObject();
                                        listTingkat.add(objData.get("nama").getAsString());
                                    }
                                    dataAdapterTingkat.notifyDataSetChanged();
                                }else{
                                    Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                }
                                pDialog.dismiss();
                            }catch (Exception ex){
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                pDialog.dismiss();
                            }
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
        }
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        tglLahir = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String formatted = formater.format(newDate.getTime());
                studentTglLahir.setText(formatted);
            }

        },1998, newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private void attempRegister(){
        studentName.setError(null);
        editEmail.setError(null);
        editPassword.setError(null);
        studentPhone.setError(null);
        studenTempatLahir.setError(null);
        studentTglLahir.setError(null);
        studentWali.setError(null);
        studentAddress.setError(null);
        studentKodePos.setError(null);

        String nama = studentName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String phone = studentPhone.getText().toString();
        String tempat_lahir = studenTempatLahir.getText().toString();
        String tgl_lahir = studentTglLahir.getText().toString();
        String wali = studentWali.getText().toString();
        String alamat = studentAddress.getText().toString();
        String tingkat = studentPendidikan.getSelectedItem().toString();
        String kodePos = studentKodePos.getText().toString();
        //String zona = studentZone.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        //validasi kode pos
        if (TextUtils.isEmpty(kodePos)) {
            studentKodePos.setError(getString(R.string.id_error_kode_pos_empty));
            focusView = studentKodePos;
            cancel = true;
        }

        //validasi alamat
        if (TextUtils.isEmpty(alamat)) {
            studentAddress.setError(getString(R.string.id_error_alamat_empty));
            focusView = studentAddress;
            cancel = true;
        }

        //validasi wali
        if (TextUtils.isEmpty(wali)) {
            studentWali.setError(getString(R.string.id_error_wali_empty));
            focusView = studentWali;
            cancel = true;
        }

        //validasi tgl_lahir
        if (TextUtils.isEmpty(tgl_lahir)) {
            studentTglLahir.setError(getString(R.string.id_error_tgl_lahir_empty));
            focusView = studentTglLahir;
            cancel = true;
        }

        //validasi tempat_lahir
        if (TextUtils.isEmpty(tempat_lahir)) {
            studenTempatLahir.setError(getString(R.string.id_error_tempat_lahir));
            focusView = studenTempatLahir;
            cancel = true;
        }

        //validasi phone
        if (TextUtils.isEmpty(phone)) {
            studentPhone.setError(getString(R.string.id_error_telp_empty));
            focusView = studentPhone;
            cancel = true;
        }

        //validasi password
        if (TextUtils.isEmpty(password)) {
            editPassword.setError(getString(R.string.id_error_password_empty));
            focusView = editPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            editPassword.setError(getString(R.string.id_error_passowrd_length));
            focusView = editPassword;
            cancel = true;
        }

        //validasi email
        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.id_error_email_empty));
            focusView = editEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editEmail.setError(getString(R.string.id_error_email_invalid));
            focusView = editEmail;
            cancel = true;
        }

        //validasi nama lengkap
        if (TextUtils.isEmpty(nama)) {
            studentName.setError(getString(R.string.id_error_nama_empty));
            focusView = studentName;
            cancel = true;
        } else if (!isNamaValid(nama)) {
            studentName.setError(getString(R.string.id_error_nama_invalid));
            focusView = studentName;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }else {
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            String url = new RequestServer().getServer_url()+"register/siswa";
            Log.d("Register Url",">"+url);

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("nama", nama);
            jsonReq.addProperty("email", email);
            jsonReq.addProperty("password", password);
            jsonReq.addProperty("telp", phone);
            jsonReq.addProperty("tempat_lahir", tempat_lahir);
            jsonReq.addProperty("tgl_lahir", tgl_lahir);
            jsonReq.addProperty("wali", wali);
            jsonReq.addProperty("alamat", alamat);
            jsonReq.addProperty("kodepos", kodePos);
            jsonReq.addProperty("tingkat", tingkat);
            //jsonReq.addProperty("zona", zona);
            Log.d("Request",">"+jsonReq);

            if(isNetworkAvailable()){
                Ion.with(RegisterActivity.this)
                        .load(url)
                        //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                        .setJsonObjectBody(jsonReq)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                try{
                                    String status = result.get("status").toString();
                                    if (status.equals("1")){
                                        Toast.makeText(getApplicationContext(), getString(R.string.id_success_register), Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(i);
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
                pDialog.dismiss();
            }

        }

    }

    private boolean isNamaValid(String nama){
        return nama.length() > 4;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
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
