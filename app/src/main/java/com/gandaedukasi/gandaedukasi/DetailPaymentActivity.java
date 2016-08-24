package com.gandaedukasi.gandaedukasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

public class DetailPaymentActivity extends AppCompatActivity {
    private String id, title, keterangan, cost;
    private TextView tvTitle,tvKeterangan,tvPaymentNo,tvCost;
    private Session session;
    private Button btnCash,btnTransfer;
    private LinearLayout layout_button;
    protected ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        keterangan = getIntent().getStringExtra("keterangan");
        cost = getIntent().getStringExtra("cost");
        session = new Session(DetailPaymentActivity.this);

        setContentView(R.layout.activity_detail_payment);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvKeterangan = (TextView) findViewById(R.id.tvKeterangan);
        tvPaymentNo = (TextView) findViewById(R.id.tvPaymentNo);
        tvCost = (TextView) findViewById(R.id.tvCost);
        layout_button = (LinearLayout) findViewById(R.id.layout_button);

        tvTitle.setText(title);
        tvKeterangan.setText(keterangan);
        tvPaymentNo.setText("No. Pembayaran: "+id);
        tvCost.setText("Rp. "+cost);
        btnCash = (Button) findViewById(R.id.btnCash);
        btnTransfer = (Button) findViewById(R.id.btnTransfer);

        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailPaymentActivity.this, PembayaranCashActivity.class);
                startActivity(i);
            }
        });

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailPaymentActivity.this, PembayaranTransferActivity.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onResume(){
        super.onResume();
        cekPembayaran();
    }

    private void cekPembayaran(){
        if(isNetworkAvailable()){


            String url = new RequestServer().getServer_url()+"cekBuktiPembayaran";
            Log.d("Test Url",">"+url);
            Log.d("Test ID",">"+id);
            Ion.with(DetailPaymentActivity.this)
                    .load(url)
                    .setMultipartParameter("pembayaran_id", id)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                String status = result.get("status").toString();
                                Log.d("status",">"+status);
                                if (status.equals("0")){
                                    layout_button.setVisibility(View.VISIBLE);
                                }
                            }catch (Exception ex){
                                Log.e("Errooooooor",">"+ex);
                            }
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
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
