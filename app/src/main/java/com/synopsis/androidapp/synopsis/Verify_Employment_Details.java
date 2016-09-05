package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
 * Created by Kumar on 9/2/2016.
 */
public class Verify_Employment_Details extends Activity {
    EditText employment_verification_employerET, employment_verification_employer_contact, employment_verification_employee_id, verify_emp_joindateET, verify_emp_resigndateET, employment_verification_designationET, employment_verification_compensationET, verify_employent_autoCompleteTextViewET, employment_verification_supervisor_nameET, employment_verification_supervisor_contactET, employment_verification_reason_for_leavingET;
    Button verify_employment_submitBtn;
    /*  parameters.put("employment_verify_name", employment_verify_name);
                                parameters.put("employment_verify_contact_no", employment_verify_contact_no);
                                parameters.put("employment_verify_employee_id", employment_verify_employee_id);
                                parameters.put("employment_verify_date_ofjoin", employment_verify_date_ofjoin);
                                parameters.put("employment_verify_date_of_resign", employment_verify_date_of_resign);
                                parameters.put("employment_verify_designation", employment_verify_designation);
                                parameters.put("employment_verify_compensation", employment_verify_compensation);
                                parameters.put("employment_verify_location", employment_verify_location);

                                parameters.put("employment_verify_supervisor_name", employment_verify_supervisor_name);
                                parameters.put("employment_verify_supervisor_contact", employment_verify_supervisor_contact);
                                parameters.put("employment_verify_reason_of_leaving", employment_verify_reason_of_leaving);
*/
    String email, password, random_code, employment_verify_name, employment_verify_contact_no, employment_verify_employee_id, employment_verify_date_ofjoin, employment_verify_date_of_resign, employment_verify_designation, employment_verify_compensation, employment_verify_location, employment_verify_supervisor_name, employment_verify_supervisor_contact, employment_verify_reason_of_leaving;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_employment_details);


        Intent intent = getIntent();
        String verification_status = intent.getStringExtra("verification_status");
        HashMap<String, String> extracted_emp_detail = (HashMap<String, String>) intent.getSerializableExtra("extracted_emp_detail");
        Log.d("jobin", "extracted emp details are:" + extracted_emp_detail.toString());
        verify_employment_submitBtn = (Button) findViewById(R.id.verify_employment_submitBtn);

        verify_employment_submitBtn.setText("Save changes");

        employment_verification_employerET = (EditText) findViewById(R.id.employment_verification_employerET);
        employment_verification_employer_contact = (EditText) findViewById(R.id.employment_verification_employer_contact);
        employment_verification_employee_id = (EditText) findViewById(R.id.employment_verification_employee_id);

        verify_emp_joindateET = (EditText) findViewById(R.id.verify_emp_joindateET);
        verify_emp_resigndateET = (EditText) findViewById(R.id.verify_emp_resigndateET);
        employment_verification_designationET = (EditText) findViewById(R.id.employment_verification_designation);
        employment_verification_compensationET = (EditText) findViewById(R.id.employment_verification_compensation);
        verify_employent_autoCompleteTextViewET = (EditText) findViewById(R.id.verify_employent_autoCompleteTextView);
        employment_verification_supervisor_nameET = (EditText) findViewById(R.id.employment_verification_supervisor_name);
        employment_verification_supervisor_contactET = (EditText) findViewById(R.id.employment_verification_supervisor_contact);
        employment_verification_reason_for_leavingET = (EditText) findViewById(R.id.employment_verification_reason_for_leaving);


        employment_verification_employerET.setText(extracted_emp_detail.get("employer_name").toString());

        employment_verification_employer_contact.setText(extracted_emp_detail.get("contact").toString());

        employment_verification_employee_id.setText(extracted_emp_detail.get("employee_id").toString());

        verify_emp_joindateET.setText(extracted_emp_detail.get("working_from").toString());

        verify_emp_resigndateET.setText(extracted_emp_detail.get("worked_till").toString());

        employment_verification_designationET.setText(extracted_emp_detail.get("designation").toString());

        employment_verification_compensationET.setText(extracted_emp_detail.get("compensation").toString());
        verify_employent_autoCompleteTextViewET.setText(extracted_emp_detail.get("location").toString());
        employment_verification_supervisor_nameET.setText(extracted_emp_detail.get("superviser_name").toString());
        employment_verification_supervisor_contactET.setText(extracted_emp_detail.get("superviser_contact").toString());
        employment_verification_reason_for_leavingET.setText(extracted_emp_detail.get("leaving_reason").toString());
        random_code = extracted_emp_detail.get("random_code").toString();

        if (verification_status.matches("verified")) {
            verify_employment_submitBtn.setVisibility(View.INVISIBLE);
            employment_verification_employerET.setEnabled(false);
            employment_verification_employer_contact.setEnabled(false);
            employment_verification_employee_id.setEnabled(false);
            verify_emp_joindateET.setEnabled(false);
            verify_emp_resigndateET.setEnabled(false);
            employment_verification_designationET.setEnabled(false);
            employment_verification_compensationET.setEnabled(false);
            verify_employent_autoCompleteTextViewET.setEnabled(false);
            employment_verification_supervisor_nameET.setEnabled(false);
            employment_verification_supervisor_contactET.setEnabled(false);
            employment_verification_reason_for_leavingET.setEnabled(false);


        } else {
            verify_employment_submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
/*, , , verify_emp_joindateET, verify_emp_resigndateET, , , , , , ;
 */
                    employment_verify_name = employment_verification_employerET.getText().toString().trim();
                    employment_verify_contact_no = employment_verification_employer_contact.getText().toString().trim();
                    employment_verify_employee_id = employment_verification_employee_id.getText().toString().trim();
                    employment_verify_date_ofjoin = verify_emp_joindateET.getText().toString().trim();
                    employment_verify_date_of_resign = verify_emp_resigndateET.getText().toString().trim();
                    employment_verify_designation = employment_verification_designationET.getText().toString().trim();
                    employment_verify_compensation = employment_verification_compensationET.getText().toString().trim();
                    employment_verify_location = verify_employent_autoCompleteTextViewET.getText().toString().trim();

                    employment_verify_supervisor_name = employment_verification_supervisor_nameET.getText().toString().trim();
                    employment_verify_supervisor_contact = employment_verification_supervisor_contactET.getText().toString().trim();
                    employment_verify_reason_of_leaving = employment_verification_reason_for_leavingET.getText().toString().trim();


                    if (employment_verify_name.equals("") || employment_verify_date_ofjoin.equals("") || employment_verify_date_of_resign.equals("") || employment_verify_designation.equals("") || employment_verify_compensation.equals("") || employment_verify_location.equals("") || employment_verify_reason_of_leaving.equals("")) {

                        Toast toast = Toast.makeText(getApplicationContext(), "Kindly fill all the fields", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();


                    } else {
                        SharedPreferences prefs = getSharedPreferences("Login_details", MODE_PRIVATE);
                        email = prefs.getString("email", "");
                        password = prefs.getString("password", "");
                        String url = Constants.baseUrl + "employment_verification_submit.php";
                        ///////////////////////////////volley  ///////////////////////////////////////////////////////////////
                        RequestQueue requestQueue = Volley.newRequestQueue(Verify_Employment_Details.this);
                        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                //    Log.d("jobin", "string response is : " + response);

                                try {
                                    JSONObject person = new JSONObject(response);
                                    String result = person.getString("result");
                                    String error = person.getString("error");
                                    if (result.equals("success")) {

                                        final AlertDialog alertDialog = new AlertDialog.Builder(Verify_Employment_Details.this).create();
                                        alertDialog.setTitle("Title");
                                        alertDialog.setMessage("Changes Saved");
                                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();


                                            }

                                        });


                                        alertDialog.show();


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

                                parameters.put("employment_verify_supervisor_name", employment_verify_supervisor_name);
                                parameters.put("employment_verify_supervisor_contact", employment_verify_supervisor_contact);
                                parameters.put("employment_verify_reason_of_leaving", employment_verify_reason_of_leaving);
                                parameters.put("random_code", random_code);

                                parameters.put("email", email);
                                parameters.put("password", password);
                                parameters.put("Action", "employment_verification_update");


                                return parameters;
                            }
                        };
                        requestQueue.add(stringrequest);


                        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                                10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                    }
                }
            });
        }

    }
}
