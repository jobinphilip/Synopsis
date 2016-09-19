package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kumar on 8/18/2016.
 */
public class Referals_list extends Activity {
    private ListView lv;
    TextView referals_no_one_refered_warningTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referals_list);
        referals_no_one_refered_warningTV=(TextView)findViewById(R.id.referals_no_one_refered_warningTV);
        referals_no_one_refered_warningTV.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        ArrayList<HashMap<String, String>> referance_list = ( ArrayList<HashMap<String, String>>)intent.getSerializableExtra("referance_list");
        if(referance_list.isEmpty()||referance_list==null)
        {
            referals_no_one_refered_warningTV.setVisibility(View.VISIBLE);
        }
        else {


            lv = (ListView) findViewById(R.id.referals_LV);

            CreditsList_View_Adapter adapter = new CreditsList_View_Adapter(this, referance_list);
            lv.setAdapter(adapter);

        }


    }
}
