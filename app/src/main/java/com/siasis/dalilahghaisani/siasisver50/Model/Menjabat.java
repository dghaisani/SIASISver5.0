package com.siasis.dalilahghaisani.siasisver50.Model;

/**
 * Created by lenovo on 3/18/2015.
 */
public class Menjabat {
    private String username;
    private int idKelas;

    public Menjabat(String username, int idKelas) {
        this.username = username;
        this.idKelas = idKelas;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }
}
