package com.gandaedukasi.gandaedukasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.adapter.PaymentAdapter;
import com.gandaedukasi.gandaedukasi.models.Payment;
import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    private List<Payment> listdata;
    protected RecyclerView mRecyclerView;
    protected PaymentAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ProgressDialog pDialog;
    public JsonArray data;
    private RelativeLayout main_layout;
    private TextView tvTotal;
    private Button btnCash,btnTransfer;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(PaymentActivity.this);
        setContentView(R.layout.activity_payment);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        main_layout = (RelativeLayout) findViewById(R.id.main_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvPayment);
        mLayoutManager = new LinearLayoutManager(PaymentActivity.this);

        /*
        btnCash = (Button) findViewById(R.id.btnCash);
        btnTransfer = (Button) findViewById(R.id.btnTransfer);

        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentActivity.this, PembayaranCashActivity.class);
                startActivity(i);
            }
        });

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentActivity.this, PembayaranTransferActivity.class);
                startActivity(i);
            }
        });
        */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
    }

    private void getData(){
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(PaymentActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getApplicationContext(), "Proses dibatalkan!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            pDialog.show();

            listdata = new ArrayList<>();
            data = new JsonArray();
            String url = new RequestServer().getServer_url() + "getPayment";

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());

            Ion.with(PaymentActivity.this)
                    .load(url)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                String status = result.get("status").getAsString();
                                if (status.equals("1")) {
                                    data = result.getAsJsonArray("data");
                                    for (int i=0; i<data.size(); i++){
                                        JsonObject objData = data.get(i).getAsJsonObject();
                                        listdata.add(new Payment(
                                                objData.get("id").getAsString(),
                                                objData.get("title").getAsString(),
                                                objData.get("keterangan").getAsString(),
                                                objData.get("cost").getAsString()
                                        ));
                                    }
                                    mAdapter = new PaymentAdapter(PaymentActivity.this, listdata);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                    tvTotal.setText("Rp. "+result.get("total").getAsString());
                                    main_layout.setVisibility(View.VISIBLE);
                                }else{
                                    new AlertDialog.Builder(PaymentActivity.this)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle("Kosong")
                                            .setMessage("Maaf, pembayaran sedang tidak tersedia")
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent(PaymentActivity.this, MenuActivity.class);
                                                    finish();
                                                }
                                            })
                                            .show();
                                    //Toast.makeText(getApplicationContext(), "Jadwal sedang kosong", Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception ex){
                                Log.e("Erooooor",">"+ex.toString());
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                finish();
                            }
                            pDialog.dismiss();
                        }
                    });
        }else {
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
            finish();
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
