package com.example.android.navigationdrawerexample.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.navigationdrawerexample.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 5/5/2015.
 */
public class Database extends Activity {
    final Context context = this;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.new_semester);
        result = (TextView) this.findViewById(R.id.textView12);
        Button semester = (Button) this.findViewById(R.id.button13);
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
                                            new Newdatabase(Database.this).execute(nama);
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
    }

    private class Newdatabase extends AsyncTask<String,Long,JSONObject>
    {
        private ProgressDialog dialog;
        private Database activity;

        public Newdatabase(Database activity) {
            this.activity = activity;
            dialog = new ProgressDialog(this.activity);
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
