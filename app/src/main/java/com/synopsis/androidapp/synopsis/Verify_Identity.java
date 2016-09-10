package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.TimeZone;

/**
 * Created by User on 7/21/2016.
 */
public class Verify_Identity extends Fragment {
    EditText firstnameET, LastnameET, fathernameET, mobileET, emailET, alternate_mobileET, dobET, country_codeET, alternate_country_codeET;
    String firstname2, lastname2, fathername2, country_code2, mobile2, alternate_country_code2, alternatemobile2, dateofbirth2, gender2, password, url, email;
    private Calendar calendar;
    Button identityVerificationBtn;

    RadioGroup genderRadioGroup;
    public static final String Login_details = "Login_details";
    private int year, month, day;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.verify_identity_details, container, false);
        firstnameET = (EditText) view.findViewById(R.id.identity_fnameET);
        LastnameET = (EditText) view.findViewById(R.id.identity_lastnameET);
        fathernameET = (EditText) view.findViewById(R.id.identity_fathernameET);
        mobileET = (EditText) view.findViewById(R.id.identity_mobileET);
        emailET = (EditText) view.findViewById(R.id.identity_emailET);
        dobET = (EditText) view.findViewById(R.id.identity_dobET);
        dobET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date


                DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

                    // when dialog box is closed, below method will be called.
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {
                        String year1 = String.valueOf(selectedYear);
                        String month1 = String.valueOf(selectedMonth + 1);
                        String day1 = String.valueOf(selectedDay);

                        dobET.setText(day1 + "/" + month1 + "/" + year1);

                    }
                };


// Create the DatePickerDialog instance
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                        R.style.AppTheme, datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Select the date");
                datePicker.show();

// Listener

            }
        });
        country_codeET = (EditText) view.findViewById(R.id.country_code_id_verify);
        alternate_country_codeET = (EditText) view.findViewById(R.id.alterate_country_code_id_verify);
        alternate_mobileET = (EditText) view.findViewById(R.id.identity_alternate_phoneET);
        genderRadioGroup = (RadioGroup) view.findViewById(R.id.identity_genderRG);
        url = Constants.baseUrl + "identity_verification_submit.php";

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dobET.setText("" + year + "/" + month + "/" + day);

        SharedPreferences prefs = getActivity().getSharedPreferences(Login_details, getActivity().MODE_PRIVATE);
        email = prefs.getString("email", "");
        password = prefs.getString("password", "");

        //////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////
        String url = Constants.baseUrl + "identity_verification_submit.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("jobin", "string response is : " + response);

                try {
                    JSONObject identity_detailsjobj = new JSONObject(response);
                    String result = identity_detailsjobj.getString("result");
                    String error = identity_detailsjobj.getString("error");
                    if (result.equals("success")) {

                        LastnameET.setText(identity_detailsjobj.getString("last_name"));
                        firstnameET.setText(identity_detailsjobj.getString("first_name"));
                        fathernameET.setText(identity_detailsjobj.getString("father_name"));
                        mobileET.setText(identity_detailsjobj.getString("mobile"));
                        emailET.setText(email);
                        emailET.setEnabled(false);
                        alternate_mobileET.setText(identity_detailsjobj.getString("alternate_mobile"));
                        dobET.setText(identity_detailsjobj.getString("date_of_birth"));
                        country_codeET.setText(identity_detailsjobj.getString("country_code"));
                        alternate_country_codeET.setText(identity_detailsjobj.getString("alternate_country_code"));
                        String gender = identity_detailsjobj.getString("gender");
                        RadioButton m = (RadioButton) view.findViewById(R.id.rdb_male);
                        RadioButton f = (RadioButton) view.findViewById(R.id.rdb_female);
                        String s = m.getText().toString().trim();
                        if (s.equals(gender)) {
                            m.setChecked(true);
                        } else {
                            f.setChecked(true);
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

                parameters.put("Action", "check_identity_form");


                return parameters;
            }
        };
        requestQueue.add(stringrequest);


        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        mobileET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputFilter[] filterArray = new InputFilter[1];
                    String code = country_codeET.getText().toString().trim();
                    if (code.length() > 3) {

                        filterArray[0] = new InputFilter.LengthFilter(9);
                        mobileET.setFilters(filterArray);

                    } else {

                        filterArray[0] = new InputFilter.LengthFilter(10);
                        mobileET.setFilters(filterArray);
                    }


                } else {

                    if (country_codeET.getText().length() <= 3 && mobileET.getText().toString().trim().length() != 10) {
                        mobileET.setError("Phone number should be 10 digits for this country code ");
                    } else {
                        mobile2 = mobileET.getText().toString().trim();
                        country_code2 = country_codeET.getText().toString().trim();
                        if (country_code2.matches("")) {
                            country_code2 = "+91";
                        }


                        /////////////////////////////////////////////////server check phone number already registered//////////////////////
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        String url = Constants.baseUrl + "phone_number_check.php";
                        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.d("jobin", "string response is : " + response);

                                try {
                                    JSONObject person = new JSONObject(response);
                                    String result = person.getString("result");

                                    if (result.equals("user_exists")) {
                                        mobileET.setError("Phone number already registered. ");


                                    } else {
                                        mobileET.setError(null);

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

                                parameters.put("phone", mobile2);
                                parameters.put("country_code", country_code2);

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
                    if (code.length() > 3 && mobileET.getText().toString().trim().length() != 9) {

                        mobileET.setError("Phone number should be 9 digits for this country code ");

                    } else {
                        mobileET.setError(null);
                    }
                }
            }
        });


        alternate_mobileET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputFilter[] filterArray = new InputFilter[1];
                    String code = alternate_country_codeET.getText().toString().trim();
                    if (code.length() > 3) {

                        filterArray[0] = new InputFilter.LengthFilter(9);
                        alternate_mobileET.setFilters(filterArray);

                    } else {

                        filterArray[0] = new InputFilter.LengthFilter(10);
                        alternate_mobileET.setFilters(filterArray);
                    }


                } else {

                    if (alternate_country_codeET.getText().length() <= 3 && alternate_mobileET.getText().toString().trim().length() != 10) {
                        alternate_mobileET.setError("Phone number should be 10 digits for this country code ");
                    }
                }
            }
        });


        alternate_country_codeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String code = alternate_country_codeET.getText().toString().trim();
                    if (code.length() > 3 && alternate_mobileET.getText().toString().trim().length() != 9) {

                        alternate_mobileET.setError("Phone number should be 9 digits for this country code ");

                    } else {
                        alternate_mobileET.setError(null);
                    }
                }
            }
        });


        identityVerificationBtn = (Button) view.findViewById(R.id.identityVerificationBtn);

        identityVerificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname2 = firstnameET.getText().toString().trim();
                lastname2 = LastnameET.getText().toString().trim();
                fathername2 = fathernameET.getText().toString().trim();
                mobile2 = mobileET.getText().toString().trim();
                //   email2 = emailET.getText().toString().trim();
                alternatemobile2 = alternate_mobileET.getText().toString().trim();
                dateofbirth2 = dobET.getText().toString().trim();
                if (!dateofbirth2.matches("")) {
                    String year_ofbirth = dateofbirth2.substring(dateofbirth2.length() - 4, dateofbirth2.length());
                    if (Integer.parseInt(year_ofbirth) > year - 16) {

                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Minimum age should be 16", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    } else {

                        country_code2 = country_codeET.getText().toString().trim();
                        alternate_country_code2 = alternate_country_codeET.getText().toString().trim();
                        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                        Log.d("jobin", "radio id:" + selectedId);
                        // find the radiobutton by returned id
                        RadioButton radioButton = (RadioButton) view.findViewById(selectedId);
                        gender2 = radioButton.getText().toString();
       /*
        if (firstname2.equals("") || lastname2.equals("") || fathername2.equals("") || mobile2.equals("") || dateofbirth2.equals("")) {

            Toast toast = Toast.makeText(getApplicationContext(), "Kindly fill all the missing fields", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        */

                        ///////////////////////////////volley   ///////////////////////////////////////////////////////////////
                        String url2 = Constants.baseUrl + "identity_verification_submit.php";
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        StringRequest stringrequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.d("jobin", "string response is : " + response);

                                try {
                                    JSONObject person = new JSONObject(response);
                                    String result = person.getString("result");
                                    String error = person.getString("error");
                                    if (result.equals("success")) {

                                        android.support.v4.app.Fragment fragment = new VerifyClass();
                                        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                            fragmentManager.popBackStack();
                                        }
                                        fragmentTransaction.remove(fragment);
                                        fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();
                                        //    startActivity(new Intent(getActivity().getApplicationContext(), Dash_board.class));


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
                                parameters.put("email", email);
                                parameters.put("password", password);
                                parameters.put("first_name", firstname2);
                                parameters.put("last_name", lastname2);
                                parameters.put("father_name", fathername2);
                                parameters.put("date_of_birth", dateofbirth2);
                                parameters.put("gender", gender2);
                                parameters.put("mobile", mobile2);
                                parameters.put("alternate_mobile", alternatemobile2);
                                parameters.put("country_code", country_code2);
                                parameters.put("alternate_country_code", alternate_country_code2);


                                parameters.put("Action", "identity_verification_form");


                                return parameters;
                            }
                        };
                        requestQueue.add(stringrequest);


                        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                                10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    }
                }
            }
        });


        return view;
    }
}
