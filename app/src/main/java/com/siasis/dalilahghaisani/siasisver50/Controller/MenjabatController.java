package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.siasis.dalilahghaisani.siasisver50.Controller.Kelas.KelasController;
import com.siasis.dalilahghaisani.siasisver50.Controller.Profile.ProfileController;
import com.siasis.dalilahghaisani.siasisver50.Model.Kelas;
import com.siasis.dalilahghaisani.siasisver50.Model.Mahasiswa;
import com.siasis.dalilahghaisani.siasisver50.Model.Menjabat;
import com.siasis.dalilahghaisani.siasisver50.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenjabatController extends Activity{
    private String username;
    private TextView nama;
    private TextView npm;
    private TextView hp;
    private TextView email;
    ImageView foto;
    private LinearLayout linearMain;

    public MenjabatController(String username){
        this.username = username;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public MenjabatController(){
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.username = getIntent().getStringExtra("Username");

        setContentView(R.layout.view_profile_other);
        this.nama = (TextView) this.findViewById(R.id.nama_mahasiswa);
        this.npm = (TextView) this.findViewById(R.id.npm_mahasiswa);
        this.hp = (TextView) this.findViewById(R.id.nohp_mahasiswa);
        this.email = (TextView) this.findViewById(R.id.email_mahasiswa);
        this.linearMain = (LinearLayout) findViewById(R.id.role);
        this.foto = (ImageView) this.findViewById(R.id.foto_profil);
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
            dialog = new ProgressDialog(this.activity, R.style.MyTheme);
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
            Mahasiswa asdos = (new ProfileController()).getMahasiswa(mahasiswa);
            ArrayList<Kelas> arrayKelas = (new MenjabatController(username)).getMenjabatKelas(asdos.getStatus());
            nama.setText(asdos.getName());
            npm.setText(asdos.getNpm());
            hp.setText(asdos.getHp());
            email.setText(asdos.getEmail());
            if (!asdos.getPath().equals("a")){
                String urlForImage = "http://ppl-a08.cs.ui.ac.id/" + asdos.getPath();
                new DownloadImageTask(foto).execute(urlForImage);
            }else{
                foto.setImageDrawable(getResources().getDrawable(R.drawable.ava120));
            }

            TextView role1 = new TextView(getApplicationContext());
            role1.setText("Mahasiswa");
            role1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            role1.setPadding(20,20,20,20);
            role1.setTextColor(getResources().getColor(R.color.black));
            linearMain.addView(role1);
            for(int i=0; i<arrayKelas.size(); i++){
                TextView role = new TextView(getApplicationContext());
                role.setPadding(20,20,20,20);
                role.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                role.setText("Asisten Dosen: " + arrayKelas.get(i).getNama());
                role.setTextColor(getResources().getColor(R.color.black));
                linearMain.addView(role);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
/*
    public void setMenjabat(){
        String url = "http://ppl-a08.cs.ui.ac.id/menjabat.php?fun=all";
        ArrayList<Menjabat> temp = new ArrayList<Menjabat>();

        JSONArray jsonAllMenjabat = (new JSONParser()).getJSONArrayFromUrl(url);
        if(jsonAllMenjabat != null) {
            int length = jsonAllMenjabat.length();

            for (int i = 0; i < length; i++) {
                try {
                    JSONObject jsonObject = jsonAllMenjabat.getJSONObject(i);
                    temp.add(new Menjabat(jsonObject.getString("Username"), jsonObject.getInt("Id_Kelas")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            this.allMenjabat = temp;
        }
    }*/

    public ArrayList<Menjabat> getListMenjabat(){
        String url = "http://ppl-a08.cs.ui.ac.id/menjabat.php?fun=getMenjabat&username=" + this.username;
        JSONArray role = (new JSONParser()).getJSONArrayFromUrl(url);
        ArrayList<Menjabat> menjabat = new ArrayList<Menjabat>();

        if(role != null) {
            int length = role.length();
            for (int i = 0; i < length; i++) {
                try {
                    JSONObject jsonObject = role.getJSONObject(i);
                    menjabat.add(new Menjabat(jsonObject.getString("Username"), jsonObject.getInt("Id_Kelas")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } return menjabat;
    }

    public ArrayList<Kelas> getMenjabatKelas(int role){
        if(role == 2)
            return (new KelasController()).getAll();
        else {
            ArrayList<Menjabat> listMenjabat = getListMenjabat();
            ArrayList<Kelas> kelas = new ArrayList<Kelas>();

            if (listMenjabat != null && !listMenjabat.isEmpty()) {
                String listIdKelas = "(";

                for (int i = 0; i < listMenjabat.size() - 1; i++) {
                    listIdKelas += listMenjabat.get(i).getIdKelas() + ",";
                }
                listIdKelas += listMenjabat.get(listMenjabat.size() - 1).getIdKelas() + ")";

                String url = "http://ppl-a08.cs.ui.ac.id/menjabat.php?fun=getKelas&Id_Kelas=" + listIdKelas;

                JSONArray jsonkelas = (new JSONParser()).getJSONArrayFromUrl(url);

                int length = jsonkelas.length();

                for (int i = 0; i < length; i++) {
                    try {
                        JSONObject jsonObject = jsonkelas.getJSONObject(i);
                        kelas.add((new KelasController()).createKelas(jsonObject.getInt("Id"), jsonObject.getInt("Id_Semester"), jsonObject.getString("Nama")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return kelas;
        }
    }
}
