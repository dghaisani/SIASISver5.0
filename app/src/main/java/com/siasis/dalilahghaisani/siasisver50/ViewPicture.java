package com.siasis.dalilahghaisani.siasisver50;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.Controller.ApiConnector;
import com.siasis.dalilahghaisani.siasisver50.Controller.DownloadImageTask;
import com.siasis.dalilahghaisani.siasisver50.Controller.JSONParser;
import com.siasis.dalilahghaisani.siasisver50.Controller.ProfileController;
import com.siasis.dalilahghaisani.siasisver50.Model.Mahasiswa;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ViewPicture extends Activity {

    private static int RESULT_LOAD_IMAGE = 1;
    private static final String baseUrlForImage = "http://ppl-a08.cs.ui.ac.id/";
    private ImageView foto;
    Mahasiswa mahasiswa;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_picture);

        this.foto = (ImageView) findViewById(R.id.imageView);
        this.username = getIntent().getStringExtra("Username");

        ProfileController profileController = new ProfileController(username);

        mahasiswa = profileController.getMahasiswa(username);

        if (!mahasiswa.getPath().equals("a")){
            new GetCustomerDetails().execute(new ApiConnector());
        }else{
            ((ImageView) findViewById(R.id.imageView)).setImageDrawable(getResources().getDrawable(R.drawable.ava120));
        }

        /*if(mahasiswa.getPath() != null)
            foto.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(mahasiswa.getPath())));*/
    }

    @Override
    public void onResume(){
        super.onResume();
        /*SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profile_pref),
                MODE_PRIVATE);

        String defaultS = "";
        String path = sharedPreferences.getString(getString(R.string.path_foto), defaultS);*/

        ProfileController profileController = new ProfileController(username);

        mahasiswa = profileController.getMahasiswa(username);

        if (!mahasiswa.getPath().equals("a")){
            new GetCustomerDetails().execute(new ApiConnector());
        }else{
            ((ImageView) findViewById(R.id.imageView)).setImageDrawable(getResources().getDrawable(R.drawable.ava120));
        }

        /*if(mahasiswa.getPath() != null)
            foto.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(mahasiswa.getPath())));*/
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

    public void toEditPicture(View v){
        /*Intent intent = new Intent(this, EditPicture.class);
        intent.putExtra("Username", username);

        startActivity(intent);*/

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih foto profil")
                .setItems(R.array.edit_picture, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            dialog.dismiss();
                            startActivityForResult(i, 1);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Foto ter-reset",Toast.LENGTH_LONG).show();

                            new ResetFoto().execute(username);
                        }
                    }
                });
        AlertDialog editPicture = builder.create();
        editPicture.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex((filePathColumn[0]));
            String picturePath = cursor.getString(columnIndex);
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

            String imageData = encodeTobase64(bitmap);

            final List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Image", imageData));
            params.add(new BasicNameValuePair("CustomerID", username));

            new AsyncTask<ApiConnector,Long, Boolean >() {
                @Override
                protected Boolean doInBackground(ApiConnector... apiConnectors) {
                    return apiConnectors[0].uploadImageToserver(params);
                }
            }.execute(new ApiConnector());

            cursor.close();

            /*SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.profile_pref),
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(getString(R.string.path_foto),picturePath);
            editor.commit();*/

            //addPath(picturePath);
            //mahasiswa.setPath(picturePath);
        }
    }

    public static String encodeTobase64(Bitmap image) {
        System.gc();

        if (image == null)return null;

        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();

        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT); // min minSdkVersion 8

        return imageEncoded;
    }

    private class GetCustomerDetails extends AsyncTask<ApiConnector,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {

            // it is executed on Background thread

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            try
            {
                String photo = mahasiswa.getPath();

                String urlForImage = baseUrlForImage + photo;
                new DownloadImageTask(foto).execute(urlForImage);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }


    public void addPath(String path){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("Foto", path));
        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://ppl-a08.cs.ui.ac.id/addPath.php?username=" + username);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            is = entity.getContent();

            String msg = "Foto Updated";
            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

    private class ResetFoto extends AsyncTask<String,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(String... params) {

            // it is executed on Background thread
            JSONParser jsonParser = new JSONParser();
            String url = "http://ppl-a08.cs.ui.ac.id/mahasiswa.php?fun=reset&Username=" + params[0];
            return (jsonParser.getJSONArrayFromUrl(url));
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

        }
    }
}
