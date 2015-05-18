package com.example.android.navigationdrawerexample.Model;

import java.sql.Timestamp;

/**
 * Created by lenovo on 3/18/2015.
 */
public class Reply {
    private String id;
    private String isi;
    private Timestamp timestamp;
    private String idKelas;
    private String idThread;
    private String username;

    public Reply(String id, String isi, Timestamp timestamp, String idKelas, String idThread, String username) {
        this.id = id;
        this.isi = isi;
        this.timestamp = timestamp;
        this.idKelas = idKelas;
        this.idThread = idThread;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
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

    public String getIdThread() {
        return idThread;
    }

    public void setIdThread(String idThread) {
        this.idThread = idThread;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
