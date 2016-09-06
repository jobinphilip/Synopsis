package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class CreditsList_View_Adapter extends BaseAdapter {
    Activity activity;
    TextView nameTv,dateTV;

    public ArrayList<HashMap<String, String>> list;

    public CreditsList_View_Adapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();

        this.activity=activity;
        this.list=list;
    }


    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView = inflater.inflate(R.layout.credits_list_view_root_element, null);

            nameTv=(TextView) convertView.findViewById(R.id.lv_name);
            dateTV=(TextView) convertView.findViewById(R.id.lv_date);

        }

        HashMap<String, String> map=list.get(position);
        nameTv.setText(map.get("referal_name"));
        dateTV.setText(map.get("referal_date"));

        return convertView;
    }
}
