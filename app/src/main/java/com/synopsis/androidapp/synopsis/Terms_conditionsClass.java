package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Terms_conditionsClass extends Activity implements View.OnClickListener {
    public static final String Login_details = "Login_details";

    Button accepttermsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions);
        accepttermsBtn = (Button) findViewById(R.id.agreeBtn);
        accepttermsBtn.setOnClickListener(Terms_conditionsClass.this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == accepttermsBtn.getId()) {

            SharedPreferences prefs = getSharedPreferences(Login_details, MODE_PRIVATE);
            final String email = prefs.getString("email", "");
            final String password = prefs.getString("password", "");
            final String firstname = prefs.getString("first_name", "");
            final String lastname = prefs.getString("last_name", "");
            final String phone = prefs.getString("mobile", "");
            final String country_code = prefs.getString("country_code", "");

            WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
            final String ipAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());


           // Log.d("jobin", "inside verifyemailfn email:" + email + "password:" + password + "ip address: " + ipAddress);

            //  String url="http://10.0.2.2:8080/test/login4.php";
            //    String url="http://localhost/test/login4.php";
            // String url="http://10.0.2.2/test/login4.php";
            //  String url = "http://127.0.0.1/test/login3.php";

            // String url="http:// 192.168.1.19/test/login3.php";


///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////
            String url = Constants.baseUrl + "register_user.php";
            RequestQueue requestQueue = Volley.newRequestQueue(Terms_conditionsClass.this);
            StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("jobin", "string response is : " + response);

                    try {
                        JSONObject person = new JSONObject(response);
                        String result = person.getString("result");
                        String error = person.getString("error");
                        if (result.equals("success")) {

                            Intent I=new Intent(getApplicationContext(), BasicInfoClass.class);


                            startActivity(I);
                        } else if (error.equals("user_exists")) {

                            Toast toast = Toast.makeText(getApplicationContext(), "your email Id is already registered. Kindly login", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            finish();
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
                    parameters.put("ipAddress", ipAddress);
                    parameters.put("device", "mobile");
                    parameters.put("firstname", firstname);
                    parameters.put("lastname", lastname);
                    parameters.put("phone", phone);
                    parameters.put("country_code", country_code);

                    parameters.put("Action", "registration_form");


                    return parameters;
                }
            };
            requestQueue.add(stringrequest);


            stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

    }
}
