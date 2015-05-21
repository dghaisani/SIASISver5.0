package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.app.Activity;
import android.util.Log;

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

/**
 * Created by lenovo on 4/5/2015.
 */
public class ProfileController extends Activity {
    private String username;
    private Mahasiswa mahasiswa;

    public ProfileController(String username){
        this.username = username;
        //this.setMahasiswa();
    }

    public ProfileController(){
    }

    //getRole rian yang bikin tinggal manggil getListMenjabat dari menjabat controller
    public ArrayList<Menjabat> getRole(String username){
        return (new MenjabatController(username).getListMenjabat());
    }

    public boolean cekAsdos(){
        //Toast.makeText(getApplicationContext(), Integer.toString(getMahasiswa(username).getStatus()), Toast.LENGTH_LONG).show();
        return getMahasiswa(username).getStatus() == 1;
    }

    public boolean cekAdmin(){
        return getRole() == 2;
    }

    public int getRole(){
        return getMahasiswa(username).getStatus();
    }

    public boolean punyaProfile(){
        Mahasiswa temp = getMahasiswa(username);
        //Toast.makeText(this, "" + !temp.equals(null), Toast.LENGTH_LONG).show();
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

    public void setMahasiswa() {
        this.mahasiswa = getMahasiswa(username);
    }

    public Mahasiswa getMahasiswa(String username){
        try {
            String url = "http://ppl-a08.cs.ui.ac.id/mahasiswa.php?fun=getMahasiswa&Username=" + username;
            JSONObject jsonObject = (new JSONParser()).getJSONObjectFromUrl(url);
            //Log.e("h", jsonObject.toString());
            if(jsonObject == null)
                return null;

            return new Mahasiswa(jsonObject.getString("Username"), jsonObject.getString("Nama"), jsonObject.getString("NPM"),
                    jsonObject.getString("HP"), jsonObject.getString("Email"), jsonObject.getString("Foto"), jsonObject.getInt("Status"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("m", "masuk ex");
            return null;
        }
    }
}

