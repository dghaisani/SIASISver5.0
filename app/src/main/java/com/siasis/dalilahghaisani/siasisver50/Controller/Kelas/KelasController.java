package com.siasis.dalilahghaisani.siasisver50.Controller.Kelas;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.SessionManager;
import com.siasis.dalilahghaisani.siasisver50.Model.Kelas;
import com.siasis.dalilahghaisani.siasisver50.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class KelasController extends Activity {
    SessionManager session;

    public KelasController(){
    }

    public Kelas createKelas(int id, int id_semester, String name){
        return new Kelas(id, id_semester, name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

            setContentView(R.layout.form_kelas);
            ImageView ok = (ImageView) findViewById(R.id.button2);
            ImageView back = (ImageView) findViewById(R.id.back_addkelas);

            back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
            final EditText nama = (EditText) findViewById(R.id.editText);

            ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(nama.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Silahkan isi nama kelas baru", Toast.LENGTH_LONG).show();
                        nama.setHintTextColor(Color.parseColor("#FF0000"));
                    } else if(nama.getText().toString().length() > 25) {
                        nama.setText(null);
                        nama.setHintTextColor(Color.parseColor("#FF0000"));
                        nama.setHint("max 15 karakter");

                    } else {
                        addKelas(nama.getText().toString());
                        finish();
                    }
                }
            });
    }

    public void addKelas(String nama){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("Nama", nama));
        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/createKelas.php");
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

    public ArrayList<Kelas> getAllClass(String username){
        ArrayList<Kelas> kelas = new ArrayList<Kelas>();
        String url = "http://ppl-a08.cs.ui.ac.id/kelas.php?fun=notEnroll&Username=" + username;
        JSONArray jsonArray = (new JSONParser()).getJSONArrayFromUrl(url);

        if(jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    kelas.add(createKelas(jsonObject.getInt("Id"), jsonObject.getInt("Id_Semester"), jsonObject.getString("Nama")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }return kelas;
    }

    public ArrayList<Kelas> getAll(){
        ArrayList<Kelas> kelas = new ArrayList<Kelas>();
        String url = "http://ppl-a08.cs.ui.ac.id/kelas.php?fun=all";
        JSONArray jsonArray = (new JSONParser()).getJSONArrayFromUrl(url);

        if(jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    kelas.add(createKelas(jsonObject.getInt("Id"), jsonObject.getInt("Id_Semester"), jsonObject.getString("Nama")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }return kelas;
    }
}
