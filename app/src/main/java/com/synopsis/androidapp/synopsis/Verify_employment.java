package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 7/21/2016.
 */
public class Verify_employment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    EditText verify_emp_joindateET, verify_emp_resigndateET, employment_verify_nameET, employment_verify_contact_noET, employment_verify_employee_idET, employment_verify_designationET, employment_verify_compensationET, employment_verify_supervisor_nameET, employment_verify_supervisor_contactET, employment_verify_reason_of_leavingET;
    String currency, employment_verify_name, employment_verify_contact_no, employment_verify_employee_id, employment_verify_date_ofjoin, employment_verify_date_of_resign, employment_verify_designation, employment_verify_compensation, employment_verify_location, employment_verify_supervisor_name, employment_verify_supervisor_contact, employment_verify_reason_of_leaving, url, email, password;
    private Calendar calendar;
    Spinner employment_currency_spinner;
    AutoCompleteTextView autoCompView;
    Button verify_employment_submitBtn;
    private int year, month, day;
    SimpleDateFormat sdf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verify_employment_details, container, false);


        employment_verify_nameET = (EditText) view.findViewById(R.id.employment_verification_employerET);
        employment_verify_contact_noET = (EditText) view.findViewById(R.id.employment_verification_employer_contact);
        employment_verify_employee_idET = (EditText) view.findViewById(R.id.employment_verification_employee_id);
        verify_emp_joindateET = (EditText) view.findViewById(R.id.verify_emp_joindateET);
        verify_emp_joindateET.setOnClickListener(this);
        verify_emp_resigndateET = (EditText) view.findViewById(R.id.verify_emp_resigndateET);
        verify_emp_resigndateET.setOnClickListener(this);
        employment_verify_designationET = (EditText) view.findViewById(R.id.employment_verification_designation);
        employment_verify_compensationET = (EditText) view.findViewById(R.id.employment_verification_compensation);
        employment_currency_spinner = (Spinner) view.findViewById(R.id.employment_currency_spinner);
        sdf = new SimpleDateFormat("yyyy/MM/dd");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.currencies, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employment_currency_spinner.setAdapter(adapter);
        employment_currency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                currency = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currency = "INR";
            }
        });

        employment_verify_supervisor_nameET = (EditText) view.findViewById(R.id.employment_verification_supervisor_name);
        employment_verify_supervisor_contactET = (EditText) view.findViewById(R.id.employment_verification_supervisor_contact);
        employment_verify_reason_of_leavingET = (EditText) view.findViewById(R.id.employment_verification_reason_for_leaving);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //   verify_emp_joindateET.setText(year + "/" + month + "/" + day);
        //   verify_emp_resigndateET.setText(year + "/" + month + "/" + day);


        autoCompView = (AutoCompleteTextView) view.findViewById(R.id.verify_employent_autoCompleteTextView);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item));
        autoCompView.setOnItemClickListener(Verify_employment.this);

        verify_employment_submitBtn = (Button) view.findViewById(R.id.verify_employment_submitBtn);
        verify_employment_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employment_verify_name = employment_verify_nameET.getText().toString().trim();
                employment_verify_contact_no = employment_verify_contact_noET.getText().toString().trim();
                employment_verify_employee_id = employment_verify_employee_idET.getText().toString().trim();
                employment_verify_date_ofjoin = verify_emp_joindateET.getText().toString().trim();
                employment_verify_date_of_resign = verify_emp_resigndateET.getText().toString().trim();
                employment_verify_designation = employment_verify_designationET.getText().toString().trim();
                employment_verify_compensation = employment_verify_compensationET.getText().toString().trim();
                employment_verify_location = autoCompView.getText().toString().trim();

                employment_verify_supervisor_name = employment_verify_supervisor_nameET.getText().toString().trim();
                employment_verify_supervisor_contact = employment_verify_supervisor_contactET.getText().toString().trim();
                employment_verify_reason_of_leaving = employment_verify_reason_of_leavingET.getText().toString().trim();
                if (currency.matches("")) {
                    currency = "INR";
                }


                if(CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
                {

                    SharedPreferences prefs = getActivity().getSharedPreferences("Login_details", getActivity().MODE_PRIVATE);
                    email = prefs.getString("email", "");
                    password = prefs.getString("password", "");
                    url = Constants.baseUrl + "employment_verification_submit.php";
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
                                    alertDialog.setTitle("Title");
                                    alertDialog.setMessage("Message");
                                    alertDialog.setButton("exit", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            android.support.v4.app.Fragment fragment = new VerifyClass();
                                            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                                fragmentManager.popBackStack();
                                            }
                                            fragmentTransaction.remove(fragment);
                                            fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();
                                            // finish();
                                        }

                                    });


                                    alertDialog.setButton2("Add Employment", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    android.support.v4.app.Fragment fragment = new Verify_employment();
                                                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                    for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                                        fragmentManager.popBackStack();
                                                    }
                                                    fragmentTransaction.remove(fragment);
                                                    fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();

                                                    //     startActivity(new Intent(getActivity(), Verify_employment.class));
                                                }
                                            }
                                    );
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
                            parameters.put("employment_verify_currency", currency);
                            parameters.put("employment_verify_location", employment_verify_location);
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
                else
                {
                    startActivity(new Intent(getActivity(),Internet_ErrorMessage.class));

                }



            }
        });

        return view;

    }

    /////////////////////////////////date picker ends//////////////////////////////
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d("jobin", "item click");
        String str = (String) adapterView.getItemAtPosition(position);

    }

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


                                if(CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
                                {


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
                                else
                                {
                                    startActivity(new Intent(getActivity(),Internet_ErrorMessage.class));

                                }


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
                                if(CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
                                {


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
                                else
                                {
                                    startActivity(new Intent(getActivity(),Internet_ErrorMessage.class));

                                }


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
}