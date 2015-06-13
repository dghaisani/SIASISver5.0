package com.siasis.dalilahghaisani.siasisver50.Controller.ForumQA;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.ListReplyAdapter;
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
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DetailQAController extends Activity {

    String username;
    private ListView listReplyReq;
    JSONArray jsonArray;

    ListReplyAdapter adapt;
    EditText textReply;
    int threadID, kelasID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_qa);

        TextView judul = (TextView) this.findViewById(R.id.qa_jadwal_asis_2_keterangan);
        TextView kelas = (TextView) this.findViewById(R.id.qa_jadwal_asis_2_namamatkul);
        TextView deskripsi = (TextView) this.findViewById(R.id.qa_jadwal_asis_2_desc);
        textReply = (EditText) this.findViewById(R.id.text_reply);
        ImageView back = (ImageView) this.findViewById(R.id.back_detailqa);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listReplyReq = (ListView) this.findViewById(R.id.reply_req);
        this.username = getIntent().getStringExtra("Username");
        new GetAllReplyReq().execute(username);

        threadID = getIntent().getIntExtra("RequestID", -1);
        kelasID = getIntent().getIntExtra("KelasID", -1);

        QuestionDetail questionDetail= new QuestionDetail(kelas, judul, deskripsi, threadID, kelasID);

        //this.username = getIntent().getStringExtra("Username");

        if (threadID > 0 && kelasID > 0)
        {
            // we have customer ID passed correctly.
            getDetailRequest(questionDetail);

        }
    }

    public void getDetailRequest(QuestionDetail questionDetail){
        new GetQuestionDetails().execute(questionDetail);
    }

    private class GetAllReplyReq extends AsyncTask<String,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(String... params) {

            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            String url = "http://ppl-a08.cs.ui.ac.id/question.php?fun=listreply&Id="+threadID+"&Id_Kelas="+kelasID;
            return (jsonParser.getJSONArrayFromUrl(url));
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray != null)
                setListAdapter(jsonArray);
        }
    }

    public  void setListAdapter(JSONArray jsonArray) {
        listReplyReq = (ListView) this.findViewById(R.id.reply_req);
        this.jsonArray = jsonArray;
        adapt = new ListReplyAdapter(jsonArray, this);
        listReplyReq.setAdapter(adapt);
    }

    private class GetQuestionDetails extends AsyncTask<QuestionDetail,Long,JSONObject>
    {
        private QuestionDetail questionDetail;
        @Override
        protected JSONObject doInBackground(QuestionDetail... params) {

            // it is executed on Background thread
            this.questionDetail = params[0];

            String url = "http://ppl-a08.cs.ui.ac.id/question.php?fun=questionDetail&Id="+questionDetail.getthreadID()+"&Id_Kelas="+questionDetail.getKelasID();
            return (new JSONParser()).getJSONObjectFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject thread) {
            try {
                questionDetail.getJudul().setText(thread.getString("Judul"));
                questionDetail.getKelas().setText(thread.getString("Nama"));
                questionDetail.getDeskripsi().setText(thread.getString("Deskripsi"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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

    class QuestionDetail{
        private TextView kelas;
        private TextView judul;
        private TextView deskripsi;

        private int threadID;
        private int kelasID;

        QuestionDetail(TextView kelas, TextView judul, TextView deskripsi, int threadID, int kelasID) {
            this.judul = judul;
            this.kelas = kelas;
            this.deskripsi = deskripsi;
            this.threadID = threadID;
            this.kelasID = kelasID;
        }

        public TextView getJudul() {
            return judul;
        }

        public void setJudul(TextView judul) {
            this.judul = judul;
        }

        public TextView getKelas() {
            return kelas;
        }

        public void setKelas(TextView kelas) { this.kelas = kelas; }

        public TextView getDeskripsi() {
            return deskripsi;
        }

        public void setDeskripsi(TextView deskripsi) {
            this.deskripsi = deskripsi;
        }

        public int getthreadID() {
            return threadID;
        }

        public void setthreadID(int threadID) {
            this.threadID = threadID;
        }

        public int getKelasID() {
            return kelasID;
        }

        public void setKelasID(int kelasID) {
            this.kelasID = kelasID;
        }
    }

    public void ReplyQA (View view){
        String textRep=textReply.getText().toString();
        String idThread = Integer.toString(this.threadID);
        String idKelas = Integer.toString(this.kelasID);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("Isi", textRep));
        nameValuePairs.add(new BasicNameValuePair("Id", idThread));
        nameValuePairs.add(new BasicNameValuePair("Id_Kelas", idKelas));

        addReplyReq(nameValuePairs);
        new GetAllReplyReq().execute(username);
    }

    public void addReplyReq (List<NameValuePair> nameValuePairs){
        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/question.php?fun=addreply&username=" + username);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            is = entity.getContent();

            String msg = "Reply added";
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
    }
}
