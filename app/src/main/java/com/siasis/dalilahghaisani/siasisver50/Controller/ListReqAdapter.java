package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.siasis.dalilahghaisani.siasisver50.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lenovo on 3/31/2015.
 */
public class ListReqAdapter extends BaseAdapter {

    private JSONArray dataArray;
    private Activity activity;


    private static LayoutInflater inflater = null;
    ArrayList<String> selectedStrings = new ArrayList<String>();


    public ListReqAdapter(JSONArray jsonArray, Activity a)
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
        final ListCell cell;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_req_cell, null);
            cell = new ListCell();

            cell.Role = (CheckBox) convertView.findViewById(R.id.checkBoxRole);

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
            cell.Role.setText(jsonObject.getString("Nama"));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        cell.Role.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedStrings.add(cell.Role.getText().toString());
                    Log.e("Berhasil", cell.Role.getText().toString());
                }else{
                    selectedStrings.remove(cell.Role.getText().toString());
                }
            }
        });

        return convertView;
    }
    public ArrayList<String> getSelectedStrings(){
        return selectedStrings;
    }

    private  class  ListCell
    {
        private CheckBox Role;
    }
}
