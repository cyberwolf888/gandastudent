package com.gandaedukasi.gandaedukasi.models;

/**
 * Created by Karen on 8/12/2016.
 */

public class CariPengajar {
    public String pengajar_id;
    public String mapel_id;
    public String nama_pengajar;
    public String label_mapel;
    public String label_tempat;
    public String photo;

    public CariPengajar(
            String pengajar_id,
            String mapel_id,
            String nama_pengajar,
            String label_mapel,
            String label_tempat,
            String photo
            ){
        this.pengajar_id = pengajar_id;
        this.mapel_id = mapel_id;
        this.nama_pengajar = nama_pengajar;
        this.label_mapel = label_mapel;
        this.label_tempat = label_tempat;
        this.photo = "http://gandaedukasi.esy.es/images/photo/pengajar/"+photo;
    }

}
