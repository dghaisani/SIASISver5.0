package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.siasis.dalilahghaisani.siasisver50.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 5/2/2015.
 */
public class ListHadirAdapter extends BaseAdapter {
    private JSONArray dataArray;
    private Activity activity;

    private static LayoutInflater inflater = null;

    public ListHadirAdapter(JSONArray jsonArray, Activity a)
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
        ListCell cell;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_hadir_cell, null);
            cell = new ListCell();

            cell.Nama = (TextView) convertView.findViewById(R.id.username_mahasiswa);

            convertView.setTag(cell);
        }
        else {
            cell = (ListCell) convertView.getTag();
        }
        try {
            JSONObject jsonObject = this.dataArray.getJSONObject(position);
            cell.Nama.setText(jsonObject.getString("Username"));

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private  class  ListCell {
        private TextView Nama;
    }
}
