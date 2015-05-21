package com.siasis.dalilahghaisani.siasisver50.Model;

/**
 * Created by lenovo on 3/18/2015.
 */
public class RequestRole {
    private String username;
    private int idKelas;
    private int status;

    public RequestRole(String username, int idKelas, int status) {
        this.username = username;
        this.idKelas = idKelas;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
