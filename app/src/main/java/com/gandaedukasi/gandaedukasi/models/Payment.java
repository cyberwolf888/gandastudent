package com.gandaedukasi.gandaedukasi.models;

/**
 * Created by Karen on 8/24/2016.
 */

public class Payment {
    public String id, title, keterangan, cost;

    public Payment(String id, String title, String keterangan, String cost){
        this.id = id;
        this.title = title;
        this.keterangan = keterangan;
        this.cost = cost;
    }
}
