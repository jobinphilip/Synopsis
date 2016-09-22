package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kumar on 9/22/2016.
 */
public class Credit_Redeemed_List_ViewAdapter extends BaseAdapter {
    Activity activity;
    TextView redeem_root_claimed_amountTV,redeem_root_granted_amountTV,redeem_root_date_of_transactionTV,redeem_root_remarksTV;

    public ArrayList<HashMap<String, String>> list;

    public Credit_Redeemed_List_ViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
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
            convertView = inflater.inflate(R.layout.redeems_listview_root_element, null);
            redeem_root_claimed_amountTV=(TextView) convertView.findViewById(R.id.redeem_root_claimed_amountTV);
            redeem_root_granted_amountTV=(TextView) convertView.findViewById(R.id.redeem_root_granted_amountTV);
            redeem_root_date_of_transactionTV=(TextView) convertView.findViewById(R.id.redeem_root_date_of_transactionTV);
            redeem_root_remarksTV=(TextView) convertView.findViewById(R.id.redeem_root_remarksTV);
        }

        HashMap<String, String> map=list.get(position);
        redeem_root_claimed_amountTV.setText(map.get("redeem_tbl_claimed_amout"));
        redeem_root_granted_amountTV.setText(map.get("redeem_tbl_granted_amount"));
        redeem_root_date_of_transactionTV.setText(map.get("redeem_tbl_transaction_timestamp"));
        redeem_root_remarksTV.setText(map.get("redeem_tbl_granted_prize"));

        return convertView;
    }
}
