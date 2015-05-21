package com.siasis.dalilahghaisani.siasisver50.Model;

/**
 * Created by lenovo on 3/18/2015.
 */
public class Hadir {
    private String idJadwal;
    private String username;
    private String idKelas;

    public Hadir(String idJadwal, String username, String idKelas) {
        this.idJadwal = idJadwal;
        this.username = username;
        this.idKelas = idKelas;
    }

    public String getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal;
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
