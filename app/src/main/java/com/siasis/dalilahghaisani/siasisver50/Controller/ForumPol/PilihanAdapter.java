package com.siasis.dalilahghaisani.siasisver50.Controller.ForumPol;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.siasis.dalilahghaisani.siasisver50.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PilihanAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Integer> option;
    private HashMap<Integer, String> pilihanJudul = new HashMap<Integer, String>();
    private HashMap<Integer, String> pilihanWaktu = new HashMap<Integer, String>();

    private static LayoutInflater inflater = null;

    public PilihanAdapter(Activity activity, ArrayList<Integer> option) {
        this.option = option;
        this.activity = activity;

        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return option.size();
    }

    @Override
    public Object getItem(int position) {
        return option.get(position);
    }

    public String getJudulItem (int position){
        return pilihanJudul.get(option.get(position));
    }
    public String getWaktuItem (int position){
        return pilihanWaktu.get(option.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ListCell cell;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_child_pilihan_polling, null);
            cell = new ListCell();

            cell.Option = (TextView) convertView.findViewById(R.id.option);
            cell.JudulPilihan = (EditText) convertView.findViewById(R.id.judul_option);
            cell.JudulPilihan.setTag(position);
            cell.WaktuPilihan= (EditText) convertView.findViewById(R.id.waktu_option);
            cell.WaktuPilihan.setTag(position);

            convertView.setTag(cell);
        }
        else
        {
            cell = (ListCell) convertView.getTag();
        }

        int tag_positionJ=(Integer) cell.JudulPilihan.getTag();
        int tag_positionW=(Integer) cell.WaktuPilihan.getTag();

        cell.JudulPilihan.setId(tag_positionJ);
        cell.WaktuPilihan.setId(tag_positionW+100);

        Integer opsi = option.get(position);
        pilihanJudul.put(opsi, "");
        pilihanWaktu.put(opsi, "");
        cell.Option.setText("Option " + Integer.toString(opsi));
        cell.JudulPilihan.setText(pilihanJudul.get(opsi));
        cell.WaktuPilihan.setText(pilihanWaktu.get(opsi));

        cell.JudulPilihan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int itemIndex = v.getId();
                    String judulTemp = ((EditText) v).getText().toString();
                    pilihanJudul.put(itemIndex, judulTemp);
                }
            }
        });

        cell.WaktuPilihan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    int itemIndex = v.getId();
                    String waktuTemp = ((EditText) v).getText().toString();
                    pilihanWaktu.put(itemIndex, waktuTemp);
                }
            }
        });

        cell.JudulPilihan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int position2 = cell.JudulPilihan.getId();
                final EditText JudulPilihan2 = (EditText) cell.JudulPilihan;
                if(JudulPilihan2.getText().toString().length()>0) pilihanJudul.put(position2,JudulPilihan2.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cell.WaktuPilihan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int position2 = cell.WaktuPilihan.getId();
                final EditText WaktuPilihan2 = (EditText) cell.WaktuPilihan;
                if(WaktuPilihan2.getText().toString().length()>0) pilihanWaktu.put(position2, WaktuPilihan2.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return convertView;
    }

    private class ListCell
    {
        private TextView Option;
        private EditText JudulPilihan;
        private EditText WaktuPilihan;
    }
}
