package com.gandaedukasi.gandaedukasi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    Session session;
    EditText studentName, editEmail, studentPhone, studentTempatLahir, studentTglLahir, studentWali, studentAddress;
    TextView studentPendidikan, studentZona;
    ImageView studentPhoto;
    String photo;
    String imagePath;
    DatePickerDialog tglLahir;

    private final static int SELECT_PHOTO = 12345;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(EditProfileActivity.this);

        setContentView(R.layout.activity_edit_profile);

        studentName = (EditText) findViewById(R.id.studentName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        studentPhone = (EditText) findViewById(R.id.studentPhone);
        studentTempatLahir = (EditText) findViewById(R.id.studentTempatLahir);
        studentTglLahir = (EditText) findViewById(R.id.studentTglLahir);
        studentWali = (EditText) findViewById(R.id.studentWali);
        studentAddress = (EditText) findViewById(R.id.studentAddress);
        studentPendidikan = (TextView) findViewById(R.id.studentPendidikan);
        studentZona = (TextView) findViewById(R.id.studentZona);
        studentPhoto = (ImageView) findViewById(R.id.studentPhoto);

        studentName.setText(getIntent().getStringExtra("studentName"));
        editEmail.setText(getIntent().getStringExtra("studentEmail"));
        studentPhone.setText(getIntent().getStringExtra("studentPhone"));
        studentTempatLahir.setText(getIntent().getStringExtra("studentTempatLahir"));
        studentTglLahir.setText(getIntent().getStringExtra("studentTanggalLahir"));
        studentWali.setText(getIntent().getStringExtra("studentParent"));
        studentAddress.setText(getIntent().getStringExtra("studentAddress"));
        studentPendidikan.setText(getIntent().getStringExtra("studentTingkatPendidikan"));
        studentZona.setText(getIntent().getStringExtra("studentZona"));
        photo = getIntent().getStringExtra("studentPhoto");

        studentTglLahir.setFocusable(false);

        setDateTimeField();

        if(!TextUtils.isEmpty(photo)){
            String url_photo = new RequestServer().getPhotoUrl()+"/siswa/"+photo;
            Ion.with(studentPhoto).load(url_photo);
            Log.d("Url Photo",url_photo);
        }

        Button btnSave;
        btnSave = (Button)findViewById(R.id.btnSave);

        studentTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tglLahir.show();
            }
        });
        studentPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
                /*
                Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(i);
                */
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
        startActivity(i);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            try{
                Uri pickedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                cursor.moveToFirst();
                imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                //Cek file size
                File file = new File(imagePath);
                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                Log.d("File Size",">"+file_size);
                if(file_size>(3*1024)){
                    //TODO jika gambar terlalu besar
                    imagePath = "";
                    Toast.makeText(getApplicationContext(), "Ukuran gambar terlalu besar. Ukuran file maksimal 3 MB", Toast.LENGTH_LONG).show();
                }else{
                    /*BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);*/
                    studentPhoto.setImageBitmap(decodeSampledBitmapFromResource(imagePath, 100, 100));
                }
                cursor.close();
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Ukuran gambar terlalu besar. Ukuran file maksimal 3 MB", Toast.LENGTH_LONG).show();
            }

        }
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String res, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(res, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(res, options);
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

    private void editProfile(){
        studentName.setError(null);
        editEmail.setError(null);
        studentPhone.setError(null);
        studentTempatLahir.setError(null);
        studentTglLahir.setError(null);
        studentWali.setError(null);
        studentAddress.setError(null);

        String nama = studentName.getText().toString();
        String email = editEmail.getText().toString();
        String phone = studentPhone.getText().toString();
        String tempat_lahir = studentTempatLahir.getText().toString();
        String tgl_lahir = studentTglLahir.getText().toString();
        String wali = studentWali.getText().toString();
        String alamat = studentAddress.getText().toString();

        boolean cancel = false;
        View focusView = null;

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
            studentTempatLahir.setError(getString(R.string.id_error_tempat_lahir));
            focusView = studentTempatLahir;
            cancel = true;
        }

        //validasi phone
        if (TextUtils.isEmpty(phone)) {
            studentPhone.setError(getString(R.string.id_error_telp_empty));
            focusView = studentPhone;
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
            pDialog = new ProgressDialog(EditProfileActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            String url = new RequestServer().getServer_url()+"editProfileSiswa";
            Log.d("Test Url",">"+url);

            if(isNetworkAvailable()){
                if(TextUtils.isEmpty(imagePath)){
                    Ion.with(EditProfileActivity.this)
                            .load(url)
                            .setMultipartParameter("user_id", session.getUserId())
                            .setMultipartParameter("nama", nama)
                            .setMultipartParameter("email", email)
                            .setMultipartParameter("phone", phone)
                            .setMultipartParameter("tempat_lahir", tempat_lahir)
                            .setMultipartParameter("tgl_lahir", tgl_lahir)
                            .setMultipartParameter("wali", wali)
                            .setMultipartParameter("alamat", alamat)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    Log.d("Response",">"+result);
                                    try{
                                        String status = result.get("status").toString();
                                        if (status.equals("1")){
                                            Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                            startActivity(i);
                                            finish();
                                        }else{
                                            //TODO jika status 0
                                            Toast.makeText(getApplicationContext(), "Gagal menyipan data", Toast.LENGTH_LONG).show();
                                        }
                                    }catch (Exception ex){
                                        Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                    }
                                    pDialog.dismiss();
                                }
                            });
                }else{
                    Ion.with(EditProfileActivity.this)
                            .load(url)
                            .setMultipartParameter("user_id", session.getUserId())
                            .setMultipartParameter("nama", nama)
                            .setMultipartParameter("email", email)
                            .setMultipartParameter("phone", phone)
                            .setMultipartParameter("tempat_lahir", tempat_lahir)
                            .setMultipartParameter("tgl_lahir", tgl_lahir)
                            .setMultipartParameter("wali", wali)
                            .setMultipartParameter("alamat", alamat)
                            .setMultipartFile("photo", "application/images", new File(imagePath))
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    Log.d("Response",">"+result);
                                    try{
                                        String status = result.get("status").toString();
                                        if (status.equals("1")){
                                            Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                            startActivity(i);
                                            finish();
                                        }else{
                                            //TODO jika status 0
                                            Toast.makeText(getApplicationContext(), "Gagal menyipan data", Toast.LENGTH_LONG).show();
                                        }
                                    }catch (Exception ex){
                                        Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                    }
                                    pDialog.dismiss();
                                }
                            });
                }
            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }

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

    private boolean isNamaValid(String nama){
        return nama.length() > 4;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
}