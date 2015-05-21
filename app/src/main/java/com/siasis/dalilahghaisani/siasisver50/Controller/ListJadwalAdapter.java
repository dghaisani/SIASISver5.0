package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.siasis.dalilahghaisani.siasisver50.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 3/31/2015.
 */
public class ListJadwalAdapter extends BaseAdapter {

    private JSONArray dataArray;
    private Activity activity;

    private static LayoutInflater inflater = null;

    public ListJadwalAdapter(JSONArray jsonArray, Activity a)
    {
        this.dataArray = jsonArray;
        this.activity = a;

        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.dataArray.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // set up convert view if it is null
        ListCell cell;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_jadwal_cell, null);
            cell = new ListCell();

            cell.Judul = (TextView) convertView.findViewById(R.id.judul);
            cell.Asisten = (TextView) convertView.findViewById(R.id.asisten);;
            cell.Tanggal = (TextView) convertView.findViewById(R.id.tanggal);
            cell.Waktu = (TextView) convertView.findViewById(R.id.waktu);
            cell.Ruangan = (TextView) convertView.findViewById(R.id.ruangan);

            cell.mobile = (ImageView) convertView.findViewById(R.id.customer_mobile);

            convertView.setTag(cell);
        }
        else
        {
            cell = (ListCell) convertView.getTag();
        }

        // change the data of cell

        try
        {
            JSONObject jsonObject = this.dataArray.getJSONObject(position);
            cell.Judul.setText(jsonObject.getString("Judul"));
            cell.Asisten.setText(jsonObject.getString("Username"));
            cell.Tanggal.setText(jsonObject.getString("Tanggal"));
            cell.Waktu.setText(jsonObject.getString("W_Mulai") + " - " + jsonObject.getString("W_Akhir"));
            cell.Ruangan.setText(jsonObject.getString("Ruangan"));

            cell.mobile.setImageResource(R.drawable.avahome);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        return convertView;
    }



    private  class  ListCell
    {
        private TextView Judul;
        private TextView Asisten;
        private TextView Tanggal;
        private TextView Waktu;
        private TextView Ruangan;
        private ImageView mobile;
    }
}
