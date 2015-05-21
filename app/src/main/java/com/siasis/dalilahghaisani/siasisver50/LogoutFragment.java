package com.siasis.dalilahghaisani.siasisver50;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        Button logoutYes = (Button) rootView.findViewById(R.id.buttonLogoutYes);
        Button logoutNo = (Button) rootView.findViewById(R.id.buttonLogoutNo);

        session = new SessionManager(getActivity().getApplicationContext());
        this.detailMahasiswa = session.getUserDetails();
        this.username = this.detailMahasiswa.get(KEY_NAME);
        this.role = Integer.parseInt(this.detailMahasiswa.get(KEY_ROLE));

        logoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                getActivity().finish();
            }
        });

        return rootView;

    }
}
