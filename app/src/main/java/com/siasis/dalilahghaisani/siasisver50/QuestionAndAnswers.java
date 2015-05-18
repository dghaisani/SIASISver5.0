package com.example.android.navigationdrawerexample;

/**
 * Created by lenovo on 3/18/2015.
 */
public class QuestionAndAnswers {
    private String id;
    private String idKelas;
    private String username;

    public QuestionAndAnswers(String id, String idKelas, String username) {
        this.id = id;
        this.idKelas = idKelas;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
