package com.example.android.navigationdrawerexample.Controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.navigationdrawerexample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 3/31/2015.
 */
public class ListRoleAdapter extends BaseAdapter {

    private JSONArray dataArray;
    private Activity activity;

    private static LayoutInflater inflater = null;

    public ListRoleAdapter(JSONArray jsonArray, Activity a)
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

        // change the data of cell

        try
        {
            JSONObject jsonObject = this.dataArray.getJSONObject(position);
            if (jsonObject.getInt("Status")==1) cell.Role.setText("Asisten Dosen " +
                    jsonObject.getString("Nama"));
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
