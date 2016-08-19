package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.EGLDisplay;
import android.os.Bundle;
import android.util.Log;
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
 * Created by User on 7/21/2016.
 */
public class Verify_educaton extends Activity {
    EditText register_number_ET, college_nameET, universityET, month_andyear_of_passET, percentageET,courseNameET,courseTypeET;
    String register_number, collegename, university, month_and_year_of_pass, percentage, url,email,password,courseName,courseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_education);
        register_number_ET = (EditText) findViewById(R.id.regNoET);
        college_nameET = (EditText) findViewById(R.id.collegeET);
        universityET = (EditText) findViewById(R.id.universityET);
        month_andyear_of_passET = (EditText) findViewById(R.id.month_andYearOfpassET);
        percentageET = (EditText) findViewById(R.id.percentET);
        courseNameET = (EditText) findViewById(R.id.courseET);
        courseTypeET = (EditText) findViewById(R.id.courseTypeET);

        url = Constants.baseUrl + "education_verification_submit.php";

    }

    public void veriifyEducationFn(View view) {
        register_number = register_number_ET.getText().toString().trim();
        collegename = college_nameET.getText().toString().trim();
        university = universityET.getText().toString().trim();
        month_and_year_of_pass = month_andyear_of_passET.getText().toString().trim();
        percentage = percentageET.getText().toString().trim();
        courseName = courseNameET.getText().toString().trim();
        courseType = courseTypeET.getText().toString().trim();

        if (register_number.equals("") || collegename.equals("") || university.equals("") || month_and_year_of_pass.equals("") || percentage.equals("")|| courseName.equals("")|| courseType.equals("")) {
            Toast.makeText(getApplicationContext(), "Kindly fill all the fields", Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences prefs = getSharedPreferences("Login_details", MODE_PRIVATE);
             email = prefs.getString("email", "");
            password = prefs.getString("password", "");

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
                    parameters.put("month_and_year_of_pass", month_and_year_of_pass);
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
}