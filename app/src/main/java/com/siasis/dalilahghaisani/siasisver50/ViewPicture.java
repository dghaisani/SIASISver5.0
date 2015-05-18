package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.android.navigationdrawerexample.Controller.ProfileController;
import com.example.android.navigationdrawerexample.Model.Mahasiswa;


public class ViewPicture extends Activity {

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

        if(mahasiswa.getPath() != null)
            foto.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(mahasiswa.getPath())));
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

        if(mahasiswa.getPath() != null)
            foto.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(mahasiswa.getPath())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_picture, menu);
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
        Intent intent = new Intent(this, EditPicture.class);
        intent.putExtra("Username", username);

        startActivity(intent);
    }
}
