package com.example.android.navigationdrawerexample.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.navigationdrawerexample.Menjabat;
import com.example.android.navigationdrawerexample.Model.Kelas;
import com.example.android.navigationdrawerexample.Model.Mahasiswa;
import com.example.android.navigationdrawerexample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lenovo on 4/5/2015.
 */
public class MenjabatController extends Activity{
    private String username;
    private ArrayList<Menjabat> allMenjabat;
    private TextView username_asdos;
    private TextView nama;
    private TextView npm;
    private TextView hp;
    private TextView email;
    private LinearLayout linearMain;

    public MenjabatController(String username){
        this.username = username;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public MenjabatController(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.username = getIntent().getStringExtra("Username");

        setContentView(R.layout.view_profile_personal);
        //this.username_asdos = (TextView) this.findViewById(R.id.username_mahasiswa);
        this.nama = (TextView) this.findViewById(R.id.nama_mahasiswaText);
        this.npm = (TextView) this.findViewById(R.id.npm_mahasiswaText);
        this.hp = (TextView) this.findViewById(R.id.nohp_mahasiswaText);
        this.email = (TextView) this.findViewById(R.id.email_mahasiswaText);
        this.linearMain = (LinearLayout) findViewById(R.id.role);

        new GetProfile(MenjabatController.this).execute(this.username);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private class GetProfile extends AsyncTask<String,Long,String>
    {
        private ProgressDialog dialog;
        private MenjabatController activity;

        public GetProfile(MenjabatController activity) {
            this.activity = activity;
            dialog = new ProgressDialog(this.activity);
        }
        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Sedang mengambil data...");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(String mahasiswa) {
            //Log.e("k", mahasiswa);
            Mahasiswa asdos = (new ProfileController()).getMahasiswa(mahasiswa);
            Log.e("k", (asdos == null) + "");
            ArrayList<Kelas> arrayKelas = (new MenjabatController(mahasiswa)).getMenjabatKelas();
            //username_asdos.setText(asdos.getUsername());
            nama.setText(asdos.getName());
            npm.setText(asdos.getNpm());
            hp.setText(asdos.getHp());
            email.setText(asdos.getEmail());

            TextView role1 = new TextView(getApplicationContext());
            role1.setText("Mahasiswa");
            role1.setTextColor(getResources().getColor(R.color.black));
            linearMain.addView(role1);
            for(int i=0; i<arrayKelas.size(); i++){
                TextView role = new TextView(getApplicationContext());
                role.setText("Asisten Dosen: " + arrayKelas.get(i).getNama());
                role.setTextColor(getResources().getColor(R.color.black));
                linearMain.addView(role);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public void setMenjabat(){
        String url = "http://ppl-a08.cs.ui.ac.id/menjabat.php?fun=all";
        ArrayList<Menjabat> temp = new ArrayList<Menjabat>();

        JSONArray jsonAllMenjabat = (new JSONParser()).getJSONArrayFromUrl(url);
        int length = jsonAllMenjabat.length();

        for(int i=0; i<length; i++){
            try {
                JSONObject jsonObject = jsonAllMenjabat.getJSONObject(i);
                temp.add(new Menjabat(jsonObject.getString("Username"), jsonObject.getInt("Id_Kelas")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } this.allMenjabat = temp;
    }

    public ArrayList<Menjabat> getAllMenjabat(){
        return this.allMenjabat;
    }

    public ArrayList<Menjabat> getListMenjabat(){
        String url = "http://ppl-a08.cs.ui.ac.id/menjabat.php?fun=getMenjabat&username=" + this.username;
        JSONArray role = (new JSONParser()).getJSONArrayFromUrl(url);

        int length = role.length();
        ArrayList<Menjabat> menjabat = new ArrayList<Menjabat>();

        for(int i=0; i<length; i++){
            try {
                JSONObject jsonObject = role.getJSONObject(i);
                menjabat.add(new Menjabat(jsonObject.getString("Username"), jsonObject.getInt("Id_Kelas")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return menjabat;
    }

    public ArrayList<Kelas> getMenjabatKelas(){
        ArrayList<Menjabat> listMenjabat = getListMenjabat();

        String listIdKelas = "(";

        for (int i=0; i<listMenjabat.size()-1; i++){
            listIdKelas += listMenjabat.get(i).getIdKelas() + ",";
        } listIdKelas += listMenjabat.get(listMenjabat.size()-1).getIdKelas() + ")";

        String url = "http://ppl-a08.cs.ui.ac.id/menjabat.php?fun=getKelas&Id_Kelas=" + listIdKelas;

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
}
