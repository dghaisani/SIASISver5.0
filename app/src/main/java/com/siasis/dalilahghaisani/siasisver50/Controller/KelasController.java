package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by lenovo on 4/5/2015.
 */
public class KelasController extends Activity {
    private String username;
    private String op;
    private LinearLayout linearMain;
    SessionManager session;

    private ArrayList<Kelas> pilihan = new ArrayList<Kelas>();

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

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> detailMahasiswa = session.getUserDetails();
        this.username = detailMahasiswa.get("username");
        this.op = getIntent().getStringExtra("View");

        if (op.equals("listKelas")) {

            setContentView(R.layout.list_kelas);

            Button create = (Button) this.findViewById(R.id.button11);
            linearMain = (LinearLayout) findViewById(R.id.container);

            new GetAllKelasTask(KelasController.this).execute(linearMain);

            create.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent showDetails = new Intent(getApplicationContext(), KelasController.class);
                    //asumsi username gak null
                    showDetails.putExtra("Username", username);
                    showDetails.putExtra("View", "createKelas");
                    startActivity(showDetails);
                    finish();
                }
            });
        } else if (op.equals("createKelas")){
            setContentView(R.layout.form_kelas);
            Button ok = (Button) findViewById(R.id.button2);
            final EditText nama = (EditText) findViewById(R.id.editText);

            ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(nama.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Hei Isi Namanya", Toast.LENGTH_LONG).show();
                        nama.setHintTextColor(Color.parseColor("#FF0000"));
                    } else if(nama.getText().toString().length() > 25) {
                        nama.setText(null);
                        nama.setHintTextColor(Color.parseColor("#FF0000"));
                        nama.setHint("max 15 karakter");

                    } else {
                        finish();
                    }
                }
            });
        }
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

    public void deleteKelas (int id){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("Id", Integer.toString(id)));
        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/deleteKelas.php");
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

    private class GetAllKelasTask extends AsyncTask<LinearLayout,Long,LinearLayout>
    {
        private ProgressDialog dialog;
        private KelasController activity;

        public GetAllKelasTask(KelasController activity) {
            this.activity = activity;
            dialog = new ProgressDialog(this.activity, R.style.MyTheme);
        }

        @Override
        protected LinearLayout doInBackground(LinearLayout... params) {
            return params[0];
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Please wait...");
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

            pilihan = (new KelasController()).getAll();
            if (!pilihan.isEmpty()){
                for (int i = 0; i < pilihan.size(); i++) {
                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                    TextView textView = new TextView(getApplicationContext());
                    textView.setId(i);
                    //Toast.makeText(getApplicationContext(), jsonArray.getJSONObject(i).getString("Nama"), Toast.LENGTH_LONG).show();
                    final String namaKelas = pilihan.get(i).getNama();
                    textView.setText(namaKelas);
                    textView.setTextColor(getResources().getColor(R.color.black));
                    linearLayout.addView(textView);

                    final Button button = new Button(getApplicationContext());
                    button.setId(i);
                    button.setText("Delete");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(KelasController.this);
                            alertDialogBuilder.setTitle("Delete Class");
                            alertDialogBuilder.setMessage("Are you sure you want to delete " + namaKelas + " ?").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                            StrictMode.setThreadPolicy(policy);

                                            int pos = button.getId();
                                            int idKelas = pilihan.get(pos).getId();
                                            Toast.makeText(getApplicationContext(), idKelas + "", Toast.LENGTH_LONG).show();
                                            deleteKelas(idKelas);
                                            Intent showDetails = new Intent(getApplicationContext(), KelasController.class);
                                            //asumsi username gak null
                                            showDetails.putExtra("Username", username);
                                            showDetails.putExtra("View", "listKelas");
                                            startActivity(showDetails);
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    });
                    linearLayout.addView(button);

                    linearLayout3.addView(linearLayout);
                } scrollView.addView(linearLayout3);
                a.addView(scrollView);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
