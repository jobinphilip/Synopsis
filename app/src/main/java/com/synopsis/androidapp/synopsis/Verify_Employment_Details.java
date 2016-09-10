package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Kumar on 9/2/2016.
 */
public class Verify_Employment_Details extends Fragment implements View.OnClickListener {
    EditText employment_verification_employerET, employment_verification_employer_contact, employment_verification_employee_id, verify_emp_joindateET, verify_emp_resigndateET, employment_verification_designationET, employment_verification_compensationET, verify_employent_autoCompleteTextViewET, employment_verification_supervisor_nameET, employment_verification_supervisor_contactET, employment_verification_reason_for_leavingET;
    Button verify_employment_submitBtn;
    String verification_status, currency, email, password, random_code, employment_verify_name, employment_verify_contact_no, employment_verify_employee_id, employment_verify_date_ofjoin, employment_verify_date_of_resign, employment_verify_designation, employment_verify_compensation, employment_verify_location, employment_verify_supervisor_name, employment_verify_supervisor_contact, employment_verify_reason_of_leaving;
    Spinner employment_currency_spinner;
    private Calendar calendar;
    private int year, month, day;
    SimpleDateFormat sdf;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verify_employment_details, container, false);

        sdf = new SimpleDateFormat("yyyy/MM/dd");
        HashMap<String, String> extracted_emp_detail = null;


        Bundle bundle = new Bundle();
        bundle = getArguments();
        Log.d("jobin", "extracted emp details arein bundle are" + bundle.toString());
        if (bundle.getSerializable("extracted_emp_detail") != null) {
            extracted_emp_detail = (HashMap<String, String>) bundle.getSerializable("extracted_emp_detail");

        }
        if (bundle.get("verification_status") != null) {
            verification_status = bundle.getString("verification_status");
        }
        Log.d("jobin", "extracted emp details are:" + extracted_emp_detail.toString());
        verify_employment_submitBtn = (Button) view.findViewById(R.id.verify_employment_submitBtn);

        verify_employment_submitBtn.setText("Save changes");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        employment_verification_employerET = (EditText) view.findViewById(R.id.employment_verification_employerET);
        employment_verification_employer_contact = (EditText) view.findViewById(R.id.employment_verification_employer_contact);
        employment_verification_employee_id = (EditText) view.findViewById(R.id.employment_verification_employee_id);


        verify_emp_joindateET = (EditText) view.findViewById(R.id.verify_emp_joindateET);
        verify_emp_joindateET.setOnClickListener(this);
        verify_emp_resigndateET = (EditText) view.findViewById(R.id.verify_emp_resigndateET);
        verify_emp_resigndateET.setOnClickListener(this);


        employment_verification_designationET = (EditText) view.findViewById(R.id.employment_verification_designation);
        employment_verification_compensationET = (EditText) view.findViewById(R.id.employment_verification_compensation);
        verify_employent_autoCompleteTextViewET = (EditText) view.findViewById(R.id.verify_employent_autoCompleteTextView);
        employment_verification_supervisor_nameET = (EditText) view.findViewById(R.id.employment_verification_supervisor_name);
        employment_verification_supervisor_contactET = (EditText) view.findViewById(R.id.employment_verification_supervisor_contact);
        employment_verification_reason_for_leavingET = (EditText) view.findViewById(R.id.employment_verification_reason_for_leaving);
        currency = extracted_emp_detail.get("currency").toString();

        employment_currency_spinner = (Spinner) view.findViewById(R.id.employment_currency_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.currencies, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employment_currency_spinner.setAdapter(adapter);
        if (currency.matches("INR")) {
            employment_currency_spinner.setSelection(0);
        } else if (currency.matches("USD")) {
            employment_currency_spinner.setSelection(1);
        } else if (currency.matches("GBP")) {
            employment_currency_spinner.setSelection(2);
        } else if (currency.matches("AUD")) {
            employment_currency_spinner.setSelection(3);
        } else if (currency.matches("AED")) {
            employment_currency_spinner.setSelection(4);
        } else if (currency.matches("CAD")) {
            employment_currency_spinner.setSelection(5);
        } else if (currency.matches("SGD")) {
            employment_currency_spinner.setSelection(6);
        } else {
            employment_currency_spinner.setSelection(7);
        }

        employment_currency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                currency = parent.getItemAtPosition(position).toString();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


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
            employment_currency_spinner.setEnabled(false);
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

/*
                    if (employment_verify_name.equals("") || employment_verify_date_ofjoin.equals("") || employment_verify_date_of_resign.equals("") || employment_verify_designation.equals("") || employment_verify_compensation.equals("") || employment_verify_location.equals("") || employment_verify_reason_of_leaving.equals("")) {

                        Toast toast = Toast.makeText(getApplicationContext(), "Kindly fill all the fields", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();


                    }
                    */


                    SharedPreferences prefs = getActivity().getSharedPreferences("Login_details", getActivity().MODE_PRIVATE);
                    email = prefs.getString("email", "");
                    password = prefs.getString("password", "");
                    String url = Constants.baseUrl + "employment_verification_submit.php";
                    ///////////////////////////////volley  ///////////////////////////////////////////////////////////////
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            //    Log.d("jobin", "string response is : " + response);

                            try {
                                JSONObject person = new JSONObject(response);
                                String result = person.getString("result");
                                String error = person.getString("error");
                                if (result.equals("success")) {

                                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog.setTitle("Title");
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
                            parameters.put("employment_verify_currency", currency);
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
            });
        }

        return view;
    }

 /*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_employment_details);

        sdf = new SimpleDateFormat("yyyy/MM/dd");
        Intent intent = getIntent();
        String verification_status = intent.getStringExtra("verification_status");
        HashMap<String, String> extracted_emp_detail = (HashMap<String, String>) intent.getSerializableExtra("extracted_emp_detail");
        Log.d("jobin", "extracted emp details are:" + extracted_emp_detail.toString());
        verify_employment_submitBtn = (Button) findViewById(R.id.verify_employment_submitBtn);

        verify_employment_submitBtn.setText("Save changes");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

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
        currency = extracted_emp_detail.get("currency").toString();

        employment_currency_spinner = (Spinner) findViewById(R.id.employment_currency_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currencies, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employment_currency_spinner.setAdapter(adapter);
        if (currency.matches("INR")) {
            employment_currency_spinner.setSelection(0);
        } else if (currency.matches("USD")) {
            employment_currency_spinner.setSelection(1);
        } else if (currency.matches("GBP")) {
            employment_currency_spinner.setSelection(2);
        } else if (currency.matches("AUD")) {
            employment_currency_spinner.setSelection(3);
        } else if (currency.matches("AED")) {
            employment_currency_spinner.setSelection(4);
        } else if (currency.matches("CAD")) {
            employment_currency_spinner.setSelection(5);
        } else if (currency.matches("SGD")) {
            employment_currency_spinner.setSelection(6);
        } else {
            employment_currency_spinner.setSelection(7);
        }

        employment_currency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                currency = parent.getItemAtPosition(position).toString();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


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
            employment_currency_spinner.setEnabled(false);
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

/*
                    if (employment_verify_name.equals("") || employment_verify_date_ofjoin.equals("") || employment_verify_date_of_resign.equals("") || employment_verify_designation.equals("") || employment_verify_compensation.equals("") || employment_verify_location.equals("") || employment_verify_reason_of_leaving.equals("")) {

                        Toast toast = Toast.makeText(getApplicationContext(), "Kindly fill all the fields", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();


                    }
                    */

/*
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
                                parameters.put("employment_verify_currency", currency);
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
            });
        }

    }
    */

 /*   /////////////////////////////date picer fn//////////////////////////////////////
    int ETid;

    public void verify_edu_date_pickerfn(View v) {
        ETid = v.getId();
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
            if (ETid == R.id.verify_emp_joindateET) {
                verify_emp_joindateET.setText(year + "/" + month + "/" + day);


                employment_verify_date_ofjoin = verify_emp_joindateET.getText().toString().trim();

                employment_verify_date_of_resign = verify_emp_resigndateET.getText().toString().trim();
                if (!employment_verify_date_of_resign.matches("")) {
                    try {
                        if (sdf.parse(employment_verify_date_of_resign).before(sdf.parse(employment_verify_date_ofjoin))) {
                            verify_emp_resigndateET.setError("please verify start date and end date");

                        } else {
                            verify_emp_resigndateET.setError(null);

                            SharedPreferences prefs = getSharedPreferences("Login_details", MODE_PRIVATE);
                            final String email = prefs.getString("email", "");
                            final String password = prefs.getString("password", "");


///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////
                            String url = Constants.baseUrl + "validate_employment_dates.php";
                            RequestQueue requestQueue = Volley.newRequestQueue(Verify_Employment_Details.this);
                            StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.d("jobin", "string response is : " + response);

                                    try {
                                        JSONObject date_validation_jobj = new JSONObject(response);
                                        String result = date_validation_jobj.getString("result");


                                        if (result.matches("invalid")) {

                                            verify_emp_joindateET.setError("Date mismatch with prevous employments");


                                        } else {
                                            verify_emp_joindateET.setError(null);
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
                                    parameters.put("join_date", employment_verify_date_ofjoin);
                                    parameters.put("resign_date", employment_verify_date_of_resign);


                                    parameters.put("Action", "validate_employment_dates");


                                    return parameters;
                                }
                            };
                            requestQueue.add(stringrequest);


                            stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                                    10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                        }

                    } catch (Exception e) {

                    }
                }
            } else {
                verify_emp_resigndateET.setText(year + "/" + month + "/" + day);

                employment_verify_date_ofjoin = verify_emp_joindateET.getText().toString().trim();

                employment_verify_date_of_resign = verify_emp_resigndateET.getText().toString().trim();
                if (!employment_verify_date_ofjoin.matches("")) {
                    try {
                        if (sdf.parse(employment_verify_date_of_resign).before(sdf.parse(employment_verify_date_ofjoin))) {

                            verify_emp_resigndateET.setError("please verify start date and end date");

                        } else {

                            verify_emp_resigndateET.setError(null);


                            SharedPreferences prefs = getSharedPreferences("Login_details", MODE_PRIVATE);
                            final String email = prefs.getString("email", "");
                            final String password = prefs.getString("password", "");


///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////
                            String url = Constants.baseUrl + "validate_employment_dates.php";
                            RequestQueue requestQueue = Volley.newRequestQueue(Verify_Employment_Details.this);
                            StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.d("jobin", "string response is : " + response);

                                    try {
                                        JSONObject date_validation_jobj = new JSONObject(response);
                                        String result = date_validation_jobj.getString("result");
                                        if (result.matches("invalid")) {

                                            verify_emp_resigndateET.setError("Date mismatch with prevous employments");


                                        } else {

                                            verify_emp_resigndateET.setError(null);
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
                                    parameters.put("join_date", employment_verify_date_ofjoin);
                                    parameters.put("resign_date", employment_verify_date_of_resign);


                                    parameters.put("Action", "validate_employment_dates");


                                    return parameters;
                                }
                            };
                            requestQueue.add(stringrequest);


                            stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                                    10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                        }

                    } catch (Exception e) {

                    }
                }
            }

        }
    };
*/

    @Override
    public void onClick(final View view2) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date


        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                String year1 = String.valueOf(selectedYear);
                String month1 = String.valueOf(selectedMonth + 1);
                String day1 = String.valueOf(selectedDay);
                if (view2.getId() == R.id.verify_emp_joindateET) {
                    verify_emp_joindateET.setText(year1 + "/" + month1 + "/" + day1);

                    employment_verify_date_ofjoin = verify_emp_joindateET.getText().toString().trim();

                    employment_verify_date_of_resign = verify_emp_resigndateET.getText().toString().trim();
                    if (!employment_verify_date_of_resign.matches("")) {
                        try {
                            if (sdf.parse(employment_verify_date_of_resign).before(sdf.parse(employment_verify_date_ofjoin))) {
                                verify_emp_resigndateET.setError("please verify start date and end date");

                                                  /*         Fragment frg = null;
                                frg = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
                                final FragmentTransaction ft =getActivity(). getSupportFragmentManager().beginTransaction();
                                ft.detach(frg);
                                ft.attach(frg);
                                ft.commit();

                                Toast.makeText(getActivity(),"please verify start date and end date",Toast.LENGTH_LONG).show();
*/
                            } else {
                                verify_emp_resigndateET.setError(null);

                                SharedPreferences prefs = getActivity().getSharedPreferences("Login_details", getActivity().MODE_PRIVATE);
                                final String email = prefs.getString("email", "");
                                final String password = prefs.getString("password", "");


///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////
                                String url = Constants.baseUrl + "validate_employment_dates.php";
                                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("jobin", "string response is : " + response);

                                        try {
                                            JSONObject date_validation_jobj = new JSONObject(response);
                                            String result = date_validation_jobj.getString("result");


                                            if (result.matches("invalid")) {
/*
                                                Fragment frg = null;
                                                frg = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
                                                final FragmentTransaction ft =getActivity(). getSupportFragmentManager().beginTransaction();
                                                ft.detach(frg);
                                                ft.attach(frg);
                                                ft.commit();
*/
                                                verify_emp_joindateET.setError("Date mismatch with prevous employments");


                                                //          Toast.makeText(getActivity(),"Date mismatch with prevous employments",Toast.LENGTH_LONG).show();
                                            } else {
                                           /*     Fragment frg = null;
                                                frg = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
                                                final FragmentTransaction ft =getActivity(). getSupportFragmentManager().beginTransaction();
                                                ft.detach(frg);
                                                ft.attach(frg);
                                                ft.commit();
    */
                                                verify_emp_joindateET.setError(null);

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
                                        parameters.put("join_date", employment_verify_date_ofjoin);
                                        parameters.put("resign_date", employment_verify_date_of_resign);


                                        parameters.put("Action", "validate_employment_dates");


                                        return parameters;
                                    }
                                };
                                requestQueue.add(stringrequest);


                                stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                                        10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                            }

                        } catch (Exception e) {
                            Log.e("jobin", "exception raised" + e);
                        }
                    }
                } else if (view2.getId() == R.id.verify_emp_resigndateET) {

                    verify_emp_resigndateET.setText(year1 + "/" + month1 + "/" + day1);

                    employment_verify_date_ofjoin = verify_emp_joindateET.getText().toString().trim();

                    employment_verify_date_of_resign = verify_emp_resigndateET.getText().toString().trim();
                    if (!employment_verify_date_ofjoin.matches("")) {
                        try {
                            if (sdf.parse(employment_verify_date_of_resign).before(sdf.parse(employment_verify_date_ofjoin))) {
                                //  Toast.makeText(getActivity(),"please verify start date and end date",Toast.LENGTH_LONG).show();
                            /*    Fragment frg = null;
                                frg = getActivity().getSupportFragmentManager().findFragmentByTag("tag");
                                final FragmentTransaction ft =getActivity(). getSupportFragmentManager().beginTransaction();
                                ft.detach(frg);
                                ft.attach(frg);
                                ft.commit();
   */
                                verify_emp_resigndateET.setError("please verify start date and end date");


                            } else {

                                verify_emp_resigndateET.setError(null);


                                SharedPreferences prefs = getActivity().getSharedPreferences("Login_details", getActivity().MODE_PRIVATE);
                                final String email = prefs.getString("email", "");
                                final String password = prefs.getString("password", "");


///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////
                                String url = Constants.baseUrl + "validate_employment_dates.php";
                                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("jobin", "string response is : " + response);

                                        try {
                                            JSONObject date_validation_jobj = new JSONObject(response);
                                            String result = date_validation_jobj.getString("result");
                                            if (result.matches("invalid")) {

                                                // Toast.makeText(getActivity(),"Date mismatch with prevous employments",Toast.LENGTH_LONG).show();
                                                verify_emp_resigndateET.setError("Date mismatch with prevous employments");


                                            } else {

                                                verify_emp_resigndateET.setError(null);
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
                                        parameters.put("join_date", employment_verify_date_ofjoin);
                                        parameters.put("resign_date", employment_verify_date_of_resign);


                                        parameters.put("Action", "validate_employment_dates");


                                        return parameters;
                                    }
                                };
                                requestQueue.add(stringrequest);


                                stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                                        10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                            }

                        } catch (Exception e) {
                            Log.e("jobin", "exception raised" + e);
                        }
                    }

                }

            }
        };


        DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                R.style.AppTheme, datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        datePicker.show();


    }

    /////////////////////////////////date picker ends//////////////////////////////

}
