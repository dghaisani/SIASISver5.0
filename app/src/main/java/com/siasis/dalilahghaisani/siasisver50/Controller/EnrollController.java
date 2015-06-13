package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.Controller.Kelas.KelasController;
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
import java.util.HashMap;
import java.util.List;

public class EnrollController extends Activity{
    private LinearLayout linearMain;
    private String username;
    private ArrayList<CheckBox> addKelas = new ArrayList<CheckBox>();
    private ArrayList<Kelas> pilihan = new ArrayList<Kelas>();
    SessionManager session;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> detailMahasiswa = session.getUserDetails();
        this.username = detailMahasiswa.get("username");

        setContentView(R.layout.add_enroll);
        linearMain = (LinearLayout) findViewById(R.id.container);
        ImageView enroll = (ImageView) findViewById(R.id.button);
        ImageView back = (ImageView) findViewById(R.id.back_enroll);
        new GetAllKelasTask(EnrollController.this).execute(linearMain);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        enroll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String kelas = "";
                for (int i = 0; i < addKelas.size(); i++) {
                    if (addKelas.get(i).isChecked()) {
                        kelas += pilihan.get(addKelas.get(i).getId()).getId() + " ";
                        /*Toast.makeText(getApplicationContext(),
                                "Kamu meng-enroll Kelas "+ pilihan.get(addKelas.get(i).getId()).getNama(), Toast.LENGTH_LONG).show();*/
                    }
                }
                if (!kelas.equals("")){
                    kelas = kelas.substring(0, kelas.length() - 1);
                    addEnroll(username, kelas);
                }
                finish();
            }
        });
    }

    public List<String> getAllAsdosKelas(int id){
        List<String> listDaftarAsdos = new ArrayList<String>();
        String url = "http://ppl-a08.cs.ui.ac.id/enroll.php?fun=getAsdos&Id_Kelas=" + id;
        JSONArray jsonArray = (new JSONParser()).getJSONArrayFromUrl(url);

        if(jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    listDaftarAsdos.add(jsonObject.getString("Username"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }return listDaftarAsdos;
    }

    private class GetAllKelasTask extends AsyncTask<LinearLayout,Long,LinearLayout>
    {
        private ProgressDialog dialog;
        private EnrollController activity;

        public GetAllKelasTask(EnrollController activity) {
            this.activity = activity;
            dialog = new ProgressDialog(this.activity, R.style.MyTheme);
        }

        @Override
        protected LinearLayout doInBackground(LinearLayout... params) {
            return params[0];
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Sedang mengambil data...");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(LinearLayout a) {
            ScrollView scrollView = new ScrollView(getApplicationContext());
            scrollView.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));

            LinearLayout linearLayout3 = new LinearLayout(getApplicationContext());
            linearLayout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout3.setOrientation(LinearLayout.VERTICAL);
            //linearLayout3.setPadding(30,30,30,30);

            pilihan = (new KelasController()).getAllClass(username);
            if (!pilihan.isEmpty()){
                for (int i = 0; i < pilihan.size(); i++) {
                    CheckBox checkBox = new CheckBox(getApplicationContext());
                    checkBox.setId(i);
                    checkBox.setText(pilihan.get(i).getNama());
                    checkBox.setTextColor(getResources().getColor(R.color.black));
                    checkBox.setTextSize(22);
                    checkBox.setPadding(20,0,20,20);
                    linearLayout3.addView(checkBox);
                    addKelas.add(checkBox);
                } scrollView.addView(linearLayout3);
                a.addView(scrollView);
            }
            if(pilihan.isEmpty()){
                TextView none = new TextView(getApplicationContext());
                none.setText("Seluruh kelas telah di enroll");
                none.setPadding(10, 30, 10, 10);
                none.setGravity(Gravity.CENTER);
                none.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                none.setTextColor(getResources().getColor(R.color.black));
                a.addView(none);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public void addEnroll(String username, String kelas) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("Kelas", kelas));
        nameValuePairs.add(new BasicNameValuePair("Username", username));

        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/createEnroll.php");
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

    public void deleteEnroll(final String username, final String kelas) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("Kelas", kelas));
        nameValuePairs.add(new BasicNameValuePair("Username", username));

        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/deleteEnroll.php");
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

}
