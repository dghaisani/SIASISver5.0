package com.siasis.dalilahghaisani.siasisver50.Model;


import java.util.List;

/**
 * Created by lenovo on 3/18/2015.
 */
public class Polling {
    private String id;
    private String idKelas;
    private String judul;
    private List<Pilihan> pilihan;

    public Polling(String id, String idKelas, String judul) {
        this.id = id;
        this.idKelas = idKelas;
        this.judul = judul;
        this.pilihan = pilihan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public List<Pilihan> getPilihan() {
        return pilihan;
    }

    public void setPilihan(List<Pilihan> pilihan) {
        this.pilihan = pilihan;
    }
}
