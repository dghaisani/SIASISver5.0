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
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.Jadwal.JadwalController;
import com.siasis.dalilahghaisani.siasisver50.Controller.NavigationDrawerActivity;
import com.siasis.dalilahghaisani.siasisver50.Controller.NotifReceiver;
import com.siasis.dalilahghaisani.siasisver50.Controller.Profile.ProfileController;
import com.siasis.dalilahghaisani.siasisver50.Controller.SessionManager;
import com.siasis.dalilahghaisani.siasisver50.Model.Jadwal;
import com.siasis.dalilahghaisani.siasisver50.Model.Mahasiswa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

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
        String[] in = new String[2];
        in[0] = username.getText().toString().toLowerCase();
        in[1] = password.getText().toString();
        new LoginTask(MainActivity.this).execute(in);
    }

    public void  startNotif (String username){
        ArrayList<Jadwal> jadwals = (new JadwalController()).getJadwalToday(username);
        if(jadwals != null) {
            Log.e("masuk notif", "enggak null");
            for (int i = 0; i < jadwals.size(); i++) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                Intent notif = new Intent(this, JadwalController.class);
                notif.putExtra("JadwalID", jadwals.get(i).getId());
                notif.putExtra("KelasID", jadwals.get(i).getIdKelas());
                notif.putExtra("Username", username);
                notif.putExtra("View", "detailJadwal");

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(JadwalController.class);
                stackBuilder.addNextIntent(notif);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentTitle(jadwals.get(i).getJudul());
                builder.setContentText(jadwals.get(i).getWaktuMulai().substring(0, jadwals.get(i).getWaktuMulai().length() - 3)
                        + " - " + jadwals.get(i).getWaktuAkhir().substring(0, jadwals.get(i).getWaktuAkhir().length() - 3));
                builder.setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(i, builder.build());
                Log.e("masuk notif", i+"");
            }
        }
    }
    public void deleteAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, NotifReceiver.class);
        intent.putExtra("jenis", "harian");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        alarmManager.cancel(pendingIntent);

        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent2 = new Intent(MainActivity.this, NotifReceiver.class);
        intent2.putExtra("jenis", "menitan");
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(MainActivity.this, 0, intent2, 0);

        alarmManager2.cancel(pendingIntent2);
    }

    private class LoginTask extends AsyncTask<String[],Long,JSONObject>
    {
        private ProgressDialog dialog;

        private MainActivity activity;

        public LoginTask(MainActivity activity) {
            this.activity = activity;
            dialog = new ProgressDialog(this.activity, R.style.MyTheme);
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

                    //startNotif(username);
//
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(MainActivity.this, NotifReceiver.class);
                    intent.putExtra("jenis", "harian");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 6);
                    calendar.set(Calendar.MINUTE, 0);

                    alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);

                    AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent intent2 = new Intent(MainActivity.this, NotifReceiver.class);
                    intent2.putExtra("jenis", "menitan");
                    //intent2.putExtra("username", username);
                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(MainActivity.this, 0, intent2, 0);

                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTimeInMillis(System.currentTimeMillis());
                    calendar2.set(Calendar.HOUR_OF_DAY, 0);
                    calendar2.set(Calendar.MINUTE, 30);

                    alarmManager2.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_HALF_HOUR, pendingIntent2);

                    Intent showDetails = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                    startActivity(showDetails);
                    finish();
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                    alertDialog.setTitle("Login gagal..");
                    alertDialog.setMessage("Salah memasukkan username/password");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("kena ex", "di main");
            }
        }
    }
}