package com.siasis.dalilahghaisani.siasisver50.Controller;

/**
 * Created by lenovo on 5/20/2015.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.siasis.dalilahghaisani.siasisver50.Model.Jadwal;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lenovo on 5/15/2015.
 */
public class NotifRHarian extends Service {
    private NotificationManager nManager;
    SessionManager session;
    private String username;
    Intent intent;

    @Override
    public IBinder onBind(Intent intent) {
        this.intent=intent;
        return null;
    }

    public void onCreate(){
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("notif", "harian");
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> detailMahasiswa = session.getUserDetails();
        this.username = detailMahasiswa.get("username");
        //if(jenis.equals("harian")){
            ArrayList<Jadwal> jadwals = (new JadwalController()).getJadwalToday(username);
            if(jadwals != null) {
                for (int i = 0; i < jadwals.size(); i++) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                    final String w_mulai = jadwals.get(i).getWaktuMulai().substring(0, jadwals.get(i).getWaktuMulai().length() - 3);
                    final String w_akhir = jadwals.get(i).getWaktuAkhir().substring(0, jadwals.get(i).getWaktuAkhir().length() - 3);
                    builder.setContentTitle(jadwals.get(i).getJudul());
                    builder.setContentText(w_mulai + " - " + w_akhir + ", R." + jadwals.get(i).getRuangan());
                    builder.setAutoCancel(true);

                    Intent notif = new Intent(this, JadwalController.class);
                    notif.putExtra("JadwalID", jadwals.get(i).getId());
                    notif.putExtra("KelasID", jadwals.get(i).getIdKelas());
                    notif.putExtra("Username", username);
                    notif.putExtra("View", "detailJadwal");

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addParentStack(JadwalController.class);
                    stackBuilder.addNextIntent(notif);

                    PendingIntent pendingIntent = stackBuilder.getPendingIntent(jadwals.get(i).getId(), PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setContentIntent(pendingIntent);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(jadwals.get(i).getId(), builder.build());

                    //                notif.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //                PendingIntent pendingIntent = PendingIntent.getActivity(this, jadwals.get(i).getId(),
                    //                        notif, PendingIntent.FLAG_UPDATE_CURRENT);
                    //
                    //                builder.setContentIntent(pendingIntent);
                    //                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE)
                }
            }
//        } else if (jenis.equals("menitan")){
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//            String currentTime = dateFormat.format(new Date());
//            StringTokenizer st = new StringTokenizer(currentTime, " ");
//            String day = st.nextToken();
//            String time = st.nextToken();
//
//            String[] t = time.split(":");
//            if(t[0].compareToIgnoreCase("6") > 0){
//                ArrayList<Jadwal> jadwals = (new JadwalController()).getJadwalUpdate(username);
//                if(jadwals != null) {
//                    for (int i = 0; i < jadwals.size(); i++) {
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//
//                        Intent notif = new Intent(this, JadwalController.class);
//                        notif.putExtra("JadwalID", jadwals.get(i).getId());
//                        notif.putExtra("KelasID", jadwals.get(i).getIdKelas());
//                        notif.putExtra("Username", username);
//                        notif.putExtra("View", "detailJadwal");
//
//                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//                        stackBuilder.addParentStack(JadwalController.class);
//                        stackBuilder.addNextIntent(notif);
//
//                        PendingIntent pendingIntent = stackBuilder.getPendingIntent(jadwals.get(i).getId(), PendingIntent.FLAG_UPDATE_CURRENT);
//
//                        builder.setContentIntent(pendingIntent);
//                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                        notificationManager.notify(jadwals.get(i).getId(), builder.build());
//                    }
//                }
//            }
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
