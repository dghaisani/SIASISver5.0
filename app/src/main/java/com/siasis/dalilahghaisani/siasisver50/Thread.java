package com.siasis.dalilahghaisani.siasisver50;

import java.sql.Timestamp;
/**
 * Created by lenovo on 3/18/2015.
 */
public class Thread {
    private String id;
    private String deskripsi;
    private String judul;
    private Timestamp timestamp;
    private String idKelas;

    public Thread(String id, String deskripsi, String judul, Timestamp timestamp, String idKelas) {
        this.id = id;
        this.deskripsi = deskripsi;
        this.judul = judul;
        this.timestamp = timestamp;
        this.idKelas = idKelas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }
}
