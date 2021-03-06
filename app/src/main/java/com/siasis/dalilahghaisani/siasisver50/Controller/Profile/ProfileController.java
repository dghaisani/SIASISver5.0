package com.siasis.dalilahghaisani.siasisver50.Controller.Profile;

import android.util.Log;

import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.MenjabatController;
import com.siasis.dalilahghaisani.siasisver50.Model.Mahasiswa;
import com.siasis.dalilahghaisani.siasisver50.Model.Menjabat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ProfileController {
    private String username;
    private Mahasiswa mahasiswa;

    public ProfileController(String username){
        this.username = username;
    }

    public ProfileController(){
    }

    //getRole rian yang bikin tinggal manggil getListMenjabat dari menjabat controller
    public ArrayList<Menjabat> getRole(String username){
        return (new MenjabatController(username).getListMenjabat());
    }
/*
    public boolean cekAsdos(){
        return getMahasiswa(username).getStatus() == 1;
    }

    public boolean cekAdmin(){
        return getRoleMahasiswa() == 2;
    }*/

    public int getRoleMahasiswa(){
        try {
            String url = "http://ppl-a08.cs.ui.ac.id/mahasiswa.php?fun=getStatus&Username=" + username;
            JSONObject jsonObject = (new JSONParser()).getJSONObjectFromUrl(url);
            if(jsonObject != null) {
                return jsonObject.getInt("Status");
            } else {
                return -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean punyaProfile(){
        Mahasiswa temp = getMahasiswa(username);
        return temp == null;
    }

    public void addMahasiswa(String username){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("Username", username));
        nameValuePairs.add(new BasicNameValuePair("Nama", username));
        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/createMahasiswa.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            is = entity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Log.e("Client Protocol", "Log_Tag");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Log_Tag", "IOException");
            e.printStackTrace();
        }
    }
/*
    public void setMahasiswa() {
        this.mahasiswa = getMahasiswa(username);
    }*/

    public Mahasiswa getMahasiswa(String username){
        try {
            String url = "http://ppl-a08.cs.ui.ac.id/mahasiswa.php?fun=getMahasiswa&Username=" + username;
            JSONObject jsonObject = (new JSONParser()).getJSONObjectFromUrl(url);

            if(jsonObject == null)
                return null;

            return new Mahasiswa(jsonObject.getString("Username"), jsonObject.getString("Nama"), jsonObject.getString("NPM"),
                    jsonObject.getString("HP"), jsonObject.getString("Email"), jsonObject.getString("Foto"), jsonObject.getInt("Status"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

