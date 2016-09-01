package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.ListView;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kumar on 8/18/2016.
 */
public class Referals_list extends Activity {
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referals_list);
        Intent intent = getIntent();
        ArrayList<HashMap<String, String>> referance_list = ( ArrayList<HashMap<String, String>>)intent.getSerializableExtra("referance_list");
        Log.d("jobin","in the referals_listclass :"+ referance_list+"");

        lv=(ListView)findViewById(R.id.referals_LV);

        CreditsList_View_Adapter adapter = new CreditsList_View_Adapter(this, referance_list);
        lv.setAdapter(adapter);




    }
}
