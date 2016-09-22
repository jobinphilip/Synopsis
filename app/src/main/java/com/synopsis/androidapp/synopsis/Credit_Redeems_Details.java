package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kumar on 9/22/2016.
 */
public class Credit_Redeems_Details extends Activity {
    private ListView lv;
    TextView no_one_redeemsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_list);
        no_one_redeemsTV=(TextView)findViewById(R.id.no_redeems_messageTv);
        no_one_redeemsTV.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        ArrayList<HashMap<String, String>> redeem_details_list = ( ArrayList<HashMap<String, String>>)intent.getSerializableExtra("redeem_details_list");
        if(redeem_details_list.isEmpty()||redeem_details_list==null)
        {
            no_one_redeemsTV.setVisibility(View.VISIBLE);
        }
        else {


            lv = (ListView) findViewById(R.id.redeem_details_LV);

            Credit_Redeemed_List_ViewAdapter adapter = new Credit_Redeemed_List_ViewAdapter(this, redeem_details_list);
            lv.setAdapter(adapter);

        }


    }
}
