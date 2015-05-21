package com.siasis.dalilahghaisani.siasisver50;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.KelasController;
import com.siasis.dalilahghaisani.siasisver50.Controller.SessionManager;
import com.siasis.dalilahghaisani.siasisver50.Model.Kelas;

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

/**
 * Created by ASUS on 5/20/2015.
 */
public class AdminKelasFragment extends Fragment {

    private String username;
    private String op;
    private LinearLayout linearMain;
    SessionManager session;

    private View rootView;

    private ArrayList<Kelas> pilihan = new ArrayList<Kelas>();

    public Kelas createKelas(int id, int id_semester, String name){
        return new Kelas(id, id_semester, name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //session = new SessionManager(getActivity().getApplicationContext());
        //HashMap<String, String> detailMahasiswa = session.getUserDetails();
        //this.username = detailMahasiswa.get("username");
        this.username = "rian.fitriansyah";

        op = getActivity().getIntent().getStringExtra("View");

        if (op==null){
            op = "listKelas";
        }

        if (op.equals("listKelas")) {

            rootView = inflater.inflate(R.layout.list_kelas, container, false);

            Button create = (Button) rootView.findViewById(R.id.button11);
            linearMain = (LinearLayout) rootView.findViewById(R.id.container);

            new GetAllKelasTask(AdminKelasFragment.this).execute(linearMain);

            create.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent showDetails = new Intent(getActivity().getApplicationContext(), KelasController.class);
                    //asumsi username gak null
                    showDetails.putExtra("Username", username);
                    showDetails.putExtra("View", "createKelas");
                    startActivity(showDetails);
                }
            });
        } else if (op.equals("createKelas")){
            rootView = inflater.inflate(R.layout.form_kelas, container, false);

            Button ok = (Button) rootView.findViewById(R.id.button2);
            final EditText nama = (EditText) rootView.findViewById(R.id.editText);

            ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(nama.getText().toString().equals("")){
                        Toast.makeText(getActivity().getApplicationContext(), "Hei Isi Namanya", Toast.LENGTH_LONG).show();
                        nama.setHintTextColor(Color.parseColor("#FF0000"));
                    } else if(nama.getText().toString().length() > 25) {
                        nama.setText(null);
                        nama.setHintTextColor(Color.parseColor("#FF0000"));
                        nama.setHint("max 15 karakter");

                    } else {
                        addKelas(nama.getText().toString());
                        Intent showDetails = new Intent(getActivity().getApplicationContext(), AdminKelasFragment.class);
                        showDetails.putExtra("Username", username);
                        showDetails.putExtra("View", "listKelas");
                        startActivity(showDetails);
                        getActivity().finish();
                    }
                }
            });
        }
        return rootView;
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
        private AdminKelasFragment activity;

        public GetAllKelasTask(AdminKelasFragment activity) {
            this.activity = activity;
            dialog = new ProgressDialog(this.activity.getActivity());
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
            ScrollView scrollView = new ScrollView(getActivity().getApplicationContext());
            scrollView.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));

            LinearLayout linearLayout3 = new LinearLayout(getActivity().getApplicationContext());
            linearLayout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout3.setOrientation(LinearLayout.VERTICAL);

            pilihan = (new KelasController()).getAll();
            if (!pilihan.isEmpty()){
                for (int i = 0; i < pilihan.size(); i++) {
                    LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                    TextView textView = new TextView(getActivity().getApplicationContext());
                    textView.setId(i);
                    //Toast.makeText(getApplicationContext(), jsonArray.getJSONObject(i).getString("Nama"), Toast.LENGTH_LONG).show();
                    final String namaKelas = pilihan.get(i).getNama();
                    textView.setText(namaKelas);
                    textView.setTextColor(getResources().getColor(R.color.black));
                    linearLayout.addView(textView);

                    final Button button = new Button(getActivity().getApplicationContext());
                    button.setId(i);
                    button.setText("Delete");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminKelasFragment.this.getActivity());
                            alertDialogBuilder.setTitle("Delete Class");
                            alertDialogBuilder.setMessage("Are you sure you want to delete " + namaKelas + " ?").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                            StrictMode.setThreadPolicy(policy);

                                            int pos = button.getId();
                                            int idKelas = pilihan.get(pos).getId();
                                            Toast.makeText(getActivity().getApplicationContext(), idKelas + "", Toast.LENGTH_LONG).show();
                                            deleteKelas(idKelas);
                                            Intent showDetails = new Intent(getActivity().getApplicationContext(), AdminKelasFragment.class);
                                            //asumsi username gak null
                                            showDetails.putExtra("Username", username);
                                            showDetails.putExtra("View", "listKelas");
                                            startActivity(showDetails);
                                            getActivity().finish();
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
