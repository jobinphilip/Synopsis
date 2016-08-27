package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 7/21/2016.
 */
public class Credits extends Activity {
    TextView credits_self_verify_amnt_tv, credits_self_verify_total_tv, credits_verified_referal_amount_tv, credits_verified_referal_number_tv, credits_verified_referal_total_tv, credits_referal_bonus_amount_tv, credits_referal_bonus_number_tv, credits_referal_bonus_total_tv, credits_total_tv;
    String  email, password;
float credits_self_verify_amnt,    credits_verified_referal_number, credits_verified_referal_total,  credits_referal_bonus_number, credits_referal_bonus_total, credits_total;
   // List<String> veriried_referals_list,referance_list;
     ArrayList<HashMap<String, String>> veriried_referals_list;
     ArrayList<HashMap<String, String>> referance_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);
        credits_self_verify_amnt_tv = (TextView) findViewById(R.id.credits_self_verify_amnt_tv);

        credits_self_verify_total_tv = (TextView) findViewById(R.id.credits_self_verify_total_tv);
        credits_verified_referal_amount_tv = (TextView) findViewById(R.id.credits_verified_referal_amount_tv);
        credits_verified_referal_number_tv = (TextView) findViewById(R.id.credits_verified_referal_number_tv);
        credits_verified_referal_total_tv = (TextView) findViewById(R.id.credits_verified_referal_total_tv);
        credits_referal_bonus_amount_tv = (TextView) findViewById(R.id.credits_referal_bonus_amount_tv);
        credits_referal_bonus_number_tv = (TextView) findViewById(R.id.credits_referal_bonus_number_tv);
        credits_referal_bonus_total_tv = (TextView) findViewById(R.id.credits_referal_bonus_total_tv);
        credits_total_tv = (TextView) findViewById(R.id.credits_total_tv);
        String url = Constants.baseUrl + "credits.php";
        SharedPreferences prefs = getSharedPreferences("Login_details", MODE_PRIVATE);
        email = prefs.getString("email", "");
        password = prefs.getString("password", "");
        credits_self_verify_amnt=0;
        credits_verified_referal_number=0;
        credits_verified_referal_total=0;

        ///////////////////////////////volley   ///////////////////////////////////////////////////////////////
        RequestQueue requestQueue = Volley.newRequestQueue(Credits.this);
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("jobin", "string response is : " + response);

                try {
                    JSONObject credits_jobj = new JSONObject(response);
                    String result = credits_jobj.getString("result");
                    String error = credits_jobj.getString("error");

                    if (result.equals("success")) {
                    Log.d("jobin","here");
                        String verification_status = credits_jobj.getString("verification_status");
                        float self_verification_rate = credits_jobj.getInt("self_verification_rate");

                        if(!verification_status.equals("verified"))
                        {
                            credits_self_verify_amnt=0;
                        }
                        else
                        {
                            credits_self_verify_amnt=((self_verification_rate/100)*5000);

                        }
                        String referal_details = credits_jobj.getString("referaldetails");
                        String verified_referals = credits_jobj.getString("verified_referals");

                      referance_list = new ArrayList<HashMap<String,String>>();
                        JSONArray referals_jsonarray = new JSONArray(referal_details);
                        credits_referal_bonus_number=referals_jsonarray.length();
                        credits_referal_bonus_total=credits_referal_bonus_number*25;
                        for (int i = 0; i < referals_jsonarray.length(); i++) {
                            JSONObject obj = referals_jsonarray.getJSONObject(i);

                            HashMap<String,String> temp=new HashMap<String, String>();
                            temp.put("referal_name", obj.getString("user_tbl_first_and_middle_name")+" "+obj.getString("user_tbl_last_name"));
                            temp.put("referal_date", obj.getString("user_tbl_time_stamp_registration"));

                            referance_list.add(temp);


                          //  referance_list.add(obj.getString("user_tbl_first_and_middle_name")+" "+obj.getString("user_tbl_last_name")+","+obj.getString("user_tbl_time_stamp_registration"));

                        }


                    veriried_referals_list = new ArrayList<HashMap<String,String>>();
                        JSONArray veriried_referals_jsonarray = new JSONArray(verified_referals);
                        credits_verified_referal_number=veriried_referals_jsonarray.length();
                        credits_verified_referal_total=credits_verified_referal_number*100;
                        for (int i = 0; i < veriried_referals_jsonarray.length(); i++) {
                            JSONObject obj = veriried_referals_jsonarray.getJSONObject(i);


                            HashMap<String,String> temp=new HashMap<String, String>();
                            temp.put("referal_name", obj.getString("user_tbl_first_and_middle_name")+" "+obj.getString("user_tbl_last_name"));
                            temp.put("referal_date", obj.getString("user_tbl_time_stamp_registration"));

                            veriried_referals_list.add(temp);
                         //   veriried_referals_list.add(obj.getString("user_tbl_first_and_middle_name")+" "+obj.getString("user_tbl_last_name")+","+obj.getString("user_tbl_time_stamp_registration"));

                        }

                        int credits_total2, credits_self_verify_amnt2, credits_verified_referal_number2, credits_verified_referal_total2, credits_referal_bonus_number2, credits_referal_bonus_total2;

                        credits_total = 50 + credits_self_verify_amnt + credits_referal_bonus_total + credits_verified_referal_total;
                        credits_total2 = Math.round(credits_total);
                        credits_self_verify_amnt2 = Math.round(credits_self_verify_amnt);
                        credits_verified_referal_number2 = Math.round(credits_verified_referal_number);
                        credits_verified_referal_total2 = Math.round(credits_verified_referal_total);
                        credits_referal_bonus_number2 = Math.round(credits_referal_bonus_number);
                        credits_referal_bonus_total2 = Math.round(credits_referal_bonus_total);


                        credits_self_verify_amnt_tv.setText(credits_self_verify_amnt2 + "");
                        credits_self_verify_total_tv.setText(credits_self_verify_amnt2 + "");
                        credits_verified_referal_number_tv.setText(credits_verified_referal_number2 + "");
                        credits_verified_referal_total_tv.setText(credits_verified_referal_total2 + "");
                        credits_referal_bonus_number_tv.setText(credits_referal_bonus_number2 + "");
                        credits_referal_bonus_total_tv.setText(credits_referal_bonus_total2 + "");
                        credits_total_tv.setText(credits_total2 + "");

                    } else {
                        Log.d("jobin", "it happened again..! errror:" + error);
                    }

                } catch (JSONException e) {
                    Log.d("jobin", "json errror:" + e);
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jobin", "error response is : " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", email);
                parameters.put("password", password);
                Log.d("jobin", "parameters added ");

                parameters.put("Action", "view_credits");


                return parameters;
            }
        };

        requestQueue.add(stringrequest);



        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

   public void verified_referals_fn(View view)
   {
       Intent intent = getIntent();

       intent.putExtra("veriried_referals_list", (ArrayList<HashMap<String, String>>) veriried_referals_list);
     //  intent.putExtra("referance_list",referance_list);
       intent.setClass(getApplicationContext(),Verified_referals_list.class);
        startActivity(intent);
   }
    public void referal_bonus_fn(View view)
    {
        Intent intent = getIntent();
        intent.putExtra("referance_list",  (ArrayList<HashMap<String, String>>) referance_list);
      //  intent.putExtra("veriried_referals_list",veriried_referals_list);
        intent.setClass(getApplicationContext(),Referals_list.class);
        startActivity(intent);

    }




}





