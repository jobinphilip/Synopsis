package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
public class LoginClass extends Activity {
    EditText unameET, passwordET;
    CheckBox sessionCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        unameET = (EditText) findViewById(R.id.uname_ET);
        passwordET = (EditText) findViewById(R.id.PassET);
        sessionCheckBox = (CheckBox) findViewById(R.id.sessioncheckBox);
    }
    public void loginfn(View view) {
        final String uname = unameET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();
        boolean sessionchecking = sessionCheckBox.isChecked();
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        final String ipAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.d("jobin", "inside login fn  email:" + uname + "password:" + password + "session:" + sessionchecking);
        String url = Constants.baseUrl + "submit.php";
        RequestQueue requestQueue = Volley.newRequestQueue(LoginClass.this);


///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////

        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("jobin", "string response is : " + response);
                

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
                parameters.put("password", password);
                parameters.put("ipAddress", ipAddress);
                parameters.put("device", "mobile");
                parameters.put("Action", "login_form");

                Log.d("jobin", "login prameters added");
                return parameters;
            }
        };
        requestQueue.add(stringrequest);


        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void registerfn(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterUserClass.class));

    }

}
