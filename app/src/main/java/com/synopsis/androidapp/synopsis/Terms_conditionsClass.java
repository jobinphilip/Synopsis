package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 7/15/2016.
 */
public class Terms_conditionsClass extends Activity implements View.OnClickListener {
    public static final String Login_details = "Login_details";

    Button accepttermsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions);
        accepttermsBtn=(Button)findViewById(R.id.agreeBtn);
        accepttermsBtn.setOnClickListener(Terms_conditionsClass.this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId()==accepttermsBtn.getId())
        {

            SharedPreferences prefs = getSharedPreferences(Login_details, MODE_PRIVATE);
            final String email = prefs.getString("email", "");
            final String password = prefs.getString("password", "");

            WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
            final String ipAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());


            Log.d("jobin", "inside verifyemailfn email:" + email + "password:" + password + "ip address: "+ipAddress);
             String url=constants.baseUrl+"submit.php";
          //  String url="http://10.0.2.2:8080/test/login4.php";
         //    String url="http://localhost/test/login4.php";
            // String url="http://10.0.2.2/test/login4.php";
          //  String url = "http://127.0.0.1/test/login3.php";
            RequestQueue requestQueue = Volley.newRequestQueue(Terms_conditionsClass.this);
            // String url="http:// 192.168.1.19/test/login3.php";


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
                    parameters.put("email", email);
                    parameters.put("password", password);
                    parameters.put("ipAddress", ipAddress);
                    parameters.put("device", "mobile");
                    parameters.put("Action", "registration_form");

                    Log.d("jobin", "prameterd added");
                    return parameters;
                }
            };
            requestQueue.add(stringrequest);


            stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                  1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

    }
}
