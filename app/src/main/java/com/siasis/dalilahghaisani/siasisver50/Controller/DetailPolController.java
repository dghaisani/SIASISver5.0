package com.example.android.navigationdrawerexample.Controller;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.navigationdrawerexample.R;

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
import java.util.ArrayList;
import java.util.List;

public class DetailPolController extends Activity {

    String username;
    private ListView listReplyPol;
    JSONArray jsonArray;

    ListReplyReqAdapter adapt;
    RadioGroup groupPilihan;
    EditText textReply;
    int threadID, kelasID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_isi_forum_polling);

        TextView judul = (TextView) this.findViewById(R.id.nama_threadpol);
        textReply = (EditText) this.findViewById(R.id.text_replyPol);
        groupPilihan = (RadioGroup) this.findViewById(R.id.pilihanGroup);

        listReplyPol= (ListView) this.findViewById(R.id.reply_pol);
        this.username = getIntent().getStringExtra("Username");
        new GetAllReplyReq().execute(username);
        new GetAllPilihan().execute(username);

        threadID = getIntent().getIntExtra("RequestID", -1);
        kelasID = getIntent().getIntExtra("KelasID", -1);

        PollingDetail pollingDetail= new PollingDetail(judul, threadID, kelasID);

        //this.username = getIntent().getStringExtra("Username");

        if (threadID > 0 && kelasID > 0)
        {
            // we have customer ID passed correctly.
            getDetailRequest(pollingDetail);
        }
    }

    public void getDetailRequest(PollingDetail pollingDetail){
        new GetPollingDetails().execute(pollingDetail);
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
        listReplyPol = (ListView) this.findViewById(R.id.reply_pol);
        this.jsonArray = jsonArray;
        adapt = new ListReplyReqAdapter(jsonArray, this);
        listReplyPol.setAdapter(adapt);
    }

    private class GetAllPilihan extends AsyncTask<String,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(String... params) {

            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            String url = "http://ppl-a08.cs.ui.ac.id/polling.php?fun=listpilihan&Id="+threadID+"&Id_Kelas="+kelasID;
            return (jsonParser.getJSONArrayFromUrl(url));
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray != null)
                try {
                    setListAdapterPilihan(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    public  void setListAdapterPilihan(JSONArray jsonArray) throws JSONException {
        this.jsonArray = jsonArray;
        JSONObject jsonObject;
        RadioButton button;
        for (int i=0;i<jsonArray.length();i++){
            jsonObject = this.jsonArray.getJSONObject(i);
            button = new RadioButton(this);
            button.setText(jsonObject.getString("Judul")+", "+ jsonObject.getString("Waktu"));
            groupPilihan.addView(button);
        }
    }

    private class GetPollingDetails extends AsyncTask<PollingDetail,Long,JSONObject>
    {
        private PollingDetail pollingDetail;
        @Override
        protected JSONObject doInBackground(PollingDetail... params) {

            // it is executed on Background thread
            this.pollingDetail = params[0];

            String url = "http://ppl-a08.cs.ui.ac.id/polling.php?fun=pollingDetail&Id="+pollingDetail.getthreadID()+"&Id_Kelas="+pollingDetail.getKelasID();
            return (new JSONParser()).getJSONObjectFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject thread) {
            try {
                pollingDetail.getJudul().setText(thread.getString("Judul"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_req_controller, menu);
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

    class PollingDetail{
        private TextView judul;

        private int threadID;
        private int kelasID;

        PollingDetail(TextView judul, int threadID, int kelasID) {
            this.judul = judul;

            this.threadID = threadID;
            this.kelasID = kelasID;
        }

        public TextView getJudul() {
            return judul;
        }

        public void setJudul(TextView judul) {
            this.judul = judul;
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

    public void ReplyPol (View view){
        String textRep= textReply.getText().toString();
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

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/polling.php?fun=addreply&username=" + username);
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
