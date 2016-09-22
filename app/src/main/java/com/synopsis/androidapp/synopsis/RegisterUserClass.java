package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
    Spinner self_or_refer_spinner;
    EditText emailET, confirmEmailET, passwordET, first_nameET, last_nameET, phoneET, country_codeET, referorIdEt,middlename_ET;
    String referror_string, firstname_string, middle_name_string,lastname_string, email_string, confirm_email_string, password_string, phone_string, country_code_string, referorId, referance_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user_page);
        self_or_refer_spinner = (Spinner) findViewById(R.id.self_or_refer_spinner);
        first_nameET = (EditText) findViewById(R.id.firstname_ET);
        middlename_ET= (EditText) findViewById(R.id.middlename_ET);
        last_nameET = (EditText) findViewById(R.id.last_nameET);
        phoneET = (EditText) findViewById(R.id.phone_ET);
        emailET = (EditText) findViewById(R.id.email_Et);
        confirmEmailET = (EditText) findViewById(R.id.confirm_email_Et);
        passwordET = (EditText) findViewById(R.id.passwordET);
        country_codeET = (EditText) findViewById(R.id.Country_code_reg_user);
        referorIdEt = (EditText) findViewById(R.id.referorIdET);
        referorIdEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    referror_string = referorIdEt.getText().toString().trim();


                    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                    CharSequence inputStr = referror_string;

                    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(inputStr);
                    if (!(matcher.matches())&&!referror_string.matches("No One Referred Me")) {
                        referorIdEt.setError("invalid referror Id");
                    } else if (referror_string.matches("No One Referred Me")) {
                        referorIdEt.setError(null);
                    }

                    else if (referror_string.matches("")) {
                        referorIdEt.setError(null);
                    } else {


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
                                        referorIdEt.setError(null);
                                        return;

                                    } else {
                                        referorIdEt.setError("Invalid Referror Id ");
                                        return;

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

                                parameters.put("email", referror_string);


                                parameters.put("Action", "email_check_form");


                                return parameters;
                            }
                        };
                        requestQueue.add(stringrequest);


                        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                                10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    }
                    /////////////////////////////////////////////////server check phone number already registered ends//////////////////////

                }
            }
        });

        referorIdEt.setEnabled(false);
        passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.register_by, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        self_or_refer_spinner.setAdapter(adapter);
        self_or_refer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                referance_type = parent.getItemAtPosition(position).toString();
                if (referance_type.matches("Referred By Others")) {
                    referorIdEt.setEnabled(true);
                    referorIdEt.requestFocus();
                    referorIdEt.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(referorIdEt, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    referorIdEt.setEnabled(false);
                    referorIdEt.setText("No One Referred Me");
                    referorIdEt.setError(null);

                }
                Log.d("jobin", "selected referrance type :" + referance_type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        passwordET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;


                if (event.getAction() == MotionEvent.ACTION_UP) {

                    if ((event.getRawX() >= (passwordET.getRight() - passwordET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) && !visibility) {

                        passwordET.setTransformationMethod(null);
                        visibility = true;
                        passwordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_password, 0);
                        return true;
                    } else if ((event.getRawX() >= (passwordET.getRight() - passwordET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) && visibility) {
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

                    email_string = emailET.getText().toString().trim();


                    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                    CharSequence inputStr = email_string;

                    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(inputStr);
                    if (!(matcher.matches())) {
                        emailET.setError("invalid email");
                    } else {


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
                                        return;

                                    } else {
                                        emailET.setError(null);
                                        return;

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

                    }
                    /////////////////////////////////////////////////server check phone number already registered ends//////////////////////

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

                    if (country_codeET.getText().length() <= 3 && phoneET.getText().toString().trim().length() != 10) {
                        phoneET.setError("Phone number should be 10 digits for this country code ");
                    } else {
                        phone_string = phoneET.getText().toString().trim();
                        country_code_string = country_codeET.getText().toString().trim();
                        if (country_code_string.matches("")) {
                            country_code_string = "+91";
                        }


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


                                    } else {
                                        phoneET.setError(null);

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
        middle_name_string=middlename_ET.getText().toString().trim();
        lastname_string = last_nameET.getText().toString().trim();
        phone_string = phoneET.getText().toString().trim();
        country_code_string = country_codeET.getText().toString().trim();
        referorId = referorIdEt.getText().toString().trim();
        String expression2 = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email_string;

        Pattern pattern2 = Pattern.compile(expression2, Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = pattern2.matcher(inputStr);
        if (!(matcher2.matches())) {

            Toast toast = Toast.makeText(getApplicationContext(), "Invalid email id", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        }
        else if(email_string==referorId||email_string.matches(referorId))
        {
            referorIdEt.setError("Referror Id And User Email Cannot Be The Same");
        }
        else if (email_string.matches("") || confirm_email_string.matches("") || password_string.matches("") || firstname_string.matches("") || lastname_string.matches("")) {

            Toast toast = Toast.makeText(getApplicationContext(), "Kindly fill all the mandatory fields", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        } else {
            if (!(email_string.matches(confirm_email_string))) {

                Toast toast = Toast.makeText(getApplicationContext(), "mail address do not match", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

            } else if (password_string.length() <= 5 || password_string.length() >= 12) {

                Toast toast = Toast.makeText(getApplicationContext(), "password length should be between 6 to 12 characters", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            } else if (!(phone_string.matches("[0-9]+"))) {

                Toast toast = Toast.makeText(getApplicationContext(), "invalid phone Number", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            } else {


                if (country_code_string.matches("")) {
                    country_code_string = "+91";
                }


                SharedPreferences.Editor editor = getSharedPreferences(Login_details, MODE_PRIVATE).edit();


                editor.putString("email", email_string);
                editor.putString("password", password_string);
                editor.putString("first_name", firstname_string);
                editor.putString("middle_name", middle_name_string);
                editor.putString("last_name", lastname_string);
                editor.putString("mobile", phone_string);
                editor.putString("country_code", country_code_string);
                editor.putString("referor_id", referorId);


                editor.commit();
                startActivity(new Intent(getApplicationContext(), Terms_conditionsClass.class));
                finish();



            }

        }

    }

    public void login_intent(View v) {
        startActivity(new Intent(getApplicationContext(), LoginClass.class));
        finish();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginClass.class));
        finish();
    }
}
