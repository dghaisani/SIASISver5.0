package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FormQA extends Activity implements View.OnClickListener {
    String username;
    EditText id_kelas, judul, deskripsi;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_q);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //username = (EditText) findViewById(R.id.editText8);
        /*submit = (Button) findViewById(R.id.buttonSubmit);

        id_kelas = (EditText) findViewById(R.id.editTextKelas);
        judul = (EditText) findViewById(R.id.editTextJudul);
        deskripsi = (EditText) findViewById(R.id.editTextDekripsi);*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_form_q, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        InputStream is = null;
        id_kelas = (EditText) findViewById(R.id.editTextKelas);
        judul = (EditText) findViewById(R.id.editTextJudul);
        deskripsi = (EditText) findViewById(R.id.editTextDekripsi);

            String sKelas = id_kelas.getText().toString();
            String sJudul = judul.getText().toString();
            String sDeskripsi = deskripsi.getText().toString();

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("Id_Kelas", sKelas));
            nameValuePairs.add(new BasicNameValuePair("Username", username));
            nameValuePairs.add(new BasicNameValuePair("Judul", sJudul));
            nameValuePairs.add(new BasicNameValuePair("Deskripsi", sDeskripsi));

            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/QA.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();

                is = entity.getContent();

                String msg = "Berhasil";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

}
