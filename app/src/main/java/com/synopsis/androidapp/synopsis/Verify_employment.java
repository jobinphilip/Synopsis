package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
public class Verify_employment extends Activity {
    EditText employment_verify_nameET, employment_verify_contact_noET, employment_verify_employee_idET, employment_verify_date_ofjoinET, employment_verify_date_of_resignET, employment_verify_designationET, employment_verify_compensationET, employment_verify_locationET, employment_verify_alternate_numET, employment_verify_supervisor_nameET,employment_verify_supervisor_contactET, employment_verify_reason_of_leavingET;
    String employment_verify_name, employment_verify_contact_no, employment_verify_employee_id, employment_verify_date_ofjoin, employment_verify_date_of_resign, employment_verify_designation, employment_verify_compensation, employment_verify_location, employment_verify_alternate_num, employment_verify_supervisor_name,employment_verify_supervisor_contact, employment_verify_reason_of_leaving, url, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_employment_details);
        employment_verify_nameET = (EditText) findViewById(R.id.employment_verification_employerET);
        employment_verify_contact_noET = (EditText) findViewById(R.id.employment_verification_employer_contact);
        employment_verify_employee_idET = (EditText) findViewById(R.id.employment_verification_employee_id);
        employment_verify_date_ofjoinET = (EditText) findViewById(R.id.employment_verification_joining_date);
        employment_verify_date_of_resignET = (EditText) findViewById(R.id.employment_verification_resignation_date);
        employment_verify_designationET = (EditText) findViewById(R.id.employment_verification_designation);
        employment_verify_compensationET = (EditText) findViewById(R.id.employment_verification_compensation);
        employment_verify_locationET = (EditText) findViewById(R.id.employment_verification_location);
        employment_verify_alternate_numET = (EditText) findViewById(R.id.employment_verification_alternate_phone);
        employment_verify_supervisor_nameET = (EditText) findViewById(R.id.employment_verification_supervisor_name);
        employment_verify_supervisor_contactET = (EditText) findViewById(R.id.employment_verification_supervisor_contact);
        employment_verify_reason_of_leavingET = (EditText) findViewById(R.id.employment_verification_reason_for_leaving);


        url = Constants.baseUrl + "employment_verification_submit.php";

    }


    public void verifyEmploymentFn(View view) {

        employment_verify_name = employment_verify_nameET.getText().toString().trim();
        employment_verify_contact_no = employment_verify_contact_noET.getText().toString().trim();
        employment_verify_employee_id = employment_verify_employee_idET.getText().toString().trim();
        employment_verify_date_ofjoin = employment_verify_date_ofjoinET.getText().toString().trim();
        employment_verify_date_of_resign = employment_verify_date_of_resignET.getText().toString().trim();
        employment_verify_designation = employment_verify_designationET.getText().toString().trim();
        employment_verify_compensation = employment_verify_compensationET.getText().toString().trim();
        employment_verify_location = employment_verify_locationET.getText().toString().trim();
        employment_verify_alternate_num = employment_verify_alternate_numET.getText().toString().trim();
        employment_verify_supervisor_name = employment_verify_supervisor_nameET.getText().toString().trim();
        employment_verify_supervisor_contact = employment_verify_supervisor_contactET.getText().toString().trim();
        employment_verify_reason_of_leaving = employment_verify_reason_of_leavingET.getText().toString().trim();


        if (employment_verify_name.equals("") || employment_verify_date_ofjoin.equals("") || employment_verify_date_of_resign.equals("") || employment_verify_designation.equals("") || employment_verify_compensation.equals("") || employment_verify_location.equals("") || employment_verify_reason_of_leaving.equals("")) {
            Toast.makeText(getApplicationContext(), "Kindly fill all the fields", Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences prefs = getSharedPreferences("Login_details", MODE_PRIVATE);
            email = prefs.getString("email", "");
            password = prefs.getString("password", "");

            ///////////////////////////////volley  ///////////////////////////////////////////////////////////////
            RequestQueue requestQueue = Volley.newRequestQueue(Verify_employment.this);
            StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                //    Log.d("jobin", "string response is : " + response);

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
                    parameters.put("employment_verify_name", employment_verify_name);
                    parameters.put("employment_verify_contact_no", employment_verify_contact_no);
                    parameters.put("employment_verify_employee_id", employment_verify_employee_id);
                    parameters.put("employment_verify_date_ofjoin", employment_verify_date_ofjoin);
                    parameters.put("employment_verify_date_of_resign", employment_verify_date_of_resign);
                    parameters.put("employment_verify_designation", employment_verify_designation);
                    parameters.put("employment_verify_compensation", employment_verify_compensation);
                    parameters.put("employment_verify_location", employment_verify_location);
                    parameters.put("employment_verify_alternate_num", employment_verify_alternate_num);
                    parameters.put("employment_verify_supervisor_name", employment_verify_supervisor_name);
                    parameters.put("employment_verify_supervisor_contact", employment_verify_supervisor_contact);
                    parameters.put("employment_verify_reason_of_leaving", employment_verify_reason_of_leaving);

                    parameters.put("email", email);
                    parameters.put("password", password);
                    parameters.put("Action", "employment_verification_form");


                    return parameters;
                }
            };
            requestQueue.add(stringrequest);


            stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        }
    }

}