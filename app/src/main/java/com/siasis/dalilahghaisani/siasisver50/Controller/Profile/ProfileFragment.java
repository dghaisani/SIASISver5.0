package com.siasis.dalilahghaisani.siasisver50.Controller.Profile;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.Controller.ApiConnector;
import com.siasis.dalilahghaisani.siasisver50.Controller.DownloadImageTask;
import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.SessionManager;
import com.siasis.dalilahghaisani.siasisver50.Model.Mahasiswa;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileFragment extends Fragment implements ConfirmProfile.ConfirmProfileListener {

    private int MODE_PRIVATE = 0;

    private String username;
    private int role;

    private ImageView foto;

    Mahasiswa mahasiswa;

    private ListView ListRole;

    private JSONArray jsonArray;

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "username";

    // Email address (make variable public to access from outside)
    public static final String KEY_ROLE = "role";

    private View rootView;

    private HashMap<String, String> detailMahasiswa;

    private static final String baseUrlForImage = "http://ppl-a08.cs.ui.ac.id/";

    SessionManager session;
    public ProfileFragment() {
        // Empty constructor required for fragment subclasses
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.view_profile_personal, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.profile_pref),
                MODE_PRIVATE);


        session = new SessionManager(getActivity().getApplicationContext());
        this.detailMahasiswa = session.getUserDetails();
        this.username = this.detailMahasiswa.get(KEY_NAME);
        this.role = Integer.parseInt(this.detailMahasiswa.get(KEY_ROLE));

        ProfileController profileController= new ProfileController(username);
        mahasiswa = profileController.getMahasiswa(username);

        foto = (ImageView) rootView.findViewById(R.id.foto_profil);

        if (mahasiswa.getName() != null){
            validasi();

            rootView.findViewById(R.id.buttonDone).setVisibility(View.INVISIBLE);

            if (!mahasiswa.getPath().equals("a")){
                new GetCustomerDetails().execute(new ApiConnector());
            }else{
                ((ImageView) rootView.findViewById(R.id.foto_profil)).setImageDrawable(getResources().getDrawable(R.drawable.ava120));
            }
            ListRole = (ListView) rootView.findViewById(R.id.list_role);

            new GetAllRole().execute(username);
        }

        rootView.findViewById(R.id.foto_profil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewPicture.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.buttonProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.findViewById(R.id.nama_mahasiswa).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.npm_mahasiswa).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.email_mahasiswa).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.nohp_mahasiswa).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.nama_mahasiswaText).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.npm_mahasiswaText).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.email_mahasiswaText).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.nohp_mahasiswaText).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.buttonDone).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.buttonProfile).setVisibility(View.INVISIBLE);
            }
        });

        rootView.findViewById(R.id.buttonDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ConfirmProfile dialog = ConfirmProfile.newInstance();
                    dialog.show(getActivity().getFragmentManager(), "ConfirmProfile");*/
                InputStream is = null;
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.profile_pref),
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String sNama = ((TextView) rootView.findViewById(R.id.nama_mahasiswa)).getText().toString();
                String sNPM = ((TextView) rootView.findViewById(R.id.npm_mahasiswa)).getText().toString();
                String sEmail = ((TextView) rootView.findViewById(R.id.email_mahasiswa)).getText().toString();
                String sNomor = ((TextView) rootView.findViewById(R.id.nohp_mahasiswa)).getText().toString();

                editor.putString(getString(R.string.nama_pref), sNama);
                editor.putString(getString(R.string.npm_pref), sNPM);
                editor.putString(getString(R.string.email_pref), sEmail);
                editor.putString(getString(R.string.hp_pref), sNomor);
                editor.commit();

                ((TextView) rootView.findViewById(R.id.nama_mahasiswa)).setText(sNama);
                ((TextView) rootView.findViewById(R.id.npm_mahasiswa)).setText(sNPM);
                ((TextView) rootView.findViewById(R.id.email_mahasiswa)).setText(sEmail);
                ((TextView) rootView.findViewById(R.id.nohp_mahasiswa)).setText(sNomor);

                ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setText(sNama);
                ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setText(sNPM);
                ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setText(sEmail);
                ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setText(sNomor);

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

                    String msg = "Profile Updated";
                    Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    Log.e("Client Protocol", "Log_Tag");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("Log_Tag", "IOException");
                    e.printStackTrace();
                }

                rootView.findViewById(R.id.nama_mahasiswa).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.npm_mahasiswa).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.email_mahasiswa).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.nohp_mahasiswa).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.nama_mahasiswaText).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.npm_mahasiswaText).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.email_mahasiswaText).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.nohp_mahasiswaText).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.buttonDone).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.buttonProfile).setVisibility(View.VISIBLE);

            }

        });

        rootView.findViewById(R.id.buttonRole).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequestRole.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        InputStream is = null;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.profile_pref),
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String sNama = ((EditText) rootView.findViewById(R.id.nama_mahasiswa)).getText().toString();
        String sNPM = ((EditText) rootView.findViewById(R.id.npm_mahasiswa)).getText().toString();
        String sEmail = ((EditText) rootView.findViewById(R.id.email_mahasiswa)).getText().toString();
        String sNomor = ((EditText) rootView.findViewById(R.id.nohp_mahasiswa)).getText().toString();

        boolean valid = true;
        if (sNama.length()>25){
            ((TextView) rootView.findViewById(R.id.nama_mahasiswa)).setText(null);
            ((TextView) rootView.findViewById(R.id.nama_mahasiswa)).setHint("Nama Mahasiswa");
            ((TextView) rootView.findViewById(R.id.nama_mahasiswa)).setHintTextColor(Color.parseColor("#FF0000"));
            Toast.makeText(getActivity().getApplicationContext(), "Nama terlalu panjang", Toast.LENGTH_LONG);
            valid = false;
        }

        if (sNPM.length()>10){
            ((TextView) rootView.findViewById(R.id.npm_mahasiswa)).setText(null);
            ((TextView) rootView.findViewById(R.id.npm_mahasiswa)).setHint("NPM Mahasiswa");
            ((TextView) rootView.findViewById(R.id.npm_mahasiswa)).setHintTextColor(Color.parseColor("#FF0000"));
            Toast.makeText(getActivity().getApplicationContext(), "NPM terlalu panjang", Toast.LENGTH_LONG);
            valid = false;
        }else if (!allDigit(sNPM)){
            ((TextView) rootView.findViewById(R.id.npm_mahasiswa)).setText(null);
            ((TextView) rootView.findViewById(R.id.npm_mahasiswa)).setHint("NPM Mahasiswa");
            ((TextView) rootView.findViewById(R.id.npm_mahasiswa)).setHintTextColor(Color.parseColor("#FF0000"));
            Toast.makeText(getActivity().getApplicationContext(), "NPM harus angka semua", Toast.LENGTH_LONG);
            valid = false;
        }

        if (sNomor.length()>15){
            ((TextView) rootView.findViewById(R.id.nohp_mahasiswa)).setText(null);
            ((TextView) rootView.findViewById(R.id.nohp_mahasiswa)).setHint("Nomor HP Mahasiswa");
            ((TextView) rootView.findViewById(R.id.nohp_mahasiswa)).setHintTextColor(Color.parseColor("#FF0000"));
            Toast.makeText(getActivity().getApplicationContext(), "Nomor HP terlalu panjang", Toast.LENGTH_LONG);
            valid = false;
        }else if (!allDigit(sNomor)){
            ((TextView) rootView.findViewById(R.id.nohp_mahasiswa)).setText(null);
            ((TextView) rootView.findViewById(R.id.nohp_mahasiswa)).setHint("Nomor HP Mahasiswa");
            ((TextView) rootView.findViewById(R.id.nohp_mahasiswa)).setHintTextColor(Color.parseColor("#FF0000"));
            Toast.makeText(getActivity().getApplicationContext(), "Nomor HP harus angka semua", Toast.LENGTH_LONG);
            valid = false;
        }

        if (sEmail.length()>25){
            ((TextView) rootView.findViewById(R.id.email_mahasiswa)).setText(null);
            ((TextView) rootView.findViewById(R.id.email_mahasiswa)).setHint("Email Mahasiswa");
            ((TextView) rootView.findViewById(R.id.email_mahasiswa)).setHintTextColor(Color.parseColor("#FF0000"));
            Toast.makeText(getActivity().getApplicationContext(), "Email terlalu panjang", Toast.LENGTH_LONG);
            valid = false;
        }else if (!isValidEmailAddress(sEmail)){
            ((TextView) rootView.findViewById(R.id.email_mahasiswa)).setText(null);
            ((TextView) rootView.findViewById(R.id.email_mahasiswa)).setHint("Email Mahasiswa");
            ((TextView) rootView.findViewById(R.id.email_mahasiswa)).setHintTextColor(Color.parseColor("#FF0000"));
            Toast.makeText(getActivity().getApplicationContext(), "Email tidak valid", Toast.LENGTH_LONG);
            valid = false;
        }

        if (valid){
            editor.putString(getString(R.string.nama_pref), sNama);
            editor.putString(getString(R.string.npm_pref), sNPM);
            editor.putString(getString(R.string.email_pref), sEmail);
            editor.putString(getString(R.string.hp_pref), sNomor);
            editor.commit();

            ((EditText) rootView.findViewById(R.id.nama_mahasiswa)).setText(sNama);
            ((EditText) rootView.findViewById(R.id.npm_mahasiswa)).setText(sNPM);
            ((EditText) rootView.findViewById(R.id.email_mahasiswa)).setText(sEmail);
            ((EditText) rootView.findViewById(R.id.nohp_mahasiswa)).setText(sNomor);

            ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setText(sNama);
            ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setText(sNPM);
            ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setText(sEmail);
            ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setText(sNomor);

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

                String msg = "Profile Updated";
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                Log.e("Client Protocol", "Log_Tag");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Log_Tag", "IOException");
                e.printStackTrace();
            }

            (rootView.findViewById(R.id.nama_mahasiswa)).setVisibility(View.INVISIBLE);
            (rootView.findViewById(R.id.npm_mahasiswa)).setVisibility(View.INVISIBLE);
            (rootView.findViewById(R.id.email_mahasiswa)).setVisibility(View.INVISIBLE);
            (rootView.findViewById(R.id.nohp_mahasiswa)).setVisibility(View.INVISIBLE);
            (rootView.findViewById(R.id.nama_mahasiswaText)).setVisibility(View.VISIBLE);
            (rootView.findViewById(R.id.npm_mahasiswaText)).setVisibility(View.VISIBLE);
            (rootView.findViewById(R.id.email_mahasiswaText)).setVisibility(View.VISIBLE);
            (rootView.findViewById(R.id.nohp_mahasiswaText)).setVisibility(View.VISIBLE);
            (rootView.findViewById(R.id.buttonDone)).setVisibility(View.INVISIBLE);
            (rootView.findViewById(R.id.buttonProfile)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    public final boolean allDigit(String s){
        boolean digit = true;

        if(s != null && !s.isEmpty()){
            for(char c : s.toCharArray()){
                if(!Character.isDigit(c)){
                    digit = false;
                    break;
                }
            }
        }
        return digit;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void validasi () {

        if (!mahasiswa.getEmail().isEmpty() && mahasiswa.getEmail() != null){
            ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setText(mahasiswa.getEmail());
            ((EditText) rootView.findViewById(R.id.email_mahasiswa)).setText(mahasiswa.getEmail());
        }
        if (!mahasiswa.getName().isEmpty() && mahasiswa.getName() != null){
            ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setText(mahasiswa.getName());
            ((EditText) rootView.findViewById(R.id.nama_mahasiswa)).setText(mahasiswa.getName());
        }
        if (!mahasiswa.getHp().isEmpty() && mahasiswa.getHp() != null){
            ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setText(mahasiswa.getHp());
            ((EditText) rootView.findViewById(R.id.nohp_mahasiswa)).setText(mahasiswa.getHp());
        }
        if (!mahasiswa.getNpm().isEmpty() && mahasiswa.getNpm() != null){
            ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setText(mahasiswa.getNpm());
            ((EditText) rootView.findViewById(R.id.npm_mahasiswa)).setText(mahasiswa.getNpm());
        }
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
                setRoleAdapter(jsonArray);
        }
    }

    public  void setRoleAdapter(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.ListRole.setAdapter(new ListRoleAdapter(jsonArray, this.getActivity()));
    }

    @Override
    public void onResume(){
        super.onResume();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.profile_pref),
                    MODE_PRIVATE);
            String defaultS = "";
            String path = sharedPreferences.getString(getString(R.string.path_foto), defaultS);
            ProfileController profileController = new ProfileController(username);

            mahasiswa = profileController.getMahasiswa(username);
            if (mahasiswa.getPath() != null)
                ((ImageView) rootView.findViewById(R.id.foto_profil)).setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(mahasiswa.getPath())));
    }

    private class GetCustomerDetails extends AsyncTask<ApiConnector,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {

            // it is executed on Background thread

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            try
            {
                String photo = mahasiswa.getPath();

                String urlForImage = baseUrlForImage + photo;
                new DownloadImageTask(foto).execute(urlForImage);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
