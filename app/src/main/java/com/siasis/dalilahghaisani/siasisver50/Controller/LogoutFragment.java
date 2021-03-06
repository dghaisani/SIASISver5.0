package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siasis.dalilahghaisani.siasisver50.Controller.Jadwal.JadwalFragment;
import com.siasis.dalilahghaisani.siasisver50.MainActivity;
import com.siasis.dalilahghaisani.siasisver50.R;

import java.util.HashMap;

public class LogoutFragment extends Fragment {

    SessionManager session;

    private String username;
    private int role;

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "username";

    // Email address (make variable public to access from outside)
    public static final String KEY_ROLE = "role";

    private HashMap<String, String> detailMahasiswa;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_logout, container, false);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setMessage("Apakah Anda yakin keluar dari SIASIS ?").setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        session = new SessionManager(getActivity());
                        //detailMahasiswa = session.getUserDetails();
                        //username = detailMahasiswa.get(KEY_NAME);
                        //role = Intdeger.parseInt(detailMahasiswa.get(KEY_ROLE));

                        session.logoutUser();
                        //(new MainActivity()).deleteAlarm();
                        Intent showDetails = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        startActivity(showDetails);
                        dialog.dismiss();
                        getActivity().finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment fragment = new JadwalFragment();
                        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.remove((android.support.v4.app.Fragment) LogoutFragment.this);
                        ft.replace(R.id.frame_container, (android.support.v4.app.Fragment) fragment).commit();

                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        return rootView;

    }
}
