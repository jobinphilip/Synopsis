package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.EGLDisplay;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Verify_educaton extends Activity {
    EditText register_number_ET, college_nameET, universityET, percentageET, courseNameET, courseTypeET, verify_education_datepickerBtnET;
    String register_number, collegename, university, date_of_pass, percentage, url, email, password, courseName, courseType, verification_status;
    private int year, month, day;
    Button eduverifysubmitbtn;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.verify_education);

        SharedPreferences prefs = getSharedPreferences("Login_details", MODE_PRIVATE);
        email = prefs.getString("email", "");
        password = prefs.getString("password", "");
        eduverifysubmitbtn = (Button) findViewById(R.id.education_verificationSubmitBtn);
        register_number_ET = (EditText) findViewById(R.id.regNoET);
        college_nameET = (EditText) findViewById(R.id.collegeET);
        universityET = (EditText) findViewById(R.id.universityET);

        percentageET = (EditText) findViewById(R.id.percentET);
        courseNameET = (EditText) findViewById(R.id.courseET);
        courseTypeET = (EditText) findViewById(R.id.courseTypeET);
        verify_education_datepickerBtnET = (EditText) findViewById(R.id.verify_education_datepickerBtnET);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        verify_education_datepickerBtnET.setText("" + day + "/" + month + "/" + year);


        eduverifysubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_number = register_number_ET.getText().toString().trim();
                collegename = college_nameET.getText().toString().trim();
                university = universityET.getText().toString().trim();
                date_of_pass = verify_education_datepickerBtnET.getText().toString().trim();
                percentage = percentageET.getText().toString().trim();
                courseName = courseNameET.getText().toString().trim();
                courseType = courseTypeET.getText().toString().trim();

                if (register_number.equals("") || collegename.equals("") || university.equals("") || date_of_pass.equals("") || percentage.equals("") || courseName.equals("") || courseType.equals("")) {

                    Toast toast = Toast.makeText(getApplicationContext(), "Kindly fill all the fields", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                } else {
                    url = Constants.baseUrl + "education_verification.php";
                    ///////////////////////////////volley  ///////////////////////////////////////////////////////////////
                    RequestQueue requestQueue = Volley.newRequestQueue(Verify_educaton.this);
                    StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("jobin", "string response is : " + response);

                            try {
                                JSONObject person = new JSONObject(response);
                                String result = person.getString("result");
                                String error = person.getString("error");
                                if (result.equals("success")) {
                                    startActivity(new Intent(getApplicationContext(), VerifyClass.class));
                                } else {
                                    Log.d("jobin", "it happened again..! errror:" + error);
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
                            parameters.put("register_number", register_number);
                            parameters.put("collegename", collegename);
                            parameters.put("university", university);
                            parameters.put("month_and_year_of_pass", date_of_pass);
                            parameters.put("percentage", percentage);
                            parameters.put("course_name", courseName);
                            parameters.put("course_type", courseType);
                            parameters.put("email", email);
                            parameters.put("password", password);


                            parameters.put("Action", "education_verification_form");


                            return parameters;
                        }
                    };
                    requestQueue.add(stringrequest);


                    stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                            10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                }
            }
        });

/*

        try {
///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////

            RequestQueue requestQueue = Volley.newRequestQueue(Verify_educaton.this);
            StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("jobin", "string response is : " + response);

                    try {
                        JSONObject edu_result = new JSONObject(response);
                        String result = edu_result.getString("result");
                        String error = edu_result.getString("error");
                        if (result.equals("success")) {

                            register_number = edu_result.getString("register_number");
                            collegename = edu_result.getString("collegename");
                            university = edu_result.getString("university");
                            date_of_pass = edu_result.getString("month_and_year_of_pass");
                            percentage = edu_result.getString("percentage");
                            courseName = edu_result.getString("course_name");
                            courseType = edu_result.getString("course_type");
                            verification_status = edu_result.getString("verification_status");
                            register_number_ET.setText(register_number);
                            college_nameET.setText(collegename);
                            universityET.setText(university);
                            percentageET.setText(percentage);
                            courseNameET.setText(courseName);
                            courseTypeET.setText(courseType);
                            verify_education_datepickerBtnET.setText(date_of_pass);
                            if (verification_status.matches("verified")) {
                                register_number_ET.setFocusable(false);
                                register_number_ET.setClickable(false);
                                college_nameET.setFocusable(false);
                                college_nameET.setClickable(false);
                                universityET.setFocusable(false);
                                universityET.setClickable(false);
                                percentageET.setFocusable(false);
                                percentageET.setClickable(false);
                                courseNameET.setFocusable(false);
                                courseNameET.setClickable(false);
                                courseTypeET.setFocusable(false);
                                courseTypeET.setClickable(false);
                                verify_education_datepickerBtnET.setFocusable(false);
                                verify_education_datepickerBtnET.setClickable(false);
                                eduverifysubmitbtn.setClickable(false);
                                eduverifysubmitbtn.setVisibility(View.INVISIBLE);


                            }

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


                    parameters.put("Action", "verify_education_retrieval");


                    return parameters;
                }
            };
            requestQueue.add(stringrequest);


            stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        } catch (NullPointerException e) {

        }
        */

    }



/////////////////////////////date picer fn//////////////////////////////////////

    public void verify_edu_date_pickerfn(View v) {
        Log.d("jobin", "date picker");
        showDialog(999);
    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        Log.d("jobin", "idate pick2");
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int date) {
            Log.d("jobin", "datepick3");
            // arg1 = year
            // arg2 = month
            month = month + 1;
            // arg3 = day

            verify_education_datepickerBtnET.setText("" + date + "/" + month + "/" + year);
        }
    };
/////////////////////////////////date picker ends//////////////////////////////
}