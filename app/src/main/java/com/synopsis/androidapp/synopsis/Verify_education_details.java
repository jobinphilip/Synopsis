package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kumar on 9/1/2016.
 */
public class Verify_education_details extends Activity {
    EditText register_number_ET, college_nameET, universityET, percentageET, courseNameET, courseTypeET, verify_education_datepickerBtnET;
    Button eduverifysubmitbtn;
    private int year, month, day;
    private Calendar calendar;
    String register_number, collegename, university, date_of_pass, percentage, courseName, courseType, email, password, random_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_education);


        Intent intent = getIntent();
        HashMap<String, String> extracted_edu_detail = (HashMap<String, String>) intent.getSerializableExtra("extracted_edu_detail");
        String verification_status = intent.getStringExtra("verification_status");
        Log.d("jobin", "extracted edu details are:" + extracted_edu_detail.toString());
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

        random_code = extracted_edu_detail.get("random_code").toString();
        register_number_ET.setText(extracted_edu_detail.get("register_number").toString());

        college_nameET.setText(extracted_edu_detail.get("collegename").toString());

        universityET.setText(extracted_edu_detail.get("university").toString());

        percentageET.setText(extracted_edu_detail.get("percentage").toString());

        courseNameET.setText(extracted_edu_detail.get("course_name").toString());

        courseTypeET.setText(extracted_edu_detail.get("course_type").toString());

        verify_education_datepickerBtnET.setText(extracted_edu_detail.get("month_and_year_of_pass").toString());
        if (verification_status.matches("verified")) {
            register_number_ET.setEnabled(false);
            college_nameET.setEnabled(false);
            universityET.setEnabled(false);
            percentageET.setEnabled(false);
            courseNameET.setEnabled(false);
            courseTypeET.setEnabled(false);
            verify_education_datepickerBtnET.setEnabled(false);
            eduverifysubmitbtn.setVisibility(View.INVISIBLE);

        } else {
            eduverifysubmitbtn.setText("Save changes");

            eduverifysubmitbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences prefs = getSharedPreferences("Login_details", MODE_PRIVATE);
                    email = prefs.getString("email", "");
                    password = prefs.getString("password", "");
                    register_number = register_number_ET.getText().toString().trim();
                    collegename = college_nameET.getText().toString().trim();
                    university = universityET.getText().toString().trim();
                    date_of_pass = verify_education_datepickerBtnET.getText().toString().trim();
                    percentage = percentageET.getText().toString().trim();
                    courseName = courseNameET.getText().toString().trim();
                    courseType = courseTypeET.getText().toString().trim();


                    String url = Constants.baseUrl + "education_verification.php";
                    ///////////////////////////////volley  ///////////////////////////////////////////////////////////////
                    RequestQueue requestQueue = Volley.newRequestQueue(Verify_education_details.this);
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

                            parameters.put("random_code", random_code);
                            parameters.put("Action", "education_verification_update");


                            return parameters;
                        }
                    };
                    requestQueue.add(stringrequest);


                    stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                            10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                }
            });
        }


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
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            Log.d("jobin", "datepick3");

            month = month + 1;


            verify_education_datepickerBtnET.setText(year + "/" + month + "/" + day);
        }
    };
/////////////////////////////////date picker ends//////////////////////////////
}
