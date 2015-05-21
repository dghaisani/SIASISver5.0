package com.siasis.dalilahghaisani.siasisver50;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ASUS on 5/20/2015.
 */
public class AdminDatabaseFragment extends Fragment{

    final Context context = this.context;
    TextView result;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        rootView = inflater.inflate(R.layout.new_semester, container, false);

        result = (TextView) rootView.findViewById(R.id.textView12);
        Button semester = (Button) rootView.findViewById(R.id.button13);
        semester.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.dialog_semester, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String nama = userInput.getText().toString();
                                        if (nama.equals("") || nama.contains("/") || nama.length() > 20) {
                                            //Toast.makeText(getApplicationContext(), nama.contains("/") + "", Toast.LENGTH_LONG).show();
                                            result.setText("Nama tidak mengandung /, max: 20");
                                            result.setTextColor(Color.parseColor("#FF0000"));
                                            dialog.cancel();
                                        } else {
                                            new Newdatabase(AdminDatabaseFragment.this).execute(nama);
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        return rootView;
    }

    private class Newdatabase extends AsyncTask<String,Long,JSONObject>
    {
        private ProgressDialog dialog;
        private AdminDatabaseFragment activity;

        public Newdatabase(AdminDatabaseFragment activity) {
            this.activity = activity;
            dialog = new ProgressDialog(this.activity.getActivity());
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            String url = "http://ppl-a08.cs.ui.ac.id/database.php?nama=" + params[0];
            return (jsonParser.getJSONObjectFromUrl(url));
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Sedang mengambil data...");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(JSONObject jsonArray) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                result.setTextColor(Color.parseColor("#000000"));
                result.setText(jsonArray.getString("status"));
                //Toast.makeText(getApplicationContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
