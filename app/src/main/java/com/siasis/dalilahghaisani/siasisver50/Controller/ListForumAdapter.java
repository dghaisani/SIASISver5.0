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
public class ListForumAdapter extends BaseAdapter {

    private JSONArray dataArray;
    private Activity activity;

    private static LayoutInflater inflater = null;

    public ListForumAdapter(JSONArray jsonArray, Activity a)
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
            convertView = inflater.inflate(R.layout.list_reqasis_cell, null);
            cell = new ListCell();

            cell.Tanggal = (TextView) convertView.findViewById(R.id.textViewTanggal);
            cell.Judul = (TextView) convertView.findViewById(R.id.textViewJudulReq);

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
            if (jsonObject.getString("Tanggal").isEmpty()){
                cell.Tanggal.setText("");
                cell.Judul.setText(jsonObject.getString("Judul"));
            }
            else{
                cell.Tanggal.setText(jsonObject.getString("Tanggal"));
                cell.Judul.setText(jsonObject.getString("Judul"));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return convertView;
    }



    private  class  ListCell
    {
        private TextView Tanggal;
        private TextView Judul;
    }
}
