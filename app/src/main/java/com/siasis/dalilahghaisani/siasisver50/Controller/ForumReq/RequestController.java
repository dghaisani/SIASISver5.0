package com.siasis.dalilahghaisani.siasisver50.Controller.ForumReq;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.Kelas.KelasController;
import com.siasis.dalilahghaisani.siasisver50.Model.Kelas;
import com.siasis.dalilahghaisani.siasisver50.Model.RequestJadwal;
import com.siasis.dalilahghaisani.siasisver50.R;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RequestController extends Activity {

    String username;

    TextView textViewTanggalReq;
    TextView textViewWaktuReq;

    TextView usernameReq;
    EditText judulReq;
    EditText deskripsiReq;

    int kelasId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.req_jadwal_asis_toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.username = getIntent().getStringExtra("Username");

        ImageView bTanggalReq = (ImageView) findViewById(R.id.buttonTanggalReq);
        ImageView bWaktuReq = (ImageView) findViewById(R.id.buttonWaktuReq);
        ImageView sendReq = (ImageView) findViewById(R.id.buttonSendReq);
        ImageView back = (ImageView) findViewById(R.id.back_jadwalbaru);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Calendar calendar = Calendar.getInstance();
        final Calendar calendarWaktu = Calendar.getInstance();

        textViewTanggalReq = (TextView) findViewById(R.id.textViewTanggalReq);
        textViewWaktuReq = (TextView) findViewById(R.id.textViewWaktuReq);
        final Spinner idkelas = (Spinner)findViewById(R.id.spinnerReq);
        usernameReq = (TextView) findViewById(R.id.req_jadwal_asis_username);
        usernameReq.setText(username);
        judulReq = (EditText) findViewById(R.id.req_jadwal_asis_judul);
        deskripsiReq = (EditText) findViewById(R.id.req_jadwal_asis_deskripsi);

        bTanggalReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //view.setSpinnersShown(false);
                        //view.setCalendarViewShown(true);

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        textViewTanggalReq.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                        //Toast.makeText(getApplicationContext(), "Tahun=" + calendar.get(Calendar.YEAR) + "-Bulan=" +
                        // calendar.get(Calendar.MONTH) + "-Tanggal=" + calendar.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_LONG).show();
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(RequestController.this, d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setSpinnersShown(false);
                dialog.getDatePicker().setCalendarViewShown(true);
                dialog.show();
            }
        });

        bWaktuReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener d = new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        view.setIs24HourView(true);
                        calendarWaktu.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendarWaktu.set(Calendar.MINUTE, minute);

                        String sMinute;

                        if((""+minute).length() == 1){
                            sMinute = "0" + minute;
                        } else {
                            sMinute = "" + minute;
                        }

                        String sHourOfDay;

                        if((""+hourOfDay).length() == 1){
                            sHourOfDay = "0" + hourOfDay;
                        } else {
                            sHourOfDay = "" + hourOfDay;
                        }

                        textViewWaktuReq.setText(sHourOfDay + ":" + sMinute);
                    }
                };
                new TimePickerDialog(RequestController.this, d, calendarWaktu.get(Calendar.HOUR_OF_DAY), calendarWaktu.get(Calendar.MINUTE), true).show();
            }
        });

        final ArrayList<Kelas> arrayKelas = getKelas();
        final ArrayList<Integer> temp_id = new ArrayList<Integer>();
        final String[] daftarKelas = new String[arrayKelas.size()];

        for (int i =0; i<arrayKelas.size();i++){
            daftarKelas[i] = arrayKelas.get(i).getNama();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, daftarKelas);

        idkelas.setAdapter(adapter);
        idkelas.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        int position = idkelas.getSelectedItemPosition();
                        kelasId = arrayKelas.get(position).getId();
                        Toast.makeText(getApplicationContext(), "You have selected " + daftarKelas[position], Toast.LENGTH_LONG).show();
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );
        sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sKelas = Integer.toString(kelasId);
                String sJudul = judulReq.getText().toString();

                String sTanggal = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                String sMulai = textViewWaktuReq.getText().toString() + ":00";
                String sDeskripsi = deskripsiReq.getText().toString();
                String sUsername = usernameReq.getText().toString();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat parserTime = new SimpleDateFormat("HH:mm:ss");

                if (sJudul.isEmpty() || sTanggal.isEmpty() || sMulai.isEmpty() || sDeskripsi.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Input data tidak valid", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean temp = true;
                if (sJudul.equals("") || sJudul.trim().length() <= 0) {
                    Log.e("1", "haha");
                    judulReq.setHint("Judul tidak boleh kosong");
                    judulReq.setHintTextColor(Color.parseColor("#FF0000"));
                    temp = false;
                } else if (sJudul.length() > 25) {
                    Log.e("2", "haha");
                    judulReq.setText(null);
                    judulReq.setHintTextColor(Color.parseColor("#FF0000"));
                    judulReq.setHint("Judul, max 25 karakter");
                    temp=false;
                }

                if (sDeskripsi.equals("") || sDeskripsi.trim().length() <= 0) {
                    Log.e("3", "haha");
                    deskripsiReq.setHint("Deskripsi tidak boleh kosong");
                    deskripsiReq.setHintTextColor(Color.parseColor("#FF0000"));
                    temp = false;
                } else if (sDeskripsi.length() > 255) {
                    Log.e("4", "haha");
                    deskripsiReq.setText(null);
                    deskripsiReq.setHintTextColor(Color.parseColor("#FF0000"));
                    deskripsiReq.setHint("contoh: 2403, max 10 karakter");
                    temp=false;
                }

                if (sMulai.isEmpty()){
                    Log.e("5", "haha");
                    textViewWaktuReq.setText("Waktu tidak boleh kosong");
                    textViewWaktuReq.setHintTextColor(Color.parseColor("#FF0000"));
                    temp = false;
                }

                Date date1 = null;

                try {
                    date1 = simpleDateFormat.parse(sTanggal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = new Date();

                if (sTanggal.isEmpty()){
                    Log.e("7", "haha");
                    textViewTanggalReq.setHint("Tanggal tidak boleh kosong");
                    textViewTanggalReq.setHintTextColor(Color.parseColor("#FF0000"));
                    temp = false;
                }else if(date1.compareTo(date2)<0){
                    Log.e("8", "haha");
                    textViewTanggalReq.setHint("Tanggal telah berlalu");
                    textViewTanggalReq.setHintTextColor(Color.parseColor("#FF0000"));
                    temp = false;
                }

                if (temp) {

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("Id_Kelas", sKelas));
                    nameValuePairs.add(new BasicNameValuePair("Username", sUsername));
                    nameValuePairs.add(new BasicNameValuePair("Judul", sJudul));
                    nameValuePairs.add(new BasicNameValuePair("Tanggal", sTanggal));
                    nameValuePairs.add(new BasicNameValuePair("Waktu", sMulai));
                    nameValuePairs.add(new BasicNameValuePair("Deskripsi", sDeskripsi));

                    addReq(nameValuePairs);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Pastikan isian valid", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),"Pastikan isian valid",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public RequestJadwal createReq (String id, String idKelas, String tanggal, String materi){
        return new RequestJadwal (id, idKelas, tanggal, materi);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Kelas> getKelas(){
        String url = "http://ppl-a08.cs.ui.ac.id/request.php?fun=kelasforum&username=" + username;

        JSONArray jsonkelas = (new JSONParser()).getJSONArrayFromUrl(url);

        int length = jsonkelas.length();
        ArrayList<Kelas> kelas = new ArrayList<Kelas>();

        for(int i=0; i<length; i++){
            try {
                JSONObject jsonObject = jsonkelas.getJSONObject(i);
                kelas.add((new KelasController()).createKelas(jsonObject.getInt("Id"), jsonObject.getInt("Id_Semester"), jsonObject.getString("Nama")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return kelas;
    }

    public void addReq (List<NameValuePair> nameValuePairs){
        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/request.php?fun=a");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            is = entity.getContent();

            String msg = "Berhasil";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Log.e("Client Protocol", "Log_Tag");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Log_Tag", "IOException");
            e.printStackTrace();
        }
        finish();
    }

    public ArrayList<RequestJadwal> getAllReq(){
        ArrayList<RequestJadwal> request= new ArrayList<RequestJadwal>();
        String url = "http://ppl-a08.cs.ui.ac.id/request.php?fun=listallfaq";
        JSONArray jsonArray = (new JSONParser()).getJSONArrayFromUrl(url);

        if(jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    request.add(createReq(Integer.toString(jsonObject.getInt("Id")),
                            Integer.toString(jsonObject.getInt("Id_Kelas")), jsonObject.getString("Tanggal"),
                            jsonObject.getString("Materi")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }return request;
    }

    public void finish (View view){
        finish();
    }

}
