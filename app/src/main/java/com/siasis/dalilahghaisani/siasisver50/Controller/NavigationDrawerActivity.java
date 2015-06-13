package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.siasis.dalilahghaisani.siasisver50.Controller.Admin.AdminDatabaseFragment;
import com.siasis.dalilahghaisani.siasisver50.Controller.Admin.AdminKelasFragment;
import com.siasis.dalilahghaisani.siasisver50.Controller.Admin.AdminRoleFragment;
import com.siasis.dalilahghaisani.siasisver50.Controller.Admin.AdminThread;
import com.siasis.dalilahghaisani.siasisver50.Controller.ForumPol.PollingFragment;
import com.siasis.dalilahghaisani.siasisver50.Controller.ForumQA.QAFragment;
import com.siasis.dalilahghaisani.siasisver50.Controller.ForumReq.RequestFragment;
import com.siasis.dalilahghaisani.siasisver50.Controller.Jadwal.JadwalFragment;
import com.siasis.dalilahghaisani.siasisver50.Controller.Kelas.KelasFragment;
import com.siasis.dalilahghaisani.siasisver50.Controller.Profile.ProfileFragment;
import com.siasis.dalilahghaisani.siasisver50.R;
import com.siasis.dalilahghaisani.siasisver50.menu.MaterialMenu;
import com.siasis.dalilahghaisani.siasisver50.menu.item.MaterialSection;

import java.util.HashMap;

public class NavigationDrawerActivity extends MaterialNavigationDrawer {
    MaterialNavigationDrawer drawer = null;
    SessionManager session;
    private int role;
    private HashMap<String, String> detailMahasiswa;

    @Override
    public int headerType() {
        // set type. you get the available constant from MaterialNavigationDrawer class
        return DRAWERHEADER_IMAGE;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        Bundle bundle = new Bundle();
        drawer = this;

        MaterialMenu menu = new MaterialMenu();

        //create instruction fragment
        Fragment fragmentInstruction = new FragmentInstruction();
        fragmentInstruction.setArguments(bundle);

        // menu items
        MaterialSection jadwal = this.newSection("Jadwal Asistensi", this.getResources().getDrawable(R.drawable.jadwal), new JadwalFragment(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        jadwal.setFragmentTitle("Jadwal Asistensi");
        MaterialSection profil = this.newSection("Profil", this.getResources().getDrawable(R.drawable.person), new ProfileFragment(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        profil.setFragmentTitle("Profil");
        MaterialSection kelas = this.newSection("Kelas", this.getResources().getDrawable(R.drawable.kelas), new KelasFragment(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
        kelas.setFragmentTitle("Kelas");

        // add devisor
        this.newDevisor(menu);
        // section with own in click listener
        this.newLabel("Forum", false, menu);

        MaterialSection forum1 = this.newSection("Tanya Jawab", this.getResources().getDrawable(R.drawable.qa), new QAFragment(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));

        forum1.setFragmentTitle("Tanya Jawab");
        MaterialSection forum2 = this.newSection("Request Jadwal Asistensi", this.getResources().getDrawable(R.drawable.request), new RequestFragment(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));

        forum2.setFragmentTitle("Request Jadwal Asistensi");
        MaterialSection forum3 = this.newSection("Polling Jadwal Asistensi", this.getResources().getDrawable(R.drawable.polling), new PollingFragment(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));

        forum3.setFragmentTitle("Polling Jadwal Asistensi");

        session = new SessionManager(NavigationDrawerActivity.this);
        this.detailMahasiswa = session.getUserDetails();
        this.role = Integer.parseInt(detailMahasiswa.get("role"));
        //Log.e("username", detailMahasiswa.get("username"));

        if (role == 2){
            // add devisor
            this.newDevisor(menu);
            // section with own in click listener
            this.newLabel("Admin", false, menu);

            MaterialSection admin1 = this.newSection("Mengelola Kelas", this.getResources().getDrawable(R.drawable.mengelola_kelas), new AdminKelasFragment(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
            admin1.setFragmentTitle("Mengelola Kelas");

            MaterialSection admin2 = this.newSection("Approve Deny Request Role", this.getResources().getDrawable(R.drawable.appdeny), new AdminRoleFragment(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
            admin2.setFragmentTitle("Approve Deny Request Role");

            MaterialSection admin3 = this.newSection("Mengelola Forum", this.getResources().getDrawable(R.drawable.mengelola_forum), new AdminThread(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
            admin3.setFragmentTitle("Mengelola Forum");

            MaterialSection admin4 = this.newSection("Reset Database", this.getResources().getDrawable(R.drawable.database), new AdminDatabaseFragment(), false, menu).setSectionColor(Color.parseColor("#57c6ef"));
            admin4.setFragmentTitle("Reset database");
        }
        // add devisor
        this.newDevisor(menu);
        MaterialSection out = this.newSection("Logout", this.getResources().getDrawable(R.drawable.logout), new LogoutFragment(), true, menu).setSectionColor(Color.parseColor("#57c6ef"));
        out.setFragmentTitle("Jadwal Asistensi");
        // set start index
        menu.setStartIndex(0);

        // set this menu
        this.setCustomMenu(menu);

        // set image for recycleview_header
        this.setDrawerHeaderImage(getResources().getDrawable(R.drawable.head2));
    }

    public void setKelas(){
        setAKelas();
    }
}