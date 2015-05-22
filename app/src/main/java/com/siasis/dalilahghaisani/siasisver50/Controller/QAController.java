package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.Model.Kelas;
import com.siasis.dalilahghaisani.siasisver50.Model.QuestionAndAnswers;
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

public class QAController extends Activity {

    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pertanyaan_toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.username = getIntent().getStringExtra("Username");

        ImageView sendQuestion = (ImageView) findViewById(R.id.buttonSendQuestion);

        final Spinner idkelas = (Spinner)findViewById(R.id.spinnerQuestion);
        final TextView usernameQuestion = (TextView) findViewById(R.id.req_jadwal_question_username);
        usernameQuestion.setText(username);
        final EditText judulQuestion = (EditText) findViewById(R.id.pertanyaan_judul_question);
        final EditText deskripsiQuestion = (EditText) findViewById(R.id.req_jadwal_question_deskripsi);

        final ArrayList<Kelas> arrayKelas = getKelas();
        final ArrayList<Integer> temp_id = new ArrayList<Integer>();
        final String[] daftarKelas = new String[arrayKelas.size()];

        for (int i =0; i<arrayKelas.size();i++){
            daftarKelas[i] = arrayKelas.get(i).getNama();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, daftarKelas);

        idkelas.setAdapter(adapter);
        idkelas.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        int position = idkelas.getSelectedItemPosition();
                        temp_id.add(arrayKelas.get(position).getId());
                        //Toast.makeText(getApplicationContext(), "You have selected " + daftarKelas[position], Toast.LENGTH_LONG).show();
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );

        sendQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sKelas = temp_id.get(0).toString();
                String sJudul = judulQuestion.getText().toString();

                String sDeskripsi = deskripsiQuestion.getText().toString();
                String sUsername = usernameQuestion.getText().toString();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("Id_Kelas", sKelas));
                nameValuePairs.add(new BasicNameValuePair("Username", sUsername));
                nameValuePairs.add(new BasicNameValuePair("Judul", sJudul));
                nameValuePairs.add(new BasicNameValuePair("Deskripsi", sDeskripsi));

                addQuestion(nameValuePairs);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public ArrayList<Kelas> getKelas(){
        String url = "http://ppl-a08.cs.ui.ac.id/question.php?fun=kelasforum&username=" + username;

        JSONArray jsonkelas = (new JSONParser()).getJSONArrayFromUrl(url);

        int length = jsonkelas.length();
        ArrayList<Kelas> kelas = new ArrayList<Kelas>();

        for(int i=0; i<length; i++){
            try {
                JSONObject jsonObject = jsonkelas.getJSONObject(i);
                kelas.add((new KelasController()).createKelas(jsonObject.getInt("Id"), jsonObject.getInt("Id_Semester"), jsonObject.getString("Nama")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return kelas;
    }

    public void addQuestion (List<NameValuePair> nameValuePairs){
        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/question.php?fun=a");
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
        finish();
    }

    public QuestionAndAnswers createQA (String id, String idKelas, String judul){
        return new QuestionAndAnswers (id, idKelas,judul);
    }

    public ArrayList<QuestionAndAnswers> getAllQA(){
        ArrayList<QuestionAndAnswers> qa= new ArrayList<QuestionAndAnswers>();
        String url = "http://ppl-a08.cs.ui.ac.id/question.php?fun=listallfaq";
        JSONArray jsonArray = (new JSONParser()).getJSONArrayFromUrl(url);

        if(jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    qa.add(createQA(Integer.toString(jsonObject.getInt("Id")),
                            Integer.toString(jsonObject.getInt("Id_Kelas")),jsonObject.getString("Judul")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }return qa;
    }

    public void finish (View view){
        finish();
    }
}
