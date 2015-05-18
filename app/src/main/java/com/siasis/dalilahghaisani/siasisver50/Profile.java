package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.navigationdrawerexample.Controller.JSONParser;
import com.example.android.navigationdrawerexample.Controller.ListRoleAdapter;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Profile extends Activity implements ConfirmProfile.ConfirmProfileListener {

    String username;
    int count = 0;
    Button fetch;
    TextView text;
    EditText et, nomor, email, status, npm, nama;
    private ImageButton foto;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onResume(){
        super.onResume();

        this.foto = (ImageButton) findViewById(R.id.foto_profil);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profile_pref),
                MODE_PRIVATE);

        int defaultI=0;
        String defaultS="";
        int stats = sharedPreferences.getInt(getString(R.string.cek_pref),defaultI);
        String path = sharedPreferences.getString(getString(R.string.path_foto), defaultS);

        if (stats > 0){
            foto.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(path)));
            //new GetAllRole().execute(username);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        nama = (EditText) findViewById(R.id.etNama);
        nomor = (EditText) findViewById(R.id.etNomor);
        email = (EditText) findViewById(R.id.etEmail);
        status = (EditText) findViewById(R.id.etStatus);
        npm = (EditText) findViewById(R.id.etNPM);

        InputStream is = null;
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profile_pref),
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String sNama = nama.getText().toString();
        String sNPM = npm.getText().toString();
        String sNomor = nomor.getText().toString();
        String sEmail = email.getText().toString();

        editor.putString(getString(R.string.nama_pref), sNama);
        editor.putString(getString(R.string.npm_pref), sNPM);
        editor.putString(getString(R.string.email_pref), sEmail);
        editor.putString(getString(R.string.hp_pref), sNomor);
        editor.commit();

        nama.setText(sNama);
        npm.setText(sNPM);
        email.setText(sEmail);
        nomor.setText(sNomor);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("Nama", sNama));
        nameValuePairs.add(new BasicNameValuePair("Username", username));
        nameValuePairs.add(new BasicNameValuePair("NPM", sNPM));
        nameValuePairs.add(new BasicNameValuePair("Email", sEmail));
        nameValuePairs.add(new BasicNameValuePair("Nomor", sNomor));

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/profile.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

            String msg = "Profile updated";
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

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    /*class task extends AsyncTask<String, String, Void>
    {

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profile_pref),
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String defaultS = "";
        int defaultI = 0;
        String name = sharedPreferences.getString(getString(R.string.nama_pref), defaultS);
        String np = sharedPreferences.getString(getString(R.string.npm_pref), defaultS);
        String mail = sharedPreferences.getString(getString(R.string.email_pref), defaultS);
        String hp = sharedPreferences.getString(getString(R.string.hp_pref), defaultS);
        int stats = sharedPreferences.getInt(getString(R.string.status_pref),defaultI);

        private ProgressDialog progressDialog = new ProgressDialog(Profile.this);
        InputStream is = null ;
        String result = "";
        protected void onPreExecute() {
            if (name.isEmpty()){
                progressDialog.setMessage("Fetching data...");
                progressDialog.show();
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface arg0) {
                        task.this.cancel(true);
                    }
                });
            }
            else {

            }

        }
        @Override
        protected Void doInBackground(String... params) {
            String url_select = "http://pplkelompok5.byethost14.com/demoPPL.php";

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                //read content
                is =  httpEntity.getContent();

            } catch (Exception e) {

                Log.e("log_tag", "Error in http connection "+e.toString());
                //Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while((line=br.readLine())!=null)
                {
                    sb.append(line+"\n");
                }
                is.close();
                result=sb.toString();

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error converting result "+e.toString());
            }

            return null;

        }
        protected void onPostExecute(Void v) {


            fetch= (Button) findViewById(R.id.fetch);
            text = (TextView) findViewById(R.id.textViewNama);
            et = (EditText) findViewById(R.id.et);
            nama = (EditText) findViewById(R.id.etNama);
            nomor = (EditText) findViewById(R.id.etNomor);
            email = (EditText) findViewById(R.id.etEmail);
            status = (EditText) findViewById(R.id.etStatus);
            npm = (EditText) findViewById(R.id.etNPM);
            foto = (ImageButton) findViewById(R.id.imageButton);

            if (name.isEmpty()){
                // ambil data dari Json database
                try {
                    JSONArray Jarray = new JSONArray(result);
                    for(int i=0;i<Jarray.length();i++)
                    {
                        JSONObject Jasonobject = null;
                        //text_1 = (TextView)findViewById(R.id.txt1);
                        Jasonobject = Jarray.getJSONObject(i);

                        //get an output on the screen
                        String user = Jasonobject.getString("Username");

                        if(username.equalsIgnoreCase(user)) {
                            String name = Jasonobject.getString("Nama");
                            String np = Jasonobject.getString("NPM");
                            String mail = Jasonobject.getString("Email");
                            String hp = Jasonobject.getString("HP");
                            int stat = Jasonobject.getInt("Status");

                            editor.putString(getString(R.string.nama_pref), name);
                            editor.putString(getString(R.string.npm_pref), np);
                            editor.putString(getString(R.string.email_pref), mail);
                            editor.putString(getString(R.string.hp_pref), hp);
                            editor.putInt(getString(R.string.status_pref), stat);
                            editor.commit();

                            nama.setText(name);
                            npm.setText(np);
                            email.setText(mail);
                            nomor.setText(hp);
                            if (stat==1) status.setText("Mahasiswa");
                            break;
                        }
                        //text_1.append(id+"\t\t"+name+"\t\t"+password+"\t\t"+"\n");

                    }
                    this.progressDialog.dismiss();

                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("log_tag", "Error parsing data "+e.toString());
                }
            }
            else{
                nama.setText(name);
                npm.setText(np);
                email.setText(mail);
                nomor.setText(hp);
                if (stats==0) status.setText("Mahasiswa");
            }

        }
    }*/

    public void toViewPicture(View v){
        Intent intent = new Intent(this, ViewPicture.class);
        startActivity(intent);

        /*this.foto = (ImageButton) findViewById(R.id.imageButton);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profile_pref),
                MODE_PRIVATE);

        String defaultS = "";
        String path = sharedPreferences.getString(getString(R.string.path_foto), defaultS);
        foto.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(path)));*/
    }

    public void konfirmasiProfile (View view){
        DialogFragment dialog = new ConfirmProfile();
        dialog.show(getFragmentManager(), "ConfirmProfile");
    }

    private class GetAllRole extends AsyncTask<String,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(String... params) {

            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            String url = "http://ppl-a08.cs.ui.ac.id/role.php?fun=listrole&username=" + params[0];
            return (jsonParser.getJSONArrayFromUrl(url));
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray != null)
                setListAdapter(jsonArray);
        }
    }

    public  void setListAdapter(JSONArray jsonArray) {
        ListView listRole = (ListView) findViewById(R.id.listView);
        this.jsonArray = jsonArray;
        listRole.setAdapter(new ListRoleAdapter(jsonArray, this));
    }

    public void addRole (View view){
        Intent intent = new Intent(this, RequestRole.class);
        startActivity(intent);
    }
}

