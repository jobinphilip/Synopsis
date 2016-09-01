package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Kumar on 8/18/2016.
 */
public class Verified_referals_list extends Activity {
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verified_referals);
        Intent intent = getIntent();
        ArrayList<HashMap<String, String>> referance_list = ( ArrayList<HashMap<String, String>>)intent.getSerializableExtra("veriried_referals_list");

        lv=(ListView)findViewById(R.id.verified_referals_LV);

        CreditsList_View_Adapter adapter = new CreditsList_View_Adapter(this, referance_list);
        lv.setAdapter(adapter);



    }

}
