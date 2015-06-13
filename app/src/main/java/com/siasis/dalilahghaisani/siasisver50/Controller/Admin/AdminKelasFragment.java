package com.siasis.dalilahghaisani.siasisver50.Controller.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.siasis.dalilahghaisani.siasisver50.Controller.Kelas.KelasController;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminKelasFragment extends Fragment {

    private String username;
    private String op;
    private LinearLayout linearMain;
    SessionManager session;

    private View rootView;

    private ArrayList<Kelas> pilihan = new ArrayList<Kelas>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session = new SessionManager(getActivity());
        HashMap<String, String> detailMahasiswa = session.getUserDetails();
        this.username = detailMahasiswa.get("username");

        op = getActivity().getIntent().getStringExtra("View");

        if (op==null){
            op = "listKelas";
        }

        if (op.equals("listKelas")) {

            this.rootView = inflater.inflate(R.layout.list_kelas, container, false);

            this.linearMain = (LinearLayout) rootView.findViewById(R.id.container);
            ImageView buttonKelas = (ImageView) rootView.findViewById(R.id.buttonAddKelas);

            new GetAllKelasTask(AdminKelasFragment.this).execute(linearMain);

            buttonKelas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent showDetails = new Intent(getActivity(), KelasController.class);
                        //asumsi username gak null
                        showDetails.putExtra("Username", username);
                        showDetails.putExtra("View", "createKelas");
                        startActivity(showDetails);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        linearMain.removeAllViews();
        new GetAllKelasTask(AdminKelasFragment.this).execute(linearMain);
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
            dialog = new ProgressDialog(this.activity.getActivity(), R.style.MyTheme);
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
        protected void onPostExecute(final LinearLayout a) {
            ScrollView scrollView = new ScrollView(getActivity().getApplicationContext());
            scrollView.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));

            LinearLayout linearLayout3 = new LinearLayout(getActivity().getApplicationContext());
            linearLayout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout3.setOrientation(LinearLayout.VERTICAL);


            pilihan = (new KelasController()).getAll();
            if (!pilihan.isEmpty()){
                for (int i = 0; i < pilihan.size(); i++) {
                    final LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setPadding(70,30,40,10);
                    linearLayout.setId(i);

                    TextView textView = new TextView(getActivity().getApplicationContext());
                    textView.setId(i);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    final String namaKelas = pilihan.get(i).getNama();
                    textView.setText(namaKelas);
                    textView.setTextColor(getResources().getColor(R.color.black));
                    textView.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));

                    linearLayout.addView(textView);

                    final ImageView button = new ImageView(getActivity().getApplicationContext());
                    button.setImageResource(R.drawable.ic_deny);
                    button.setPadding(0,10,40,10);
                    button.setId(i);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminKelasFragment.this.getActivity());
                            alertDialogBuilder.setTitle("Hapus Kelas");
                            alertDialogBuilder.setMessage("Apakah Anda yakin untuk menghapus " + namaKelas + " ?").setCancelable(false)
                                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                            StrictMode.setThreadPolicy(policy);

                                            int pos = button.getId();
                                            int idKelas = pilihan.get(pos).getId();

                                            deleteKelas(idKelas);

                                            onResume();
                                        }
                                    })
                                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
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
