package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.Model.Jadwal;
import com.siasis.dalilahghaisani.siasisver50.Model.Kelas;
import com.siasis.dalilahghaisani.siasisver50.Model.Mahasiswa;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by lenovo on 3/31/2015.
 */
public class JadwalController extends Activity {
    private String username;
    private ListView GetAllHadirListView;
    private String op;
    private JSONArray mahasiswa;
    private LinearLayout kehadiran;


    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> detailMahasiswa = session.getUserDetails();
        this.username = detailMahasiswa.get("username");
        this.op = getIntent().getStringExtra("View");


        if (op.equals("detailJadwal")) {
            setContentView(R.layout.detail_jadwal);

            Log.e("masuk","na");

            TextView judul = (TextView) this.findViewById(R.id.judul);
            TextView asisten = (TextView) this.findViewById(R.id.asisten);
            TextView hp = (TextView) this.findViewById(R.id.hp);
            TextView tanggal = (TextView) this.findViewById(R.id.tanggal);
            TextView waktu = (TextView) this.findViewById(R.id.waktu);
            TextView ruangan = (TextView) this.findViewById(R.id.ruangan);
            TextView deskripsi = (TextView) this.findViewById(R.id.deskripsi);
            final TextView jumlah = (TextView) this.findViewById(R.id.textView11);

            Button hadir = (Button) this.findViewById(R.id.button5);
            Button edit = (Button) this.findViewById(R.id.button12);
            Button partisipan = (Button) this.findViewById(R.id.button6);

            ImageView picture = (ImageView) this.findViewById(R.id.pic);

            final int jadwalID = getIntent().getIntExtra("JadwalID", -1);
            final int kelasID = getIntent().getIntExtra("KelasID", -1);

            JadwalDetail jadwalDetail = new JadwalDetail(judul, asisten, hp, tanggal, waktu, ruangan, deskripsi, jadwalID, kelasID, picture, edit);

            try {
                jumlah.setText(Integer.toString(getJumlahMenghadiri(jadwalID, kelasID)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jadwalID > 0 && kelasID > 0) {
                // we have customer ID passed correctly.
                getDetailJadwal(jadwalDetail);
            }

            hadir.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //bernilai true jika ia blm menghadiri
                    if (cekMenghadiri(jadwalID, kelasID)) {
                        addMenghadiri(jadwalID, kelasID);
                        try {
                            jumlah.setText(Integer.toString(getJumlahMenghadiri(jadwalID, kelasID)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } //belum menghadiri
                }
            });
            partisipan.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent showDetails = new Intent(getApplicationContext(), JadwalController.class);
                    showDetails.putExtra("View", "partisipasi");
                    showDetails.putExtra("JadwalID", jadwalID);
                    startActivity(showDetails);
                }
            });
        } else if (op.equals("createJadwal")) {
            setContentView(R.layout.form_jadwal);

            //username = (EditText) findViewById(R.id.editText8);
            ImageView ok = (ImageView) findViewById(R.id.button2);
            ImageView bTanggal = (ImageView) findViewById(R.id.button);
            ImageView bMulai = (ImageView) findViewById(R.id.button3);
            ImageView bSelesai = (ImageView) findViewById(R.id.button4);
            final Calendar calendar = Calendar.getInstance();
            final Calendar calendarMulai = Calendar.getInstance();
            final Calendar calendarSelesai = Calendar.getInstance();
            final RadioGroup id_kelas = (RadioGroup)findViewById(R.id.kelas);
            final EditText judul = (EditText) findViewById(R.id.editText);
            final TextView tanggal = (TextView) findViewById(R.id.textView8);
            final TextView mulai = (TextView) findViewById(R.id.textView9);
            final TextView selesai = (TextView) findViewById(R.id.textView10);
            final EditText ruangan = (EditText) findViewById(R.id.editText5);
            final EditText deskripsi = (EditText) findViewById(R.id.editText6);

            final ArrayList<Kelas> arrayKelas = (new MenjabatController(username)).getMenjabatKelas();

            if (getIntent().getIntExtra("Update", 0) == 1) {
                int namaKelas = getIntent().getIntExtra("KelasID", -1);
                for (int i = 0; i < arrayKelas.size(); i++) {
                    RadioButton rbKelas = new RadioButton(getApplicationContext());
                    rbKelas.setId(arrayKelas.get(i).getId());
                    rbKelas.setText(arrayKelas.get(i).getNama());
                    rbKelas.setTextColor(getResources().getColor(R.color.black));
                    if (namaKelas == arrayKelas.get(i).getId()) {
                        rbKelas.setChecked(true);
                    }
                    id_kelas.addView(rbKelas);
                }
                judul.setText(getIntent().getStringExtra("judul"));
                tanggal.setText(getIntent().getStringExtra("tanggal"));
                mulai.setText(getIntent().getStringExtra("mulai"));
                selesai.setText(getIntent().getStringExtra("selesai"));
                ruangan.setText(getIntent().getStringExtra("ruangan"));
                deskripsi.setText(getIntent().getStringExtra("deskripsi"));
            } else {
                RadioButton rbKelas1 = new RadioButton(getApplicationContext());
                rbKelas1.setId(arrayKelas.get(0).getId());
                rbKelas1.setText(arrayKelas.get(0).getNama());
                rbKelas1.setTextColor(getResources().getColor(R.color.black));
                rbKelas1.setChecked(true);
                id_kelas.addView(rbKelas1);
                for (int i = 1; i < arrayKelas.size(); i++) {
                    RadioButton rbKelas = new RadioButton(getApplicationContext());
                    rbKelas.setId(arrayKelas.get(i).getId());
                    rbKelas.setText(arrayKelas.get(i).getNama());
                    rbKelas.setTextColor(getResources().getColor(R.color.black));
                    id_kelas.addView(rbKelas);
                }
            }

            bTanggal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            String t = dayOfMonth + "";
                            String m = monthOfYear + 1 + "";
                            if (dayOfMonth < 10)
                                t = "0" + t;
                            if (monthOfYear + 1 < 10)
                                m = "0" + m;

                            tanggal.setText(t + "-" + m + "-" + year);
                        }
                    };
                    DatePickerDialog dialog = new DatePickerDialog(JadwalController.this, d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setSpinnersShown(false);
                    dialog.getDatePicker().setCalendarViewShown(true);
                    dialog.show();
                }
            });

            bMulai.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    TimePickerDialog.OnTimeSetListener d = new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            view.setIs24HourView(true);
                            calendarMulai.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendarMulai.set(Calendar.MINUTE, minute);

                            String sMinute;

                            if (("" + minute).length() == 1) {
                                sMinute = "0" + minute;
                            } else {
                                sMinute = "" + minute;
                            }

                            String sHourOfDay;

                            if (("" + hourOfDay).length() == 1) {
                                sHourOfDay = "0" + hourOfDay;
                            } else {
                                sHourOfDay = "" + hourOfDay;
                            }

                            mulai.setText(sHourOfDay + ":" + sMinute);
                        }
                    };
                    new TimePickerDialog(JadwalController.this, d, calendarMulai.get(Calendar.HOUR_OF_DAY), calendarMulai.get(Calendar.MINUTE), true).show();
                }
            });

            bSelesai.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    TimePickerDialog.OnTimeSetListener d = new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            view.setIs24HourView(true);
                            calendarSelesai.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendarSelesai.set(Calendar.MINUTE, minute);

                            String sMinute;

                            if (("" + minute).length() == 1) {
                                sMinute = "0" + minute;
                            } else {
                                sMinute = "" + minute;
                            }

                            String sHourOfDay;

                            if (("" + hourOfDay).length() == 1) {
                                sHourOfDay = "0" + hourOfDay;
                            } else {
                                sHourOfDay = "" + hourOfDay;
                            }

                            selesai.setText(sHourOfDay + ":" + sMinute);
                        }
                    };
                    new TimePickerDialog(JadwalController.this, d, calendarSelesai.get(Calendar.HOUR_OF_DAY), calendarSelesai.get(Calendar.MINUTE), true).show();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String sKelas = id_kelas.getCheckedRadioButtonId() + "";
                    String sJudul = judul.getText().toString();

                    String sRuangan = ruangan.getText().toString();
                    String sDeskripsi = deskripsi.getText().toString();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    String currentTime = dateFormat.format(new Date());
                    StringTokenizer st = new StringTokenizer(currentTime, " ");
                    String day = st.nextToken();
                    String time = st.nextToken();

                    boolean temp = true;
                    if (sJudul.equals("") || sJudul.trim().length() <= 0) {
                        //judul.setBackgroundColor(15918822);
                        judul.setHintTextColor(Color.parseColor("#FF0000"));
                        temp = false;
                    } else if (sJudul.length() > 25) {
                        judul.setText(null);
                        judul.setHintTextColor(Color.parseColor("#FF0000"));
                        judul.setHint("Asistensi UTS, max 25 karakter");
                    }
                    if (tanggal.getText().toString().equals("") || tanggal.getText().toString().trim().length() <= 0) {
                        //bTanggal.setBackgroundColor(15918822);
                        tanggal.setHintTextColor(Color.parseColor("#FF0000"));
                        temp = false;
                    }
                    if (mulai.getText().toString().equals("") || mulai.getText().toString().trim().length() <= 0) {
                        //bMulai.setBackgroundColor(15918822);
                        mulai.setHintTextColor(Color.parseColor("#FF0000"));
                        temp = false;
                    }
                    if (selesai.getText().toString().equals("") || selesai.getText().toString().trim().length() <= 0) {
                        //bSelesai.setBackgroundColor(15918822);
                        selesai.setHintTextColor(Color.parseColor("#FF0000"));
                        temp = false;
                    }
                    if (sRuangan.equals("") || sRuangan.trim().length() <= 0) {
                        //ruangan.setBackgroundColor(15918822);
                        ruangan.setHintTextColor(Color.parseColor("#FF0000"));
                        temp = false;
                    } else if (sRuangan.length() > 10) {
                        ruangan.setText(null);
                        ruangan.setHintTextColor(Color.parseColor("#FF0000"));
                        ruangan.setHint("contoh: 2403, max 10 karakter");
                    }
                    if (sDeskripsi.equals("") || sDeskripsi.trim().length() <= 0) {
                        deskripsi.setHintTextColor(Color.parseColor("#FF0000"));
                        temp = false;
                    } else if (sDeskripsi.length() > 255) {
                        deskripsi.setText(null);
                        deskripsi.setHintTextColor(Color.parseColor("#FF0000"));
                        deskripsi.setHint("contoh: 2403, max 10 karakter");
                    }
                    if (tanggal.getText().toString().equals("Tanggal sudah berlalu")) {
                        tanggal.setTextColor(Color.parseColor("#FF0000"));
                    } else if (!tanggal.getText().toString().equals("") && tanggal.getText().toString().compareTo(day) < 0) {
                        //bTanggal.setBackgroundColor(15918822);
                        tanggal.setText("Tanggal sudah berlalu");
                        tanggal.setTextColor(Color.parseColor("#FF0000"));
                        temp = false;
                    } else if (!tanggal.getText().toString().equals("") && tanggal.getText().toString().compareToIgnoreCase(day) == 0) {
                        tanggal.setTextColor(Color.parseColor("#000000"));
                        if (mulai.getText().toString().equals("Waktu sudah berlalu")) {
                            mulai.setTextColor(Color.parseColor("#FF0000"));
                        } else if (!mulai.getText().toString().equals("")) {
                            if (mulai.getText().toString().compareToIgnoreCase(time) <= 0) {
                                //bMulai.setBackgroundColor(15918822);
                                mulai.setText("Waktu sudah berlalu");
                                mulai.setTextColor(Color.parseColor("#FF0000"));
                                temp = false;
                            } else {
                                mulai.setTextColor(Color.parseColor("#000000"));
                            }
                        }
                    } else {
                        tanggal.setTextColor(Color.parseColor("#000000"));
                        mulai.setTextColor(Color.parseColor("#000000"));
                    }
                    if (selesai.getText().toString().equals("Belum dimulai")) {
                        selesai.setTextColor(Color.parseColor("#FF0000"));
                    } else if (!selesai.getText().toString().equals("") && selesai.getText().toString().compareToIgnoreCase(mulai.getText().toString()) <= 0) {
                        //bSelesai.setBackgroundColor(15918822);
                        selesai.setText("Belum dimulai");
                        selesai.setTextColor(Color.parseColor("#FF0000"));
                        temp = false;
                    } else {
                        selesai.setTextColor(Color.parseColor("#000000"));
                    }

                    if (temp) {
                        String[] tTanggal = tanggal.getText().toString().split("-");
                        String sTanggal = tTanggal[2] + "-" + tTanggal[1] + "-" + tTanggal[0];
                        //Toast.makeText(getApplicationContext(),tanggal.getText().toString(),Toast.LENGTH_LONG).show();
                        String sMulai = mulai.getText().toString() + ":00";
                        String sSelesai = selesai.getText().toString() + ":00";

                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("Id_Kelas", sKelas));
                        nameValuePairs.add(new BasicNameValuePair("Username", username));
                        nameValuePairs.add(new BasicNameValuePair("Judul", sJudul));
                        nameValuePairs.add(new BasicNameValuePair("Tanggal", sTanggal));
                        nameValuePairs.add(new BasicNameValuePair("W_Mulai", sMulai));
                        nameValuePairs.add(new BasicNameValuePair("W_Akhir", sSelesai));
                        nameValuePairs.add(new BasicNameValuePair("Ruangan", sRuangan));
                        nameValuePairs.add(new BasicNameValuePair("Deskripsi", sDeskripsi));

                        if (getIntent().getIntExtra("Update", 0) == 1) {
                            nameValuePairs.add(new BasicNameValuePair("Id", getIntent().getIntExtra("JadwalID", -1) + ""));
                            updateJadwal(nameValuePairs);
                            Intent showDetails = new Intent(getApplicationContext(), JadwalController.class);
                            showDetails.putExtra("JadwalID", getIntent().getIntExtra("JadwalID", -1));
                            showDetails.putExtra("KelasID", getIntent().getIntExtra("KelasID", -1));
                            showDetails.putExtra("Username", username);
                            showDetails.putExtra("View", "detailJadwal");
                            startActivity(showDetails);
                            finish();
                        } else {
                            addJadwal(nameValuePairs);
                            Intent showDetails = new Intent(getApplicationContext(), PilihanController.class);
                            showDetails.putExtra("Username", username);
                            startActivity(showDetails);
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Pastikan isian valid", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"Pastikan isian valid",Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (op.equals("partisipasi")) {
            setContentView(R.layout.list_hadir);

            Log.e("ko masuk","haha");
            GetAllHadirListView = (ListView) findViewById(R.id.GetAllHadirListView);
            kehadiran = (LinearLayout) findViewById(R.id.container);

            //Toast.makeText(getApplicationContext(),getIntent().getIntExtra("JadwalID", -1) + "",Toast.LENGTH_LONG).show();

            new GetHadir(JadwalController.this).execute(getIntent().getIntExtra("JadwalID", -1));

            GetAllHadirListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        //Toast.makeText(getApplicationContext(), "masuk ke klik",Toast.LENGTH_LONG).show();
                        JSONObject mahasiswaClicked = mahasiswa.getJSONObject(position);
                        String userMahasiswa = mahasiswaClicked.getString("Username");
                        Intent showDetails = new Intent(getApplicationContext(), MenjabatController.class);
                        showDetails.putExtra("Username", userMahasiswa);
                        startActivity(showDetails);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public int getJumlahMenghadiri(int id_jadwal, int id_kelas) throws JSONException {
        String url = "http://ppl-a08.cs.ui.ac.id/hadir.php?fun=jadwalJumlah&Id="+id_jadwal+"&Id_Kelas="+id_kelas;
        //new GetHadir(JadwalController.this).execute(getIntent().getIntExtra("JadwalID", -1));

        return (new JSONParser()).getJSONObjectFromUrl(url).getInt("Count");
    }


    public void addMenghadiri(int id_jadwal, int id_kelas){
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("Id_Kelas", Integer.toString(id_kelas)));
            nameValuePairs.add(new BasicNameValuePair("Username", username));
            nameValuePairs.add(new BasicNameValuePair("Id_Jadwal", Integer.toString(id_jadwal)));
            GetAllHadirListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        //Toast.makeText(getApplicationContext(), "masuk ke klik",Toast.LENGTH_LONG).show();
                        JSONObject mahasiswaClicked = mahasiswa.getJSONObject(position);
                        String userMahasiswa = mahasiswaClicked.getString("Username");
                        Intent showDetails = new Intent(getApplicationContext(), MenjabatController.class);
                        showDetails.putExtra("Username", userMahasiswa);
                        startActivity(showDetails);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    public boolean cekMenghadiri(int id_jadwal, int id_kelas) {
        String url = "http://ppl-a08.cs.ui.ac.id/jadwal.php?fun=cekHadir&Id=" + id_jadwal + "&Id_Kelas=" + id_kelas + "&Username=" + username;
        return (new JSONParser()).getJSONObjectFromUrl(url) == null;
    }

    public void addMenghadiri(int id_jadwal){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("Username", username));
        nameValuePairs.add(new BasicNameValuePair("Id_Jadwal", Integer.toString(id_jadwal)));

        InputStream is = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/createMenghadiri.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            is = entity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Log.e("Client Protocol", "Log_Tag");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Log_Tag", "IOException");
            e.printStackTrace();
        }
    }

    public void getDetailJadwal(JadwalDetail jadwalDetail){
        new GetJadwalDetails(JadwalController.this).execute(jadwalDetail);
    }

    public void addJadwal (List<NameValuePair> nameValuePairs){
        toDatabase(nameValuePairs, "http://ppl-a08.cs.ui.ac.id/createJadwal.php");
    }

    public void updateJadwal (List<NameValuePair> nameValuePairs){
        toDatabase(nameValuePairs, "http://ppl-a08.cs.ui.ac.id/updateJadwal.php");
    }

    public void toDatabase (List<NameValuePair> nameValuePairs, String url){
        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            is = entity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Log.e("Client Protocol", "Log_Tag");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Log_Tag", "IOException");
            e.printStackTrace();
        }
    }

    private class GetJadwalDetails extends AsyncTask<JadwalDetail,Long,JSONObject> {
        private JadwalDetail jadwalDetail;
        private ProgressDialog dialog;
        private JadwalController activity;

        public GetJadwalDetails(JadwalController activity) {
            this.activity = activity;
            dialog = new ProgressDialog(this.activity);
        }

        @Override
        protected JSONObject doInBackground(JadwalDetail... params) {
            this.jadwalDetail = params[0];
            String url = "http://ppl-a08.cs.ui.ac.id/jadwal.php?fun=jadwalDetail&Id=" + jadwalDetail.getJadwalID() + "&Id_Kelas=" + jadwalDetail.getKelasID();
            return (new JSONParser()).getJSONObjectFromUrl(url);
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Sedang mengambil data...");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(final JSONObject jadwal) {
            try {
                final Jadwal kJadwal = new Jadwal(jadwal.getInt("Id"), jadwal.getString("Judul"), jadwal.getString("Tanggal"), jadwal.getString("W_Mulai"),
                        jadwal.getString("W_Akhir"), jadwal.getString("Ruangan"), jadwal.getString("Deskripsi"), jadwal.getInt("Id_kelas"),
                        jadwal.getString("Username"));
                String asdos = kJadwal.getUsername();
                final String judul = kJadwal.getJudul();
                String[] atgl = kJadwal.getTanggal().split("-");
                final String tanggal = atgl[2] + "-" + atgl[1] + "-" + atgl[0];
                final String w_mulai = kJadwal.getWaktuMulai().substring(0, kJadwal.getWaktuMulai().length() - 3);
                final String w_akhir = kJadwal.getWaktuAkhir().substring(0, kJadwal.getWaktuAkhir().length() - 3);
                final String ruangan = kJadwal.getRuangan();
                final String deskripsi = kJadwal.getDeskripsi();

                jadwalDetail.getJudul().setText(judul);
                jadwalDetail.getAsisten().setText(asdos);

                if (!username.equals(asdos))
                    jadwalDetail.getEdit().setVisibility(View.GONE);
                else
                    jadwalDetail.getEdit().setVisibility(View.VISIBLE);

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                jadwalDetail.getEdit().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent showDetails = new Intent(getApplicationContext(), JadwalController.class);
                        showDetails.putExtra("JadwalID", kJadwal.getId());
                        showDetails.putExtra("KelasID", kJadwal.getIdKelas());
                        showDetails.putExtra("Username", username);
                        showDetails.putExtra("View", "createJadwal");
                        showDetails.putExtra("Update", 1);
                        showDetails.putExtra("judul", judul);
                        showDetails.putExtra("tanggal", tanggal);
                        showDetails.putExtra("mulai", w_mulai);
                        showDetails.putExtra("selesai", w_akhir);
                        showDetails.putExtra("ruangan", ruangan);
                        showDetails.putExtra("deskripsi", deskripsi);
                        startActivity(showDetails);
                        finish();
                    }
                });
                Mahasiswa m = (new ProfileController()).getMahasiswa(asdos);
                jadwalDetail.getHp().setText(m.getHp());

                jadwalDetail.getTanggal().setText(tanggal);
                jadwalDetail.getWaktu().setText(w_mulai + " - " + w_akhir);
                jadwalDetail.getRuangan().setText(ruangan);
                jadwalDetail.getDeskripsi().setText(deskripsi);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class GetHadir extends AsyncTask<Integer, Long, JSONArray> {
        private ProgressDialog dialog;
        private JadwalController activity;

        public GetHadir(JadwalController activity) {
            this.activity = activity;
            dialog = new ProgressDialog(this.activity);
        }

        @Override
        protected JSONArray doInBackground(Integer... params) {
            String url = "http://ppl-a08.cs.ui.ac.id/hadir.php?fun=all&Id=" + params[0];
            return (new JSONParser()).getJSONArrayFromUrl(url);
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Sedang mengambil data...");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(JSONArray mahasiswa) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if(mahasiswa != null) {
                setListAdapterHadir(mahasiswa);
            } else {
                TextView none = new TextView(getApplicationContext());
                none.setText("Tidak ada yang menyatakan hadir");
                none.setTextColor(getResources().getColor(R.color.black));
                kehadiran.addView(none);
            }
        }
    }

    public  void setListAdapterHadir(JSONArray mahasiswa) {
        this.mahasiswa = mahasiswa;
        this.GetAllHadirListView.setAdapter(new ListHadirAdapter(mahasiswa, JadwalController.this));
    }

    class JadwalDetail{
        private TextView judul;
        private TextView asisten;
        private TextView hp;
        private TextView tanggal;
        private TextView waktu;
        private TextView ruangan;
        private TextView deskripsi;

        private int jadwalID;
        private int kelasID;

        private ImageView picture;
        private Button edit;

        JadwalDetail(TextView judul, TextView asisten, TextView hp, TextView tanggal, TextView waktu, TextView ruangan, TextView deskripsi,
                     int jadwalID, int kelasID, ImageView picture, Button edit) {
            this.judul = judul;
            this.asisten = asisten;
            this.hp = hp;
            this.tanggal = tanggal;
            this.waktu = waktu;
            this.ruangan = ruangan;
            this.deskripsi = deskripsi;
            this.jadwalID = jadwalID;
            this.kelasID = kelasID;
            this.picture = picture;
            this.edit = edit;
        }

        public TextView getJudul() {
            return judul;
        }

        public TextView getAsisten() {
            return asisten;
        }

        public TextView getHp() {
            return hp;
        }

        public TextView getTanggal() {
            return tanggal;
        }

        public TextView getWaktu() {
            return waktu;
        }

        public TextView getRuangan() {
            return ruangan;
        }

        public TextView getDeskripsi() {
            return deskripsi;
        }

        public int getJadwalID() {
            return jadwalID;
        }

        public int getKelasID() {
            return kelasID;
        }

        public Button getEdit() {
            return this.edit;
        }
    }
}
