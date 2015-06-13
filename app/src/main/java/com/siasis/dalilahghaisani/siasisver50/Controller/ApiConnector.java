package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class ApiConnector {

    public Boolean uploadImageToserver(List<NameValuePair> params) {

        String url = "http://ppl-a08.cs.ui.ac.id/uploadImage.php";

        HttpEntity httpEntity = null;

        try
        {

            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);

            httpEntity = httpResponse.getEntity();
            String entityResponse = EntityUtils.toString(httpEntity);

            Log.e("Entity Response  : ", entityResponse);
            return true;

        } catch (ClientProtocolException e) {

            // Signals error in http protocol
            e.printStackTrace();

            //Log Errors Her


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



}
