package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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
 * Created by User on 7/15/2016.
 */
public class LoginClass extends Activity {
    EditText unameET, passwordET;
    CheckBox sessionCheckBox;
    public static final String user_status = "user_status";
    public static final String Login_details = "Login_details";

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

        String url = Constants.baseUrl + "login.php";
        RequestQueue requestQueue = Volley.newRequestQueue(LoginClass.this);


///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////

        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jobject = new JSONObject(response);
                    String result = jobject.getString("result");
                    String error = jobject.getString("error");
                    if (result.equals("success"))
                    {
                        String status = jobject.getString("status").trim();

                        String transaction_id = jobject.getString("transaction_id").trim();

                        String email = jobject.getString("email").trim();
                        String first_name = jobject.getString("first_name").trim();
                        String last_name = jobject.getString("last_name").trim();
                        String date_of_birth = jobject.getString("date_of_birth").trim();
                        String city = jobject.getString("city").trim();
                        String state = jobject.getString("state").trim();
                        String country = jobject.getString("country").trim();
                        String mobile = jobject.getString("mobile").trim();
                        String gender = jobject.getString("gender").trim();




                        SharedPreferences prefs = getSharedPreferences(Login_details, MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = prefs.edit();
                        editor2.putString("email", email);
                        editor2.putString("password", password);
                        editor2.putString("first_name", first_name);
                        editor2.putString("last_name", last_name);
                        editor2.putString("date_of_birth", date_of_birth);
                        editor2.putString("gender", gender);
                        editor2.putString("city", city);
                        editor2.putString("state", state);
                        editor2.putString("country", country);
                        editor2.putString("mobile", mobile);
                        editor2.putString("transaction_id", transaction_id);
                        editor2.putString("status", status);

                        editor2.commit();


                        SharedPreferences.Editor editor = getSharedPreferences(user_status, MODE_PRIVATE).edit();



                        switch (status)
                        {
                            case( "email_verification_pending"):

                            {
                                editor.putString("status", "email_verification_pending");

                                String message = "Kindly verify your Email Id to proceed ";
                                new AlertDialog.Builder(LoginClass.this)
                                        .setTitle("Synopsis")
                                        .setMessage(message)
                                        .setPositiveButton("ok", null)
                                        .show();

                                break;
                            }



                        case( "basic_info_pending"):

                        {
                            editor.putString("status", "basic_info_pending");
                            startActivity(new Intent(getApplicationContext(),BasicInfoClass.class));
                            break;
                        }
                        case( "user_photo_invalid"):

                        {
                            editor.putString("status", "user_photo_invalid");
                            startActivity(new Intent(getApplicationContext(),BasicInfoClass.class));
                            break;
                        }
                        case( "identity_verification_pending"):

                        {
                            editor.putString("status", "identity_verification_pending");
                            startActivity(new Intent(getApplicationContext(),Dash_board.class));
                            break;
                        }
                        case( "education_verification_pending"):

                        {
                            editor.putString("status", "education_verification_pending");
                            startActivity(new Intent(getApplicationContext(),Dash_board.class));
                            break;
                        }
                        case( "employment_verification_pending"):

                        {
                            editor.putString("status", "employment_verification_pending");
                            startActivity(new Intent(getApplicationContext(),Dash_board.class));
                            break;
                        }
                        case( "verified"):

                        {
                            editor.putString("status", "verified");
                            startActivity(new Intent(getApplicationContext(),Dash_board.class));
                            break;
                        }
                        case( "employment_semiverified"):

                        {
                            editor.putString("status", "employment_semiverified");
                            startActivity(new Intent(getApplicationContext(),Dash_board.class));
                            break;
                        }
                        case( "education_semiverified"):

                        {
                            editor.putString("status", "education_semiverified");
                            startActivity(new Intent(getApplicationContext(),Dash_board.class));
                            break;
                        }
                    }
                    editor.commit();

                    } else   {

                        Log.d("jobin","result  is:"+result+"error is :"+error);

                        Toast.makeText(getApplicationContext(), "Invalid login credentials", Toast.LENGTH_LONG).show();

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
