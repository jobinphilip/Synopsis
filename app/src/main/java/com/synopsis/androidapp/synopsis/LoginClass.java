package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Formatter;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
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
    ProgressDialog progress;
    boolean visibility=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        unameET = (EditText) findViewById(R.id.uname_ET);
        passwordET = (EditText) findViewById(R.id.PassET);
        sessionCheckBox = (CheckBox) findViewById(R.id.sessioncheckBox);

        try {
            SharedPreferences prefs = getSharedPreferences(Login_details, MODE_PRIVATE);
            String uname = prefs.getString("uname", "");
            String password = prefs.getString("password", "");
            boolean session = prefs.getBoolean("session", false);
            if (session) {
                unameET.setText(uname);
                passwordET.setText(password);
                sessionCheckBox.setChecked(true);
            } else {
                unameET.setText("");
                passwordET.setText("");
                sessionCheckBox.setChecked(false);
            }


        } catch (NullPointerException e) {

        }


        passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());

        passwordET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if (event.getAction() == MotionEvent.ACTION_UP) {

                    if( (event.getRawX() >= (passwordET.getRight() - passwordET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))&&visibility==false) {

                        passwordET.setTransformationMethod(null);
                        passwordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_password, 0);
                        visibility=true;
                        return true;
                    }
                    else  if( (event.getRawX() >= (passwordET.getRight() - passwordET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))&&visibility==true) {

                        passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0);
                        visibility=false;
                        return true;
                    }

                }

                return false;
            }
        });


    }

    public void loginfn(View view) {
        final String uname = unameET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();

        if(CheckNetwork.isInternetAvailable(getApplicationContext())) //returns true if internet available
        {


            progress = new ProgressDialog(this);
            progress.setMessage("Please wait");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();

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
                        Log.d("jobin", response.toString());
                        if (result.equals("success")) {

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
                            editor2.putString("uname", uname);
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
                            boolean sessioncheck = sessionCheckBox.isChecked();
                            if (sessioncheck) {
                                editor2.putBoolean("session", true);
                            } else {
                                editor2.putBoolean("session", false);
                            }

                            editor2.commit();


                            SharedPreferences.Editor editor = getSharedPreferences(user_status, MODE_PRIVATE).edit();


                            switch (status) {
                                case ("email_verification_pending"):

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


                                case ("basic_info_pending"):

                                {
                                    editor.putString("status", "basic_info_pending");
                                    startActivity(new Intent(getApplicationContext(), BasicInfoClass.class));
                                    break;
                                }
                                case ("user_photo_invalid"):

                                {
                                    editor.putString("status", "user_photo_invalid");
                                    startActivity(new Intent(getApplicationContext(), BasicInfoClass.class));
                                    break;
                                }
                                case ("identity_verification_pending"):

                                {
                                    editor.putString("status", "identity_verification_pending");
                                    startActivity(new Intent(getApplicationContext(), Dash_board.class));
                                    break;
                                }
                                case ("education_verification_pending"):

                                {
                                    editor.putString("status", "education_verification_pending");
                                    startActivity(new Intent(getApplicationContext(), Dash_board.class));
                                    break;
                                }
                                case ("employment_verification_pending"):

                                {
                                    editor.putString("status", "employment_verification_pending");
                                    startActivity(new Intent(getApplicationContext(), Dash_board.class));
                                    break;
                                }
                                case ("verified"):

                                {
                                    editor.putString("status", "verified");
                                    startActivity(new Intent(getApplicationContext(), Dash_board.class));
                                    break;
                                }
                                case ("employment_semiverified"):

                                {
                                    editor.putString("status", "employment_semiverified");
                                    startActivity(new Intent(getApplicationContext(), Dash_board.class));
                                    break;
                                }
                                case ("education_semiverified"):

                                {
                                    editor.putString("status", "education_semiverified");
                                    startActivity(new Intent(getApplicationContext(), Dash_board.class));
                                    break;
                                }
                            }
                            editor.commit();

                        } else {


                            Toast toast = Toast.makeText(getApplicationContext(), "Invalid login credentials", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();

                        }

                    } catch (JSONException e) {
                        Log.d("jobin", "json errror:" + e);


                    }
                    progress.dismiss();

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
        else
        {
            startActivity(new Intent(getApplicationContext(),Internet_ErrorMessage.class));
            finish();
        }

    }

    public void registerfn(View view) {

        if(CheckNetwork.isInternetAvailable(getApplicationContext())) //returns true if internet available
        {

            startActivity(new Intent(getApplicationContext(), RegisterUserClass.class));
        }
        else
        {
            startActivity(new Intent(getApplicationContext(),Internet_ErrorMessage.class));
            finish();
        }



    }

    public void forgot_passfn(View v)

    {
        startActivity(new Intent(getApplicationContext(), Forgot_password.class));

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Start_Application.class));
        finish();
    }
}
