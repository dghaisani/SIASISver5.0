package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.navigationdrawerexample.Controller.JSONParser;
import com.example.android.navigationdrawerexample.Controller.ListReqAdapter;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class RequestRole extends Activity {

    private JSONArray jsonArray;
    private ListView listView;
    ListReqAdapter adapt;
    String username = "rian.fitriansyah";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile_requestrole);

        new GetAllRole().execute(username);
        listView = (ListView) findViewById(R.id.listViewReq);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_role, menu);
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

    private class GetAllRole extends AsyncTask<String,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(String... params) {

            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            String url = "http://ppl-a08.cs.ui.ac.id/role.php?fun=a&username=" + params[0];
            return (jsonParser.getJSONArrayFromUrl(url));
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray != null)
                setListAdapter(jsonArray);
        }
    }

    public  void setListAdapter(JSONArray jsonArray) {
        ListView listReq = (ListView) findViewById(R.id.listViewReq);
        this.jsonArray = jsonArray;
        adapt = new ListReqAdapter(jsonArray, this);
        listReq.setAdapter(adapt);
    }

    public void onClick(View v){
        InputStream is = null;
        ArrayList<String> listString = adapt.getSelectedStrings();
        for (int i = 0; i< listString.size();i++){
            String kelas = listString.get(i);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("Nama", kelas));

            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost
                        ("http://ppl-a08.cs.ui.ac.id/role.php?fun=addrole&username="+username);
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
        }

    }
}
