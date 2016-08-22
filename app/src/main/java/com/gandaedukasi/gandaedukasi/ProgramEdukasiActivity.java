package com.gandaedukasi.gandaedukasi;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.adapter.ProgramEdukasiAdapter;
import com.gandaedukasi.gandaedukasi.models.ProgramEdukasi;
import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class ProgramEdukasiActivity extends AppCompatActivity {

    private List<ProgramEdukasi> programEdukasis;
    protected RecyclerView mRecyclerView;
    protected ProgramEdukasiAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ProgressDialog pDialog;
    public JsonArray data;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(ProgramEdukasiActivity.this);
        setContentView(R.layout.activity_program_edukasi);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvPprogramEdukasi);
        mLayoutManager = new LinearLayoutManager(ProgramEdukasiActivity.this);

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
            pDialog = new ProgressDialog(ProgramEdukasiActivity.this);
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

            programEdukasis = new ArrayList<>();
            data = new JsonArray();
            String url = new RequestServer().getServer_url() + "getProgramEdukasi";

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());

            Ion.with(ProgramEdukasiActivity.this)
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

                                        programEdukasis.add(new ProgramEdukasi(
                                                objData.get("id").getAsString(),
                                                objData.get("nama").getAsString(),
                                                objData.get("label_biaya").getAsString(),
                                                objData.get("desk").getAsString()
                                        ));
                                    }
                                    mAdapter = new ProgramEdukasiAdapter(ProgramEdukasiActivity.this, programEdukasis);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                }else{
                                    new AlertDialog.Builder(ProgramEdukasiActivity.this)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle("Kosong!")
                                            .setMessage("Program sedang kosong")
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent(ProgramEdukasiActivity.this, MenuActivity.class);
                                                    finish();
                                                }
                                            })
                                            .show();
                                    //Toast.makeText(getApplicationContext(), "Jadwal sedang kosong", Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception ex){
                                Log.e("Erooooor",">"+ex.toString());
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });
        }else {
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
