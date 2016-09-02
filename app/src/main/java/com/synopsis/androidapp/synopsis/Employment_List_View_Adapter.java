package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kumar on 9/1/2016.
 */
public class Employment_List_View_Adapter extends BaseAdapter {
    Activity activity;
    TextView verify_emp_company_name, verify_emp_designation;

    public ArrayList<HashMap<String, String>> list;

    public Employment_List_View_Adapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();

        this.activity = activity;
        this.list = list;
        Log.d("jobin", "in the listview adapter:" + this.list + "");
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
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.verify_employment_listview_root_element, null);

            verify_emp_company_name = (TextView) convertView.findViewById(R.id.verify_emp_company_name);
            verify_emp_designation = (TextView) convertView.findViewById(R.id.verify_emp_designation);

        }

        HashMap<String, String> map = list.get(position);
        verify_emp_company_name.setText(map.get("employer_name"));
        verify_emp_designation.setText(map.get("designation"));


        return convertView;
    }
}
