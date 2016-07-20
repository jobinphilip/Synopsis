package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 7/15/2016.
 */
public class Terms_conditionsClass extends Activity {
    public static final String Login_details = "Login_details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions);
    }

    public void verify_emailfn(View view) {

        SharedPreferences prefs = getSharedPreferences(Login_details, MODE_PRIVATE);
        final  String email = prefs.getString("email", "");
        final String password = prefs.getString("password", "");
        final String phone_number = prefs.getString("phone_number", "");
        final String referalId = prefs.getString("referalId", "");
        Log.d("jobin", "email:" + email + "password:" + password + "phone_number" + phone_number + "referalId:" + referalId + "");


        startActivity(new Intent(getApplicationContext(),BasicInfoClass.class));



/*
////////////////////////////////// volley data processing starts ////////////////////////////////////////////////////////
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("KEY_EMAIL",email);
                params.put("KEY_PASSWORD",password);
                params.put("KEY_PHONE", phone_number);
                params.put("KEY_REFERAL_ID", referalId);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

 ////////////////////////////////volley ends///////////////////////////////////////
*/
    }


}
