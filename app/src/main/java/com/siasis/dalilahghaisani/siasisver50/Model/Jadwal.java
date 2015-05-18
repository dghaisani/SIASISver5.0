package com.example.android.navigationdrawerexample.Model;

/**
 * Created by lenovo on 3/18/2015.
 */
public class Jadwal {
    private int id;
    private String judul;
    private String tanggal;
    private String waktuMulai;
    private String waktuAkhir;
    private String ruangan;
    private String deskripsi;
    private int idKelas;
    private String username;

    public Jadwal(int id, String judul, String tanggal, String waktuMulai, String waktuAkhir, String ruangan, String deskripsi, int idKelas, String username) {
        this.id = id;
        this.judul = judul;
        this.tanggal = tanggal;
        this.waktuMulai = waktuMulai;
        this.waktuAkhir = waktuAkhir;
        this.ruangan = ruangan;
        this.deskripsi = deskripsi;
        this.idKelas = idKelas;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(String waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    public String getWaktuAkhir() {
        return waktuAkhir;
    }

    public void setWaktuAkhir(String waktuAkhir) {
        this.waktuAkhir = waktuAkhir;
    }

    public String getRuangan() {
        return ruangan;
    }

    public void setRuangan(String ruangan) {
        this.ruangan = ruangan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
