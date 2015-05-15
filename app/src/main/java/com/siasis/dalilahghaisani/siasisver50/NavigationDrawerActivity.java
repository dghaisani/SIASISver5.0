package com.siasis.dalilahghaisani.siasisver50;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.siasis.dalilahghaisani.siasisver50.R;
import com.siasis.dalilahghaisani.siasisver50.FragmentDummy;
import com.siasis.dalilahghaisani.siasisver50.FragmentInstruction;
import com.siasis.dalilahghaisani.siasisver50.MaterialNavigationDrawer;
import com.siasis.dalilahghaisani.siasisver50.menu.MaterialMenu;
import com.siasis.dalilahghaisani.siasisver50.menu.item.MaterialSection;
import com.siasis.dalilahghaisani.siasisver50.listener.MaterialSectionOnClickListener;

public class NavigationDrawerActivity extends MaterialNavigationDrawer {
    MaterialNavigationDrawer drawer = null;

    @Override
    public int headerType() {
        // set type. you get the available constant from MaterialNavigationDrawer class
        return MaterialNavigationDrawer.DRAWERHEADER_IMAGE;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        Bundle bundle = new Bundle();
        bundle.putString("instruction", "This example has an image header in the drawer. " +
                "See the method headerType in the source code. And don't forget to call " +
                "setCustomMenu(), to set your menu and setDrawerHeaderImage(), to set your image.");

        drawer = this;

        MaterialMenu menu = new MaterialMenu();

        //create instruction fragment
        Fragment fragmentInstruction = new FragmentInstruction();
        fragmentInstruction.setArguments(bundle);

        // menu items
        MaterialSection jadwal = this.newSection("Jadwal Asistensi",this.getResources().getDrawable(R.drawable.jadwal), new FragmentInstruction(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        jadwal.setFragmentTitle("Jadwal Asistensi");
        MaterialSection profil = this.newSection("Profil",this.getResources().getDrawable(R.drawable.person), new FragmentInstruction(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        profil.setFragmentTitle("Profil");
        MaterialSection kelas = this.newSection("Kelas", this.getResources().getDrawable(R.drawable.kelas), new FragmentDummy(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        kelas.setFragmentTitle("Kelas");
        // add devisor
        this.newDevisor(menu);
        // section with own in click listener
        this.newLabel("Forum", false, menu);

        MaterialSection forum1 = this.newSection("Tanya Jawab", this.getResources().getDrawable(R.drawable.qa), new FragmentDummy(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        forum1.setNotifications(20);
        forum1.setFragmentTitle("Tanya Jawab");
        MaterialSection forum2 = this.newSection("Request Jadwal Asistensi", this.getResources().getDrawable(R.drawable.request), new FragmentDummy(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        forum2.setNotifications(20);
        forum2.setFragmentTitle("Request Jadwal Asistensi");
        MaterialSection forum3 = this.newSection("Polling Jadwal Asistensi", this.getResources().getDrawable(R.drawable.polling), new FragmentDummy(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        forum3.setNotifications(20);
        forum3.setFragmentTitle("Polling Jadwal Asistensi");

        // add devisor
        this.newDevisor(menu);
        // section with own in click listener
        this.newLabel("Admin", false, menu);

        MaterialSection admin1 = this.newSection("Mengelola Kelas", this.getResources().getDrawable(R.drawable.mengelola_kelas), new FragmentDummy(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        admin1.setNotifications(20);
        admin1.setFragmentTitle("Mengelola Kelas");
        MaterialSection admin2 = this.newSection("Approve Deny Request Role", this.getResources().getDrawable(R.drawable.appdeny), new FragmentDummy(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        admin2.setNotifications(20);
        admin2.setFragmentTitle("Approve Deny Request Role");
        MaterialSection admin3 = this.newSection("Mengelola Forum", this.getResources().getDrawable(R.drawable.mengelola_forum), new FragmentDummy(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        admin3.setNotifications(20);
        admin3.setFragmentTitle("Mengelola Forum");
        MaterialSection admin4 = this.newSection("Reset Database", this.getResources().getDrawable(R.drawable.database), new FragmentDummy(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        admin4.setNotifications(20);
        admin4.setFragmentTitle("Reset database");

        // add devisor
        this.newDevisor(menu);
        this.newSection("Logout", this.getResources().getDrawable(R.drawable.logout), new FragmentDummy(), true, menu).setSectionColor(Color.parseColor("#000000"));

        // set start index
        menu.setStartIndex(0);

        // set this menu
        this.setCustomMenu(menu);

        // set image for recycleview_header
        this.setDrawerHeaderImage(getResources().getDrawable(R.drawable.head2));
    }
}