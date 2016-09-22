package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;


public class Credits extends Fragment {
    TextView credits_self_verify_amnt_tv, balance_total_tv, redeemed_total_tv, credits_self_verify_total_tv, credits_verified_referal_amount_tv, credits_verified_referal_number_tv, credits_verified_referal_total_tv, credits_referal_bonus_amount_tv, credits_referal_bonus_number_tv, credits_referal_bonus_total_tv, credits_total_tv;
    String email, password;
    Button redeem_credit_button;
    int redeem_total;
    float credits_self_verify_amnt, credits_verified_referal_number, credits_verified_referal_total, credits_referal_bonus_number, credits_referal_bonus_total, credits_total;
    ArrayList<HashMap<String, String>> veriried_referals_list;
    ArrayList<HashMap<String, String>> referance_list;
    ArrayList<HashMap<String, String>> redeem_list;
    LinearLayout verified_referals_linear_layout, referral_bonus_linear_layout,redeemed_LinearLayout;
    ImageButton google, fb, twitter, linkedin;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //   return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.credits, container, false);
        //  setContentView(R.layout.credits);
        credits_self_verify_amnt_tv = (TextView) view.findViewById(R.id.credits_self_verify_amnt_tv);
        google = (ImageButton) view.findViewById(R.id.google_IB);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://plus.google.com/107175884091313665554";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        fb = (ImageButton) view.findViewById(R.id.fb_IB);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/pages/Synopsis-Solutions/272663012760030";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        linkedin = (ImageButton) view.findViewById(R.id.ln_IB);
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.linkedin.com/in/synopsissolutions";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        twitter = (ImageButton) view.findViewById(R.id.tw_IB);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/Synopsisjobs";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        credits_self_verify_total_tv = (TextView) view.findViewById(R.id.credits_self_verify_total_tv);
        redeemed_total_tv = (TextView) view.findViewById(R.id.redeemed_total_tv);
        balance_total_tv = (TextView) view.findViewById(R.id.balance_total_tv);
        credits_verified_referal_amount_tv = (TextView) view.findViewById(R.id.credits_verified_referal_amount_tv);
        credits_verified_referal_number_tv = (TextView) view.findViewById(R.id.credits_verified_referal_number_tv);
        credits_verified_referal_total_tv = (TextView) view.findViewById(R.id.credits_verified_referal_total_tv);
        credits_referal_bonus_amount_tv = (TextView) view.findViewById(R.id.credits_referal_bonus_amount_tv);
        credits_referal_bonus_number_tv = (TextView) view.findViewById(R.id.credits_referal_bonus_number_tv);
        credits_referal_bonus_total_tv = (TextView) view.findViewById(R.id.credits_referal_bonus_total_tv);
        credits_total_tv = (TextView) view.findViewById(R.id.earned_total_tv);
        redeemed_LinearLayout = (LinearLayout) view.findViewById(R.id.redeemed_LinearLayout);
        verified_referals_linear_layout = (LinearLayout) view.findViewById(R.id.verified_referals_linear_layout);
        verified_referals_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                intent.putExtra("veriried_referals_list", (ArrayList<HashMap<String, String>>) veriried_referals_list);
                intent.setClass(getActivity().getApplicationContext(), Verified_referals_list.class);
                startActivity(intent);
            }
        });
        referral_bonus_linear_layout = (LinearLayout) view.findViewById(R.id.referral_bonus_linear_layout);
        referral_bonus_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                intent.putExtra("referance_list", (ArrayList<HashMap<String, String>>) referance_list);
                intent.setClass(getActivity().getApplicationContext(), Referals_list.class);
                startActivity(intent);
            }
        });

        redeem_credit_button = (Button) view.findViewById(R.id.redeem_credit_button);


        credits_self_verify_amnt = 0;
        credits_verified_referal_number = 0;
        credits_verified_referal_total = 0;


        if (CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
        {
            String url = Constants.baseUrl + "credits.php";

            SharedPreferences prefs = this.getActivity().getSharedPreferences("Login_details", Context.MODE_PRIVATE);
            email = prefs.getString("email", "");
            password = prefs.getString("password", "");

            ///////////////////////////////volley   ///////////////////////////////////////////////////////////////
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    try {

                        int credits_self_verify_amnt2, credits_verified_referal_total2, credits_referal_bonus_number2, credits_referal_bonus_total2;
                        JSONObject credits_jobj = new JSONObject(response);
                        String result = credits_jobj.getString("result");
                        String error = credits_jobj.getString("error");
                        redeem_total = credits_jobj.getInt("total_redeem");
                        Log.d("jobin", "redeem total is:" + redeem_total+"");
                        Log.d("jobin", "credits jobj is:" + credits_jobj.toString());
                        if (result.equals("success")) {
                            Log.d("jobin", "success");
                            final String verification_status = credits_jobj.getString("verification_status");

                            float self_verification_rate = credits_jobj.getInt("self_verification_rate");

                            if (!verification_status.equals("verified")) {
                                credits_self_verify_amnt = 0;
                            } else {
                                credits_self_verify_amnt = ((self_verification_rate / 100) * 5000);

                            }
                            String referal_details = credits_jobj.getString("referaldetails");
                            String verified_referals = credits_jobj.getString("verified_referals");
                            Log.d("jobin", "here");
                            String redeem_details = credits_jobj.getString("redeem_details");
                            Log.d("jobin", "redeem details"+redeem_details);



                            referance_list = new ArrayList<HashMap<String, String>>();
                            JSONArray referals_jsonarray = new JSONArray(referal_details);
                            credits_referal_bonus_number = referals_jsonarray.length();
                            credits_referal_bonus_total = credits_referal_bonus_number * 25;
                            for (int i = 0; i < referals_jsonarray.length(); i++) {
                                JSONObject obj = referals_jsonarray.getJSONObject(i);

                                HashMap<String, String> temp = new HashMap<String, String>();
                                temp.put("referal_name", obj.getString("user_tbl_first_and_middle_name") + " " + obj.getString("user_tbl_last_name"));
                                temp.put("referal_date", obj.getString("user_tbl_time_stamp_registration"));

                                referance_list.add(temp);

                            }


                            veriried_referals_list = new ArrayList<HashMap<String, String>>();
                            JSONArray veriried_referals_jsonarray = new JSONArray(verified_referals);
                            credits_verified_referal_number = veriried_referals_jsonarray.length();
                            credits_verified_referal_total = credits_verified_referal_number * 100;
                            for (int i = 0; i < veriried_referals_jsonarray.length(); i++) {
                                JSONObject obj = veriried_referals_jsonarray.getJSONObject(i);


                                HashMap<String, String> temp = new HashMap<String, String>();
                                temp.put("referal_name", obj.getString("user_tbl_first_and_middle_name") + " " + obj.getString("user_tbl_last_name"));
                                temp.put("referal_date", obj.getString("user_tbl_time_stamp_registration"));
                                veriried_referals_list.add(temp);

                            }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            redeem_list = new ArrayList<HashMap<String, String>>();
                            JSONArray redeem_details_jsonarray = new JSONArray(redeem_details);


                            for (int i = 0; i < redeem_details_jsonarray.length(); i++) {
                                JSONObject obj = redeem_details_jsonarray.getJSONObject(i);
                                HashMap<String, String> temp = new HashMap<String, String>();
                                temp.put("redeem_tbl_claimed_amout", obj.getString("redeem_tbl_claimed_amout"));
                                temp.put("redeem_tbl_granted_amount", obj.getString("redeem_tbl_granted_amount"));
                                temp.put("redeem_tbl_transaction_timestamp", obj.getString("redeem_tbl_transaction_timestamp"));
                                temp.put("redeem_tbl_granted_prize", obj.getString("redeem_tbl_granted_prize"));
                                redeem_list.add(temp);

                            }


                            redeemed_LinearLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = getActivity().getIntent();
                                    intent.putExtra("redeem_details_list", (ArrayList<HashMap<String, String>>) redeem_list);
                                    intent.setClass(getActivity().getApplicationContext(), Credit_Redeems_Details.class);
                                    startActivity(intent);
                                }
                            });


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                            credits_total = 50 + credits_self_verify_amnt + credits_referal_bonus_total + credits_verified_referal_total;
                            final int credits_total2 = Math.round(credits_total);
                            int balance_total = credits_total2 -redeem_total;

                            credits_self_verify_amnt2 = Math.round(credits_self_verify_amnt);
                            final int credits_verified_referal_number2 = Math.round(credits_verified_referal_number);
                            credits_verified_referal_total2 = Math.round(credits_verified_referal_total);
                            credits_referal_bonus_number2 = Math.round(credits_referal_bonus_number);
                            credits_referal_bonus_total2 = Math.round(credits_referal_bonus_total);
                            redeemed_total_tv.setText(redeem_total + "");
                            balance_total_tv.setText(balance_total + "");
                            credits_self_verify_amnt_tv.setText(credits_self_verify_amnt2 + "");
                            credits_self_verify_total_tv.setText(credits_self_verify_amnt2 + "");
                            credits_verified_referal_number_tv.setText(credits_verified_referal_number2 + "");
                            credits_verified_referal_total_tv.setText(credits_verified_referal_total2 + "");
                            credits_referal_bonus_number_tv.setText(credits_referal_bonus_number2 + "");
                            credits_referal_bonus_total_tv.setText(credits_referal_bonus_total2 + "");
                            credits_total_tv.setText(credits_total2 + "");
                            redeem_credit_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if ((credits_total2 >= 1000 && credits_verified_referal_number2 > 0) || (credits_total2 >= 1000 && verification_status.matches("verified"))) {

                                        if (CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
                                        {


///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////
                                            String url = Constants.baseUrl + "credits.php";
                                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                            StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                                                @Override
                                                public void onResponse(String response) {
                                                    Log.d("jobin", "string response is : " + response);

                                                    try {
                                                        JSONObject person = new JSONObject(response);
                                                        String result = person.getString("result");
                                                        String error = person.getString("error");
                                                        if (result.equals("success")) {


                                                            String message = "Your request to redeem the balance is submitted. Our agent will contact you within 24 hours";


                                                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                            builder.setTitle("Message");
                                                            builder.setMessage(message);

                                                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {


                                                                    dialog.dismiss();

                                                                }
                                                            });
                                                            builder.create().show();


                                                        } else {

                                                            Toast toast = Toast.makeText(getActivity(), "There was an error while processing your request. Kindly try later", Toast.LENGTH_LONG);
                                                            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                                                            toast.show();


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


                                                    parameters.put("Action", "redeem_credit");


                                                    return parameters;
                                                }
                                            };
                                            requestQueue.add(stringrequest);


                                            stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                                                    10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                        } else {
                                            startActivity(new Intent(getActivity(), Internet_ErrorMessage.class));

                                        }
                                        //message:
                                        //we will recieva a message


                                        //email, phone number ,chat


                                    } else {


                                        String message = "You do not have INR1000 balance or none of your referred users are verified";


                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle("Message");
                                        builder.setMessage(message);

                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {


                                                dialog.dismiss();

                                            }
                                        });
                                        builder.create().show();

                                    }
                                }
                            });

                        }

                    } catch (JSONException e) {
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("email", email);
                    parameters.put("password", password);
                    parameters.put("Action", "view_credits");
                    return parameters;
                }
            };
            requestQueue.add(stringrequest);
            stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            startActivity(new Intent(getActivity(), Internet_ErrorMessage.class));

        }


        return view;
    }


}





