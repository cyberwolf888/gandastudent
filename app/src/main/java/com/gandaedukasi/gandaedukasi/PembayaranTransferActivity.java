package com.gandaedukasi.gandaedukasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.models.Payment;
import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

public class PembayaranTransferActivity extends AppCompatActivity {
    private ImageView imgBukti;
    private Button btnSubmit;
    private String imagePath,id;
    private ProgressDialog pDialog;
    private Session session;

    private final static int SELECT_PHOTO = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");
        session = new Session(PembayaranTransferActivity.this);
        setContentView(R.layout.activity_pembayaran_transfer);

        imgBukti = (ImageView) findViewById(R.id.imgBukti);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        imgBukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                    imgBukti.setImageBitmap(decodeSampledBitmapFromResource(imagePath, 300, 300));
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

    private void submit(){
        if (TextUtils.isEmpty(imagePath)) {
            Toast.makeText(getApplicationContext(), "Gambar tidak boleh kosong!", Toast.LENGTH_LONG).show();
        }else{
            if(isNetworkAvailable()){
                pDialog = new ProgressDialog(PembayaranTransferActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

                String url = new RequestServer().getServer_url()+"createBuktiPembayaran";
                Log.d("Test Url",">"+url);

                Ion.with(PembayaranTransferActivity.this)
                        .load(url)
                        .setMultipartParameter("user_id", session.getUserId())
                        .setMultipartParameter("payment_id", id)
                        .setMultipartFile("image_bukti", "application/images", new File(imagePath))
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                try{
                                    String status = result.get("status").toString();
                                    if (status.equals("1")){
                                        Intent i = new Intent(PembayaranTransferActivity.this, PaymentActivity.class);
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
                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
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

}
