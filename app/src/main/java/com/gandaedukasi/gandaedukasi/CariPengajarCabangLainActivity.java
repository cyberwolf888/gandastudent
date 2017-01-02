package com.gandaedukasi.gandaedukasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.adapter.CariPengajarAdapter;
import com.gandaedukasi.gandaedukasi.adapter.CariPengajarLainAdapter;
import com.gandaedukasi.gandaedukasi.models.CariPengajar;
import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class CariPengajarCabangLainActivity extends AppCompatActivity {
    private List<CariPengajar> cariPengajars;
    protected RecyclerView mRecyclerView;
    protected CariPengajarLainAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ProgressDialog pDialog;
    public JsonArray data;

    Session session;
    String mapel_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(CariPengajarCabangLainActivity.this);
        mapel_id = getIntent().getStringExtra("mapel_id");
        setContentView(R.layout.activity_cari_pengajar_cabang_lain);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvCariPengajar);
        mLayoutManager = new LinearLayoutManager(CariPengajarCabangLainActivity.this);
    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CariPengajarCabangLainActivity.this, ListMapelJadwalActivity.class);
        startActivity(i);
        finish();
    }

    private void getData(){
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(CariPengajarCabangLainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getApplicationContext(), "Proses dibatalkan!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });

            cariPengajars = new ArrayList<>();
            data = new JsonArray();
            String url = new RequestServer().getServer_url() + "getJadwalByMapelLain";
            Log.d("test url",url);
            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());
            jsonReq.addProperty("mapel_id", mapel_id);
            Log.d("test request",jsonReq.toString());
            Ion.with(CariPengajarCabangLainActivity.this)
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
                                        String photo="";
                                        if(!objData.get("photo").isJsonNull()){
                                            photo = objData.get("photo").getAsString();
                                        }
                                        cariPengajars.add(new CariPengajar(
                                                objData.get("pengajar_id").getAsString(),
                                                objData.get("mapel_id").getAsString(),
                                                objData.get("nama_pengajar").getAsString(),
                                                objData.get("label_mapel").getAsString(),
                                                objData.get("label_tempat").getAsString(),
                                                photo
                                        ));
                                    }
                                    mAdapter = new CariPengajarLainAdapter(CariPengajarCabangLainActivity.this, cariPengajars, mapel_id);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                }else{
                                    //TODO jika status 0
                                    Toast.makeText(getApplicationContext(), "Tidak ada pengajar yang tersedia!", Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception ex){
                                Log.d("error",">"+ex);
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                finish();
                            }
                            pDialog.dismiss();
                        }
                    });

        }else{
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
