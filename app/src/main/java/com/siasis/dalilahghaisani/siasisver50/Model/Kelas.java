package com.example.android.navigationdrawerexample.Model;

import com.example.android.navigationdrawerexample.Menjabat;

/**
 * Created by lenovo on 3/18/2015.
 */
public class Kelas {
    private int id;
    private int id_kelas;
    private String nama;
    private Menjabat menjabat;

    public Kelas(int id, int id_kelas, String nama) {
        this.id = id;
        this.id_kelas = id_kelas;
        this.nama = nama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_kelas() {
        return id_kelas;
    }

    public void setId_kelas(int id_kelas) {
        this.id_kelas = id_kelas;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Menjabat getMenjabat() {
        return menjabat;
    }

    public void setMenjabat(Menjabat menjabat) {
        this.menjabat = menjabat;
    }
}
