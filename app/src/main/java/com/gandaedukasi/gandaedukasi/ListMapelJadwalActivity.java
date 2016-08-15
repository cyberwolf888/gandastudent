package com.gandaedukasi.gandaedukasi;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.gandaedukasi.gandaedukasi.utility.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;

public class ListMapelJadwalActivity extends AppCompatActivity {
    Session session;
    ListView listView;
    ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> itemList;
    JsonArray mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(ListMapelJadwalActivity.this);
        setContentView(R.layout.activity_list_mapel_jadwal);

        listView = (ListView) findViewById(R.id.mapel_listview);
    }

    @Override
    protected void onResume(){
        super.onResume();
        getMapel();
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(ListMapelJadwalActivity.this, MenuActivity.class);
        ComponentName cn = i.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        startActivity(mainIntent);
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void getMapel(){
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(ListMapelJadwalActivity.this);
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

            String url = new RequestServer().getServer_url()+"getMapelPelajar";
            Log.d("Login Url",">"+url);

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());
            Log.d("Req Data",">"+jsonReq);
            Ion.with(ListMapelJadwalActivity.this)
                    .load(url)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            Log.d("Response Data",">"+result);
                            try{
                                String status = result.get("status").toString();
                                Log.d("status",">"+status);

                                if (status.equals("1")){
                                    mData = result.getAsJsonArray("data");
                                    ArrayList<HashMap<String, String>> xitemList = new ArrayList<HashMap<String, String>>();
                                    for(int i=0; i<mData.size(); i++){
                                        JsonObject objData = mData.get(i).getAsJsonObject();
                                        HashMap<String, String> dataList = new HashMap<String, String>();
                                        dataList.put("mapel_id",objData.get("id").getAsString());
                                        dataList.put("label_mapel",objData.get("label_mapel").getAsString());
                                        xitemList.add(dataList);
                                    }

                                    itemList = xitemList;
                                    Log.d("ItemList",">"+itemList);

                                    ListAdapter adapter = new SimpleAdapter(
                                            ListMapelJadwalActivity.this,
                                            itemList,
                                            R.layout.list_item_mapel_jadwal,
                                            new String[]{"label_mapel"},
                                            new int[]{R.id.label_mapel}
                                    );
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            JsonObject objSelected = mData.get(position).getAsJsonObject();
                                            Intent i = new Intent(ListMapelJadwalActivity.this, CariPengajarActivity.class);
                                            i.putExtra("mapel_id",objSelected.get("id").getAsString());
                                            startActivity(i);
                                            Log.d("Selected :","> " + objSelected);
                                        }
                                    });
                                }else{
                                    //TODO jika status 0
                                    Toast.makeText(getApplicationContext(), "Tidak ada mata pelajaran yang tersedia untuk tingkat pendidikan anda!", Toast.LENGTH_LONG).show();
                                }

                            }catch (Exception ex){
                                Log.d("Eroor",ex.toString());
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });

        }else {
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
        }
    }
}
