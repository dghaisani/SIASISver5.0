package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lenovo on 5/15/2015.
 */
public class NotifReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String jenis = intent.getStringExtra("jenis");
        if(jenis.equals("harian")) {
            Intent service = new Intent(context, NotifService.class);
            //service.putExtra("jenis", intent.getStringExtra("jenis"));
            context.startService(service);
        } else {
            Intent service = new Intent(context, NotifRHarian.class);
            //service.putExtra("jenis", intent.getStringExtra("jenis"));
            context.startService(service);
        }
    }
}
