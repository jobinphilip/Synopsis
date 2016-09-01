package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kumar on 8/31/2016.
 */
public class Education_List_View_Adapter extends BaseAdapter {
    Activity activity;
    TextView verify_edu_courseNameTV, verify_edu_college_nameTV;

    public ArrayList<HashMap<String, String>> list;

    public Education_List_View_Adapter(Activity activity, ArrayList<HashMap<String, String>> list) {
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

            convertView = inflater.inflate(R.layout.verify_education_listview_root_element, null);

            verify_edu_courseNameTV = (TextView) convertView.findViewById(R.id.verify_edu_courseName);
            verify_edu_college_nameTV = (TextView) convertView.findViewById(R.id.verify_edu_college_name);

        }

        HashMap<String, String> map = list.get(position);
        verify_edu_college_nameTV.setText(map.get("collegename"));
        verify_edu_courseNameTV.setText(map.get("course_name"));




        /* temp.put("register_number", obj.getString("register_number"));
                            temp.put("collegename", obj.getString("collegename"));
                            temp.put("university", obj.getString("university"));
                            temp.put("course_name", obj.getString("course_name"));
                            temp.put("course_type", obj.getString("course_type"));
                            temp.put("month_and_year_of_pass", obj.getString("month_and_year_of_pass"));
                            temp.put("percentage", obj.getString("percentage"));
*/

        return convertView;
    }
}
