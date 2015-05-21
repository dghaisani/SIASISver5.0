package com.siasis.dalilahghaisani.siasisver50.Model;

import java.util.ArrayList;

/**
 * Created by lenovo on 3/18/2015.
 */
public class Mahasiswa {
    private String username;
    private String name;
    private String npm;
    private String hp;
    private String email;
    private String path;
    private int status;

    private ArrayList<Menjabat> menjabat;
    private ArrayList<RequestRole> requestRole;
    private ArrayList<Enroll> enroll;

    public Mahasiswa(String username, String name, String npm, String hp, String email, String path, int status) {
        this.username = username;
        this.name = name;
        this.npm = npm;
        this.hp = hp;
        this.email = email;
        this.path = path;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Enroll> getEnroll() {
        return enroll;
    }

    public void setEnroll(ArrayList<Enroll> enroll) {
        this.enroll = enroll;
    }

    public ArrayList<RequestRole> getRequestRole() {
        return requestRole;
    }

    public void setRequestRole(ArrayList<RequestRole> requestRole) {
        this.requestRole = requestRole;
    }

    public ArrayList<Menjabat> getMenjabat() {
        return menjabat;
    }

    public void setMenjabat(ArrayList<Menjabat> menjabat) {
        this.menjabat = menjabat;
    }
}
