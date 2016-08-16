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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;

public class PilihPaketctivity extends AppCompatActivity {
    ListView listView;
    ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> itemList;
    JsonArray mData;
    String pengajar_id,mapel_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pengajar_id = getIntent().getStringExtra("pengajar_id");
        mapel_id = getIntent().getStringExtra("mapel_id");
        setContentView(R.layout.activity_pilih_paketctivity);

        listView = (ListView) findViewById(R.id.paket_listview);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume(){
        super.onResume();
        getPaket();

    }
    private void getPaket(){
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(PilihPaketctivity.this);
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
            String url = new RequestServer().getServer_url()+"getPaket";
            Log.d("Login Url",">"+url);

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", true);
            Log.d("Req Data",">"+jsonReq);

            Ion.with(PilihPaketctivity.this)
                    .load(url)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            Log.d("Response",">"+result);
                            try{
                                String status = result.get("status").toString();
                                Log.d("status",">"+status);
                                if (status.equals("1")){
                                    mData = result.getAsJsonArray("data");
                                    ArrayList<HashMap<String, String>> xitemList = new ArrayList<HashMap<String, String>>();
                                    for(int i=0; i<mData.size(); i++){
                                        JsonObject objData = mData.get(i).getAsJsonObject();
                                        HashMap<String, String> dataList = new HashMap<String, String>();
                                        dataList.put("nama",objData.get("nama").getAsString());
                                        dataList.put("jumlah_pertemuan",objData.get("jumlah_pertemuan").getAsString()+" Pertemuan");
                                        dataList.put("durasi",objData.get("durasi").getAsString()+" Menit");
                                        dataList.put("label_harga","Rp. "+objData.get("label_harga").getAsString());
                                        xitemList.add(dataList);
                                    }
                                    itemList = xitemList;
                                    Log.d("ItemList",">"+itemList);
                                    ListAdapter adapter = new SimpleAdapter(
                                            PilihPaketctivity.this,
                                            itemList,
                                            R.layout.list_item_paket,
                                            new String[]{"nama","jumlah_pertemuan","durasi","label_harga"},
                                            new int[]{R.id.nama_paket,R.id.durasi_paket,R.id.jml_pertemuan_paket,R.id.harga_paket}
                                    );
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            JsonObject objSelected = mData.get(position).getAsJsonObject();
                                            Intent i = new Intent(PilihPaketctivity.this, BuatJadwalActivity.class);
                                            i.putExtra("pengajar_id",pengajar_id);
                                            i.putExtra("mapel_id",mapel_id);
                                            i.putExtra("paket_id",objSelected.get("paket_id").getAsString());
                                            i.putExtra("tarif_id",objSelected.get("tarif_id").getAsString());
                                            i.putExtra("jumlah_pertemuan",objSelected.get("jumlah_pertemuan").getAsString());
                                            i.putExtra("harga",objSelected.get("harga").getAsString());
                                            startActivity(i);
                                            Log.d("Selected :","> " + objSelected);
                                        }
                                    });
                                }else{
                                    //TODO jika status 0;
                                }
                            }catch (Exception ex){
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
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
