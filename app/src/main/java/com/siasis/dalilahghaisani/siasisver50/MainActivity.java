/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.siasis.dalilahghaisani.siasisver50;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.ProfileController;
import com.siasis.dalilahghaisani.siasisver50.Controller.SessionManager;
import com.siasis.dalilahghaisani.siasisver50.Model.Mahasiswa;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements View.OnClickListener {
    EditText username, password;
    Button ok;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.login);
        session = new SessionManager(getApplicationContext());

        if(session.isLoggedIn()){
            Intent showDetails = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
            startActivity(showDetails);
            finish();
        }

        username = (EditText) findViewById(R.id.editText8);
        password = (EditText) findViewById(R.id.editText9);
        ok = (Button) findViewById(R.id.button3);
        ok.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        //cek dah nih valid apa enggak
//        Intent showDetails = new Intent(this, PilihanController.class);
//        String usernameMahasiswa = username.getText().toString();
//        showDetails.putExtra("Username", usernameMahasiswa);
//        startActivity(showDetails);
//        finish();
        String[] in = new String[2];
        in[0] = username.getText().toString().toLowerCase();
        in[1] = password.getText().toString();
        new LoginTask(MainActivity.this).execute(in);
    }

    private class LoginTask extends AsyncTask<String[],Long,JSONObject>
    {
        private ProgressDialog dialog;

        private MainActivity activity;

        public LoginTask(MainActivity activity) {
            this.activity = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected JSONObject doInBackground(String[]... params) {
            String url = "http://ppl-a08.cs.ui.ac.id/login.php?username="+params[0][0]+"&password="+params[0][1];
            return (new JSONParser()).getJSONObjectFromUrl(url);
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Sedang mengambil data...");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(JSONObject mahasiswa) {
            try {
                if(mahasiswa.getInt("state") == 1) {
                    String username = mahasiswa.getString("username");
                    ProfileController profileController = new ProfileController(username);
                    Mahasiswa temp = profileController.getMahasiswa(username);
                    int status = 0;

                    if(temp == null){
                        profileController.addMahasiswa(username);
                    } else {
                        status = temp.getStatus();
                    }

                    session.createLoginSession(username, status);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    Intent showDetails = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                    startActivity(showDetails);
                    finish();
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                    alertDialog.setTitle("Login failed..");
                    alertDialog.setMessage("Username/Password is incorrect");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
