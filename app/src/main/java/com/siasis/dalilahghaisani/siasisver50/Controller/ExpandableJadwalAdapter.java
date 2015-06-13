package com.siasis.dalilahghaisani.siasisver50.Controller;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.siasis.dalilahghaisani.siasisver50.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lenovo on 5/2/2015.
 */
public class ExpandableJadwalAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<JSONObject>> _listDataChild;

    public ExpandableJadwalAdapter (Context context, List<String> listDataHeader,
                                   HashMap<String, List<JSONObject>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final JSONObject jsonObject = (JSONObject) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_jadwal_cell, null);
        }

        TextView judul = (TextView) convertView.findViewById(R.id.judul);
        TextView asisten = (TextView) convertView.findViewById(R.id.asisten);
        TextView waktu = (TextView) convertView.findViewById(R.id.waktu);
        TextView ruangan = (TextView) convertView.findViewById(R.id.ruangan);
        ImageView mobile = (ImageView) convertView.findViewById(R.id.customer_mobile);

        try {
            judul.setText(jsonObject.getString("Judul") + " - " + jsonObject.getString("Nama"));
            asisten.setText(jsonObject.getString("Username"));
            waktu.setText(jsonObject.getString("W_Mulai").substring(0, jsonObject.getString("W_Mulai").length() - 3) +
                    " - " + jsonObject.getString("W_Akhir").substring(0, jsonObject.getString("W_Akhir").length() - 3));
            ruangan.setText(jsonObject.getString("Ruangan"));
            mobile.setImageResource(R.drawable.ava);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_parent, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
