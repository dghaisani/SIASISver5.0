package com.example.android.navigationdrawerexample.Controller;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by lenovo on 3/31/2015.
 */
public class JSONParser {

    public JSONArray getJSONArrayFromUrl(String url){
        DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient
        //asumsikan dapet list kelasnya
        HttpGet httpGet = new HttpGet(url);

        HttpEntity httpEntity = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = null;

        if (httpEntity != null) {
            try {
                String entityResponse = EntityUtils.toString(httpEntity);

                Log.e("Entity Response  : ", entityResponse);

                jsonArray = new JSONArray(entityResponse);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;
    }

    public JSONObject getJSONObjectFromUrl(String url){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        HttpEntity httpEntity = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonArray = null;

        if (httpEntity != null) {
            try {
                String entityResponse = EntityUtils.toString(httpEntity);

                Log.e("Entity Response  : ", entityResponse);

                jsonArray = new JSONObject(entityResponse);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }
}
