package com.gandaedukasi.gandaedukasi.models;

/**
 * Created by Karen on 8/12/2016.
 */

public class CariPengajar {
    public String jadwal_id;
    public String pengajar_id;
    public String mapel_id;
    public String nama_pengajar;
    public String label_mapel;
    public String label_hari;
    public String waktu_mulai;
    public String waktu_selesai;
    public String label_tempat;
    public String photo;

    public CariPengajar(
            String jadwal_id,
            String pengajar_id,
            String mapel_id,
            String nama_pengajar,
            String label_mapel,
            String label_hari,
            String waktu_mulai,
            String waktu_selesai,
            String label_tempat,
            String photo
            ){
        this.jadwal_id = jadwal_id;
        this.pengajar_id = pengajar_id;
        this.mapel_id = mapel_id;
        this.nama_pengajar = nama_pengajar;
        this.label_mapel = label_mapel;
        this.label_hari = label_hari;
        this.waktu_mulai = waktu_mulai;
        this.waktu_selesai = waktu_selesai;
        this.label_tempat = label_tempat;
        this.photo = "http://gandaedukasi.esy.es/images/photo/pengajar/"+photo;
    }

}
