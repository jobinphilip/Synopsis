package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

/**
 * Created by User on 7/15/2016.
 */
public class VerifyClass extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_page);
    }
    public   void identity_verify_fn (View view)
    {
        startActivity(new Intent(getApplicationContext(),Verify_Identity.class));
    }
    public   void education_verifiy_fn (View view)
    {
        SharedPreferences prefs = getSharedPreferences("Login_details", MODE_PRIVATE);
        final String email = prefs.getString("email", "");
        final String password = prefs.getString("password", "");

///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////
        String url = Constants.baseUrl + "education_verification.php";
        RequestQueue requestQueue = Volley.newRequestQueue(VerifyClass.this);
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("jobin", "string response is : " + response);

                try {
                    JSONObject edu_verify_check = new JSONObject(response);
                    String result = edu_verify_check.getString("result");

                    if (result.equals("success")) {

                        Intent I = new Intent(getApplicationContext(), Verify_Education_List.class);
                        startActivity(I);


                    } else {
                        Intent I = new Intent(getApplicationContext(), Verify_educaton.class);
                        startActivity(I);
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


                parameters.put("Action", "check_education");


                return parameters;
            }
        };
        requestQueue.add(stringrequest);


        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }

    public   void employment_verify_fn (View view)
    {
        startActivity(new Intent(getApplicationContext(),Verify_employment.class));
    }

    public   void submit_verification_fn (View view)
    {
      //  startActivity(new Intent(getApplicationContext(),Verify_Identity.class));
    }


}