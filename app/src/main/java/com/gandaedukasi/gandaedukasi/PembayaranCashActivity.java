package com.gandaedukasi.gandaedukasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PembayaranCashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_cash);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
