package com.siasis.dalilahghaisani.siasisver50.Controller.ForumPol;

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

public class ListWaktuAdapter extends BaseAdapter {

    private JSONArray dataArray;
    private Activity activity;

    private static LayoutInflater inflater = null;

    public ListWaktuAdapter(JSONArray jsonArray, Activity a)
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
            convertView = inflater.inflate(R.layout.list_role_cell, null);
            cell = new ListCell();

            cell.Role = (TextView) convertView.findViewById(R.id.textViewRole);

            convertView.setTag(cell);
        }
        else
        {
            cell = (ListCell) convertView.getTag();
        }

        try
        {
            JSONObject jsonObject = this.dataArray.getJSONObject(position);
            cell.Role.setText(jsonObject.getString("Judul") + " " + jsonObject.getString("Waktu"));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return convertView;
    }



    private  class  ListCell
    {
        private TextView Role;
    }
}
