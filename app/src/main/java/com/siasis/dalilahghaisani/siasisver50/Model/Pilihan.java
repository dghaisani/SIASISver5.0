package com.example.android.navigationdrawerexample.Model;

/**
 * Created by lenovo on 3/18/2015.
 */
public class Pilihan {
    private String waktu;
    private String idKelas;
    private String idPolling;

    public Pilihan(String waktu, String idKelas, String idPolling) {
        this.waktu = waktu;
        this.idKelas = idKelas;
        this.idPolling = idPolling;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }

    public String getIdPolling() {
        return idPolling;
    }

    public void setIdPolling(String idPolling) {
        this.idPolling = idPolling;
    }
}
