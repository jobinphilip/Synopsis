package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

/**
 * Created by User on 7/21/2016.
 */
public class Company_register extends Activity {

    EditText company_nameET, company_contact_personET, company_emailET, company_designationET, company_mobileET, company_employee_numberET, company_country_codeET;
    Spinner company_interested_in_spinner;
    Spinner company_type_spinner;
    Button company_submitBtn;
    String company_country_code_string, company_name_string, company_contact_person_string, company_email_string, company_designation_string, company_mobile_string, company_employee_number_string, company_interested_in_string, company_type_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_company_contact);

        company_nameET = (EditText) findViewById(R.id.company_nameET);
        company_contact_personET = (EditText) findViewById(R.id.company_contactpersonET);
        company_emailET = (EditText) findViewById(R.id.company_emailET);
        company_designationET = (EditText) findViewById(R.id.company_designationET);
        company_mobileET = (EditText) findViewById(R.id.company_phoneET);
        company_country_codeET = (EditText) findViewById(R.id.Country_code);
        company_employee_numberET = (EditText) findViewById(R.id.company_employee_numberET);
        company_interested_in_spinner = (Spinner) findViewById(R.id.company_interested_in_spinner);
        company_type_spinner = (Spinner) findViewById(R.id.companytypeSpinner);


        company_mobileET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputFilter[] filterArray = new InputFilter[1];
                    String code = company_country_codeET.getText().toString().trim();
                    if (code.length() > 3) {

                        filterArray[0] = new InputFilter.LengthFilter(9);
                        company_mobileET.setFilters(filterArray);

                    } else {

                        filterArray[0] = new InputFilter.LengthFilter(10);
                        company_mobileET.setFilters(filterArray);
                    }


                } else {

                    if (company_country_codeET.getText().length() < 3 && company_mobileET.getText().toString().trim().length() != 10) {
                        company_mobileET.setError("Phone number should be 10 digits for this country code ");
                    }
                }
            }
        });

        company_country_codeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String code = company_country_codeET.getText().toString().trim();
                    if (code.length() > 3 && company_mobileET.getText().toString().trim().length() != 9) {

                        company_mobileET.setError("Phone length should be 9 digits for this country code ");

                    } else {
                        company_mobileET.setError(null);
                    }
                }
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.comnpany_type, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        company_type_spinner.setAdapter(adapter1);

        company_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                company_type_string = parent.getItemAtPosition(position).toString();
                Log.d("jobin", "selected company_type_string :" + company_type_string);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.company_interested_in, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        company_interested_in_spinner.setAdapter(adapter2);

        company_interested_in_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                company_interested_in_string = parent.getItemAtPosition(position).toString();
                Log.d("jobin", "selected interested in  :" + company_interested_in_string);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        company_submitBtn = (Button) findViewById(R.id.company_submitBtn);
        company_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                company_name_string = company_nameET.getText().toString().trim();
                company_contact_person_string = company_contact_personET.getText().toString().trim();
                company_email_string = company_emailET.getText().toString().trim();
                company_designation_string = company_designationET.getText().toString().trim();
                company_country_code_string = company_country_codeET.getText().toString().trim();
                if (company_country_code_string.equals(null) || company_country_code_string.matches("")) {
                    company_country_code_string = "+91";
                }
                company_mobile_string = company_mobileET.getText().toString().trim();

                company_employee_number_string = company_employee_numberET.getText().toString().trim();


                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                CharSequence inputStr = company_email_string;

                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(inputStr);
                if (!(matcher.matches())) {
                    Toast.makeText(getApplicationContext(), "Invalid email id", Toast.LENGTH_LONG).show();
                } else if (company_name_string.matches("") || company_contact_person_string.matches("") || company_email_string.matches("") || company_designation_string.matches("") || company_mobile_string.matches("") || company_employee_number_string.matches("") || company_interested_in_string.matches("") || company_type_string.matches("")) {
                    Toast.makeText(getApplicationContext(), "Kindly fill all the fields", Toast.LENGTH_LONG).show();
                }

                /* else if (company_mobile_string.length() < 10) {
                    Toast.makeText(getApplicationContext(), "invalid phone Number", Toast.LENGTH_LONG).show();
                }
                */
                else if (!(company_mobile_string.matches("[0-9]+"))) {
                    Toast.makeText(getApplicationContext(), "invalid phone Number", Toast.LENGTH_LONG).show();
                } else {
             /*

                    if (company_mobile_string.length() > 10) {
                        company_mobile_string = company_mobile_string.substring(company_mobile_string.length() - 10, company_mobile_string.length());
                    }
*/
                    String url = Constants.baseUrl + "company_registration.php";

                    RequestQueue requestQueue = Volley.newRequestQueue(Company_register.this);


///////////////////////////////volley   ///////////////////////////////////////////////////////////////

                    StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("jobin", "string response is : " + response);

                            try {
                                JSONObject jobject = new JSONObject(response);
                                String result = jobject.getString("result").trim();
                                String error = jobject.getString("error").trim();
                                Log.d("jobin", "result from server is: " + result + "error is:" + error);
                                if (result.equals("success")) {
                                    //   Toast.makeText(getApplicationContext(),"Thank you for registering with Synopsis. We will contact you soon.", Toast.LENGTH_LONG).show();
                                    String message = "Thank you for registering with Synopsis. We will contact you soon.";


                                    final AlertDialog.Builder builder = new AlertDialog.Builder(Company_register.this);
                                    builder.setTitle("Message");
                                    builder.setMessage(message);

                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(getApplicationContext(), Start_Application.class);
                                            startActivity(intent);
                                        }
                                    });
                                    builder.create().show();


                                } else {
                                    Log.d("jobin", "error from server:  " + result.toString());
                                    String message = "Kindly check your internet settings";
                                    new AlertDialog.Builder(Company_register.this)
                                            .setTitle("Synopsis")
                                            .setMessage(message)
                                            .setPositiveButton("ok", null)
                                            .show();
                                }

                            } catch (JSONException e) {
                                Log.d("jobin", "json errror:" + e);
                            }


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("jobin", "volley error :  " + error.toString());
                            String message = "Kindly check your internet settings";
                            new AlertDialog.Builder(Company_register.this)
                                    .setTitle("Synopsis")
                                    .setMessage(message)
                                    .setPositiveButton("ok", null)
                                    .show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();

                            parameters.put("company_name", company_name_string);
                            parameters.put("company_contact_person", company_contact_person_string);
                            parameters.put("company_email", company_email_string);
                            parameters.put("company_designation", company_designation_string);
                            parameters.put("company_mobile_country_code", company_country_code_string);
                            parameters.put("company_mobile", company_mobile_string);
                            parameters.put("company_employee_number", company_employee_number_string);
                            parameters.put("company_interested_in", company_interested_in_string);
                            parameters.put("company_type", company_type_string);
                            parameters.put("Action", "company_registration_form");

                            Log.d("jobin", "prameters added");
                            return parameters;
                        }
                    };
                    requestQueue.add(stringrequest);


                    stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                }

            }


        });


    }


}