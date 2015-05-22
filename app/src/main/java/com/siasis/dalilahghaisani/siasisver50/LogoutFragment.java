package com.siasis.dalilahghaisani.siasisver50;

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

import com.siasis.dalilahghaisani.siasisver50.Controller.SessionManager;

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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Unenroll Class");
        alertDialogBuilder.setMessage("Apakah kamu yakin ingin logout ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        session = new SessionManager(getActivity().getApplicationContext());
                        detailMahasiswa = session.getUserDetails();
                        username = detailMahasiswa.get(KEY_NAME);
                        role = Integer.parseInt(detailMahasiswa.get(KEY_ROLE));

                        session.logoutUser();
                        Intent showDetails = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        startActivity(showDetails);
                        getActivity().finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        return rootView;

    }
}
