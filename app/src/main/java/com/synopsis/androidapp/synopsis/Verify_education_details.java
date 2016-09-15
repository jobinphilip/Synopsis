package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.TimeZone;


public class Verify_education_details extends Fragment {
    EditText register_number_ET, college_nameET, universityET, percentageET, courseNameET, courseTypeET, verify_education_datepickerBtnET;
    Button eduverifysubmitbtn;
    private int year, month, day;
    private Calendar calendar;
    String register_number, collegename, university, date_of_pass, percentage, courseName, courseType, email, password, random_code;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     //   View view = inflater.inflate(R.layout.verify_education, container, false);
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.verify_education, container, false);
        Bundle bundle = new Bundle();
        bundle = this.getArguments();
        HashMap<String, String> extracted_edu_detail = (HashMap<String, String>) bundle.getSerializable("extracted_edu_detail");
        String verification_status = bundle.getString("verification_status");


/*
        Intent intent = getIntent();
        HashMap<String, String> extracted_edu_detail = (HashMap<String, String>) intent.getSerializableExtra("extracted_edu_detail");
        String verification_status = intent.getStringExtra("verification_status");
        */
        Log.d("jobin", "extracted edu details are:" + extracted_edu_detail.toString());
        eduverifysubmitbtn = (Button) view.findViewById(R.id.education_verificationSubmitBtn);

        register_number_ET = (EditText) view.findViewById(R.id.regNoET);
        college_nameET = (EditText) view.findViewById(R.id.collegeET);
        universityET = (EditText) view.findViewById(R.id.universityET);

        percentageET = (EditText) view.findViewById(R.id.percentET);
        courseNameET = (EditText) view.findViewById(R.id.courseET);
        courseTypeET = (EditText) view.findViewById(R.id.courseTypeET);
        verify_education_datepickerBtnET = (EditText) view.findViewById(R.id.verify_education_datepickerBtnET);
        verify_education_datepickerBtnET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date


                    DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

                        // when dialog box is closed, below method will be called.
                        public void onDateSet(DatePicker view, int selectedYear,
                                              int selectedMonth, int selectedDay) {
                            String year1 = String.valueOf(selectedYear);
                            String month1 = String.valueOf(selectedMonth + 1);
                            String day1 = String.valueOf(selectedDay);

                            verify_education_datepickerBtnET.setText(day1 + "/" + month1 + "/" + year1);

                        }
                    };


// Create the DatePickerDialog instance
                    DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                            R.style.AppTheme_PopupOverlay, datePickerListener,
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH));
                    datePicker.setCancelable(false);
                    datePicker.setTitle("Select the date");
                    datePicker.show();

// Listener

                }
            }
        });
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

                    SharedPreferences prefs = getActivity().getSharedPreferences("Login_details", getActivity().MODE_PRIVATE);
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
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("jobin", "string response is : " + response);

                            try {
                                JSONObject person = new JSONObject(response);
                                String result = person.getString("result");
                                String error = person.getString("error");
                                if (result.equals("success")) {


                                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setMessage("Changes Saved");
                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //   finish();

                                            android.support.v4.app.Fragment fragment = new VerifyClass();
                                            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                                fragmentManager.popBackStack();
                                            }
                                            fragmentTransaction.remove(fragment);
                                            fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();
                                        }

                                    });


                                    alertDialog.show();

                                    // startActivity(new Intent(getActivity(), VerifyClass.class));
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


        return view;
    }
}
