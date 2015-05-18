package com.example.android.navigationdrawerexample.Model;

/**
 * Created by lenovo on 3/18/2015.
 */
public class RequestJadwal {
    private String id;
    private String jam;
    private String tanggal;
    private String materi;
    private String username;
    private String idKelas;

    public RequestJadwal(String id, String idKelas, String tanggal, String materi) {
        this.id = id;
        this.jam = jam;
        this.tanggal = tanggal;
        this.materi = materi;
        this.username = username;
        this.idKelas = idKelas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getMateri() {
        return materi;
    }

    public void setMateri(String materi) {
        this.materi = materi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }
}
