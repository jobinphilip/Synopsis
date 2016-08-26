package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;

import android.util.Log;
import android.view.MotionEvent;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterUserClass extends Activity {
    public static final String Login_details = "Login_details";
    boolean visibility = false;
    EditText emailET, confirmEmailET, passwordET, first_nameET, last_nameET, phoneET, country_codeET;
    String firstname_string, lastname_string, email_string, confirm_email_string, password_string, phone_string, country_code_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user_page);
        first_nameET = (EditText) findViewById(R.id.firstname_ET);
        last_nameET = (EditText) findViewById(R.id.last_nameET);
        phoneET = (EditText) findViewById(R.id.phone_ET);
        emailET = (EditText) findViewById(R.id.email_Et);
        confirmEmailET = (EditText) findViewById(R.id.confirm_email_Et);
        passwordET = (EditText) findViewById(R.id.passwordET);
        country_codeET = (EditText) findViewById(R.id.Country_code_reg_user);

        passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());

        passwordET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;



                if (event.getAction() == MotionEvent.ACTION_UP) {

                    if ((event.getRawX() >= (passwordET.getRight() - passwordET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) && !visibility ) {

                        passwordET.setTransformationMethod(null);
                        visibility = true;
                        passwordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_password, 0);
                        return true;
                    } else if ((event.getRawX() >= (passwordET.getRight() - passwordET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) && visibility ) {
                        passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0);
                        visibility = false;
                        return true;
                    }

                }

                return false;
            }
        });


        emailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                email_string=emailET.getText().toString().trim();


                    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                    CharSequence inputStr = email_string;

                    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(inputStr);
                    if (!(matcher.matches())) {
                        emailET.setError("invalid email");
                    }


                   else {


                        final Handler handler = new Handler();

                        final Runnable r = new Runnable() {
                            public void run() {
                                /////////////////////////////////////////////////server check phone number already registered//////////////////////
                                RequestQueue requestQueue = Volley.newRequestQueue(RegisterUserClass.this);
                                String url = Constants.baseUrl + "email_check.php";
                                StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("jobin", "string response is : " + response);

                                        try {
                                            JSONObject person = new JSONObject(response);
                                            String result = person.getString("result");

                                            if (result.equals("user_exists")) {
                                                emailET.setError("Email ID already registered. Please Login ");

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

                                        parameters.put("email", email_string);


                                        parameters.put("Action", "email_check_form");


                                        return parameters;
                                    }
                                };
                                requestQueue.add(stringrequest);


                                stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                                        10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                                /////////////////////////////////////////////////server check phone number already registered ends//////////////////////

                                handler.postDelayed(this, 1000);
                            }
                        };

                        handler.postDelayed(r, 1000);
                    }
                }
            }
        });


        phoneET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputFilter[] filterArray = new InputFilter[1];
                    String code = country_codeET.getText().toString().trim();
                    if (code.length() > 3) {

                        filterArray[0] = new InputFilter.LengthFilter(9);
                        phoneET.setFilters(filterArray);

                    } else {

                        filterArray[0] = new InputFilter.LengthFilter(10);
                        phoneET.setFilters(filterArray);
                    }


                } else {

                    if (country_codeET.getText().length() < 3 && phoneET.getText().toString().trim().length() != 10) {
                        phoneET.setError("Phone number should be 10 digits for this country code ");
                    } else {
                        phone_string = phoneET.getText().toString().trim();
                        country_code_string = country_codeET.getText().toString().trim();
                        if ( country_code_string.matches("")) {
                            country_code_string = "+91";
                        }











                        final Handler handler = new Handler();

                        final Runnable r = new Runnable() {
                            public void run() {
                                /////////////////////////////////////////////////server check phone number already registered//////////////////////
                                RequestQueue requestQueue = Volley.newRequestQueue(RegisterUserClass.this);
                                String url = Constants.baseUrl + "phone_number_check.php";
                                StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("jobin", "string response is : " + response);

                                        try {
                                            JSONObject person = new JSONObject(response);
                                            String result = person.getString("result");

                                            if (result.equals("user_exists")) {
                                                phoneET.setError("Phone number already registered. Please Login ");

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

                                        parameters.put("phone", phone_string);
                                        parameters.put("country_code", country_code_string);

                                        parameters.put("Action", "phone_check_form");


                                        return parameters;
                                    }
                                };
                                requestQueue.add(stringrequest);


                                stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                                        10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                                /////////////////////////////////////////////////server check phone number already registered ends//////////////////////

                                handler.postDelayed(this, 1000);
                            }
                        };

                        handler.postDelayed(r, 1000);
                    }
                }
            }
        });


        country_codeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String code = country_codeET.getText().toString().trim();
                    if (code.length() > 3 && phoneET.getText().toString().trim().length() != 9) {

                        phoneET.setError("Phone number should be 9 digits for this country code ");

                    } else {
                        phoneET.setError(null);
                    }
                }
            }
        });

    }

    public void termsfn(View view) {


        email_string = emailET.getText().toString().trim();
        confirm_email_string = confirmEmailET.getText().toString().trim();
        password_string = passwordET.getText().toString().trim();
        firstname_string = first_nameET.getText().toString().trim();
        lastname_string = last_nameET.getText().toString().trim();
        phone_string = phoneET.getText().toString().trim();
        country_code_string = country_codeET.getText().toString().trim();

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email_string;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (!(matcher.matches())) {
            Toast.makeText(getApplicationContext(), "Invalid email id", Toast.LENGTH_LONG).show();
        } else if (email_string.matches("") || confirm_email_string.matches("") || password_string.matches("") || firstname_string.matches("") || lastname_string.matches("")) {
            Toast.makeText(getApplicationContext(), "Kindly fill all the mandatory fields", Toast.LENGTH_LONG).show();

        } else {
            if (!(email_string.matches(confirm_email_string))) {
                Toast.makeText(getApplicationContext(), "Email address do not match.", Toast.LENGTH_LONG).show();
            } else if (password_string.length() <= 5 || password_string.length() >= 12) {
                Toast.makeText(getApplicationContext(), "password length should be between 6 to 12 characters", Toast.LENGTH_LONG).show();
            }

            /* else if (phone_string.length() < 10) {
                Toast.makeText(getApplicationContext(), "invalid phone Number", Toast.LENGTH_LONG).show();
            }
            */
            else if (!(phone_string.matches("[0-9]+"))) {
                Toast.makeText(getApplicationContext(), "invalid phone Number", Toast.LENGTH_LONG).show();
            } else {

           /*     if (phone_string.length() > 10) {
                    phone_string = phone_string.substring(phone_string.length() - 10, phone_string.length());
                }
             */

                if (country_code_string.equals(null) || country_code_string.matches("")) {
                    country_code_string = "+91";
                }


                SharedPreferences.Editor editor = getSharedPreferences(Login_details, MODE_PRIVATE).edit();


                editor.putString("email", email_string);
                editor.putString("password", password_string);
                editor.putString("first_name", firstname_string);
                editor.putString("last_name", lastname_string);
                editor.putString("mobile", phone_string);
                editor.putString("country_code", country_code_string);


                editor.commit();
                startActivity(new Intent(getApplicationContext(), Terms_conditionsClass.class));


            }

        }

    }
}
