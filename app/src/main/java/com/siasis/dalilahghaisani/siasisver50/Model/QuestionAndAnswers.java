package com.example.android.navigationdrawerexample.Model;

/**
 * Created by lenovo on 3/18/2015.
 */
public class QuestionAndAnswers {
    private String id;
    private String idKelas;
    private String judul;

    public QuestionAndAnswers(String id, String idKelas, String judul) {
        this.id = id;
        this.idKelas = idKelas;
        this.judul = judul;
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

    public void setJudul(String username) {
        this.judul = judul;
    }
}
