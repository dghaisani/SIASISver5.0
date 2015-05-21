package com.siasis.dalilahghaisani.siasisver50;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.ListRoleAdapter;
import com.siasis.dalilahghaisani.siasisver50.Controller.ProfileController;
import com.siasis.dalilahghaisani.siasisver50.Controller.SessionManager;
import com.siasis.dalilahghaisani.siasisver50.Model.Mahasiswa;

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

public class ProfileFragment extends Fragment implements ConfirmProfile.ConfirmProfileListener{

    private int MODE_PRIVATE = 0;

    private String username;
    private int role;

    Mahasiswa mahasiswa;

    private ListView ListRole;

    private JSONArray jsonArray;

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "username";

    // Email address (make variable public to access from outside)
    public static final String KEY_ROLE = "role";

    private View rootView;

    private HashMap<String, String> detailMahasiswa;

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

            /*String defaultS = "";
            int defaultI = 0;
            String name = sharedPreferences.getString(getString(R.string.nama_pref), defaultS);
            String np = sharedPreferences.getString(getString(R.string.npm_pref), defaultS);
            String mail = sharedPreferences.getString(getString(R.string.email_pref), defaultS);
            String hp = sharedPreferences.getString(getString(R.string.hp_pref), defaultS);
            String path = sharedPreferences.getString(getString(R.string.path_foto), defaultS);
            int stats = sharedPreferences.getInt(getString(R.string.status_pref), defaultI);*/

        if (mahasiswa.getUsername() != null){
            ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setText(mahasiswa.getName());
            ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setText(mahasiswa.getNpm());
            ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setText(mahasiswa.getEmail());
            ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setText(mahasiswa.getHp());

            ((EditText) rootView.findViewById(R.id.nama_mahasiswa)).setText(mahasiswa.getName());
            ((EditText) rootView.findViewById(R.id.npm_mahasiswa)).setText(mahasiswa.getNpm());
            ((EditText) rootView.findViewById(R.id.email_mahasiswa)).setText(mahasiswa.getEmail());
            ((EditText) rootView.findViewById(R.id.nohp_mahasiswa)).setText(mahasiswa.getHp());

            if (mahasiswa.getPath() != null)
                ((ImageButton) rootView.findViewById(R.id.foto_profil)).setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(mahasiswa.getPath())));
            ListRole = (ListView) rootView.findViewById(R.id.list_role);

            new GetAllRole().execute(username);
        }else{
            ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setText("");
            ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setText("");
            ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setText("");
            ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setText("");

            ((EditText) rootView.findViewById(R.id.nama_mahasiswa)).setText("");
            ((EditText) rootView.findViewById(R.id.npm_mahasiswa)).setText("");
            ((EditText) rootView.findViewById(R.id.email_mahasiswa)).setText("");
            ((EditText) rootView.findViewById(R.id.nohp_mahasiswa)).setText("");
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
                ((EditText) rootView.findViewById(R.id.nama_mahasiswa)).setVisibility(View.VISIBLE);
                ((EditText) rootView.findViewById(R.id.npm_mahasiswa)).setVisibility(View.VISIBLE);
                ((EditText) rootView.findViewById(R.id.email_mahasiswa)).setVisibility(View.VISIBLE);
                ((EditText) rootView.findViewById(R.id.nohp_mahasiswa)).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setVisibility(View.INVISIBLE);
                ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setVisibility(View.INVISIBLE);
                ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setVisibility(View.INVISIBLE);
                ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setVisibility(View.INVISIBLE);
                ((ImageView) rootView.findViewById(R.id.buttonDone)).setVisibility(View.VISIBLE);
                ((ImageView) rootView.findViewById(R.id.buttonProfile)).setVisibility(View.INVISIBLE);
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

                ((TextView) rootView.findViewById(R.id.nama_mahasiswa)).setVisibility(View.INVISIBLE);
                ((TextView) rootView.findViewById(R.id.npm_mahasiswa)).setVisibility(View.INVISIBLE);
                ((TextView) rootView.findViewById(R.id.email_mahasiswa)).setVisibility(View.INVISIBLE);
                ((TextView) rootView.findViewById(R.id.nohp_mahasiswa)).setVisibility(View.INVISIBLE);
                ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setVisibility(View.VISIBLE);
                ((ImageView) rootView.findViewById(R.id.buttonDone)).setVisibility(View.INVISIBLE);
                ((ImageView) rootView.findViewById(R.id.buttonProfile)).setVisibility(View.VISIBLE);

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

        ((TextView) rootView.findViewById(R.id.nama_mahasiswa)).setVisibility(View.INVISIBLE);
        ((TextView) rootView.findViewById(R.id.npm_mahasiswa)).setVisibility(View.INVISIBLE);
        ((TextView) rootView.findViewById(R.id.email_mahasiswa)).setVisibility(View.INVISIBLE);
        ((TextView) rootView.findViewById(R.id.nohp_mahasiswa)).setVisibility(View.INVISIBLE);
        ((TextView) rootView.findViewById(R.id.nama_mahasiswaText)).setVisibility(View.VISIBLE);
        ((TextView) rootView.findViewById(R.id.npm_mahasiswaText)).setVisibility(View.VISIBLE);
        ((TextView) rootView.findViewById(R.id.email_mahasiswaText)).setVisibility(View.VISIBLE);
        ((TextView) rootView.findViewById(R.id.nohp_mahasiswaText)).setVisibility(View.VISIBLE);
        ((ImageView) rootView.findViewById(R.id.buttonDone)).setVisibility(View.INVISIBLE);
        ((ImageView) rootView.findViewById(R.id.buttonProfile)).setVisibility(View.VISIBLE);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

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
                ((ImageButton) rootView.findViewById(R.id.foto_profil)).setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(mahasiswa.getPath())));
    }
}
