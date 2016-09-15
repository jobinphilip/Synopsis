package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
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
 * Created by Kumar on 8/20/2016.
 */
public class Forgot_password  extends Activity{
    EditText forgot_passET;
    String uname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        forgot_passET=(EditText)findViewById(R.id.forgot_passET);
    }
    public void forgot_passwordfn (View v)
    {
        uname=forgot_passET.getText().toString().trim();
        if(uname.equals(""))
        {

            Toast toast = Toast.makeText(getApplicationContext(), "Kindly fill the required field", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
       else
        {


        RequestQueue requestQueue2 = Volley.newRequestQueue(Forgot_password.this);
///////////////////////////////volley ///////////////////////////////////////////////////////////////
        String url=Constants.baseUrl+"password_forgot.php";

        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("jobin", "string response is : " + response);

                try {
                    JSONObject person = new JSONObject(response);
                    String result = person.getString("result");
                    String error = person.getString("error");
                    if (result.matches("Success")) {


                        startActivity(new Intent(getApplicationContext(), Forgot_password_ResetClass.class));
                    }
                    else
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid Email Id", Toast.LENGTH_LONG);
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
                parameters.put("uname", uname);






                return parameters;
            }
        };
        requestQueue2.add(stringrequest);


        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
///////////////////////////////volley ends///////////////////////////////////////////////////////////////


        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),LoginClass.class));
        finish();
    }
}
