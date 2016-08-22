package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
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

    EditText company_nameET, company_contact_personET, company_emailET, company_designationET, company_mobileET, company_employee_numberET;
    Spinner company_interested_in_spinner;
    Spinner company_type_spinner;
    Button company_submitBtn;
    String company_name_string, company_contact_person_string, company_email_string, company_designation_string, company_mobile_string, company_employee_number_string, company_interested_in_string, company_type_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_company_contact);

        company_nameET = (EditText) findViewById(R.id.company_nameET);
        company_contact_personET = (EditText) findViewById(R.id.company_contactpersonET);
        company_emailET = (EditText) findViewById(R.id.company_emailET);
        company_designationET = (EditText) findViewById(R.id.company_designationET);
        company_mobileET = (EditText) findViewById(R.id.company_phoneET);
        company_employee_numberET = (EditText) findViewById(R.id.company_employee_numberET);
         company_interested_in_spinner=(Spinner)findViewById(R.id.company_interested_in_spinner);
      company_type_spinner=(Spinner)findViewById(R.id.companytypeSpinner);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.comnpany_type, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        company_type_spinner.setAdapter(adapter1);

        company_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                company_type_string=parent.getItemAtPosition(position).toString();
                Log.d("jobin","selected company_type_string :"+company_type_string);
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

                company_interested_in_string=parent.getItemAtPosition(position).toString();
                Log.d("jobin","selected interested in  :"+company_interested_in_string);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        company_submitBtn=(Button)findViewById(R.id.company_submitBtn);
        company_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                company_name_string=company_nameET.getText().toString().trim();
                         company_contact_person_string=company_contact_personET.getText().toString().trim();
                         company_email_string=company_emailET.getText().toString().trim();
                              company_designation_string=company_designationET.getText().toString().trim();
                company_mobile_string=company_mobileET.getText().toString().trim();

                        company_employee_number_string=company_employee_numberET.getText().toString().trim();





                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                CharSequence inputStr = company_email_string;

                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(inputStr);
                if (!(matcher.matches()) )
                {
                    Toast.makeText(getApplicationContext(),"Invalid email id",Toast.LENGTH_LONG).show();
                }

            else if(company_name_string.matches("")|| company_contact_person_string.matches("")||  company_email_string.matches("")||  company_designation_string.matches("")||  company_mobile_string.matches("")||  company_employee_number_string.matches("")||  company_interested_in_string.matches("")||  company_type_string.matches(""))
                {
                    Toast.makeText(getApplicationContext(),"Kindly fill all the fields",Toast.LENGTH_LONG).show();
                }
                else if(company_mobile_string.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"invalid phone Number",Toast.LENGTH_LONG).show();
                }
                else if(!(company_mobile_string.matches("[0-9]+") ))
                {
                    Toast.makeText(getApplicationContext(), "invalid phone Number", Toast.LENGTH_LONG).show();
                }

               else
                {
                  //  Toast.makeText(getApplicationContext(),"type: "+company_type_string+"interested in:"+company_interested_in_string,Toast.LENGTH_LONG).show();

                    if(company_mobile_string.length()>10)
                    {
                        company_mobile_string=company_mobile_string.substring(company_mobile_string.length()-10,company_mobile_string.length());
                    }

                    String url= Constants.baseUrl+"company_registration.php";
                    //  String url="http://10.0.2.2:8080/test/login4.php";
                    //    String url="http://localhost/test/login4.php";
                    // String url="http://10.0.2.2/test/login4.php";
                    //  String url = "http://127.0.0.1/test/login3.php";
                    RequestQueue requestQueue = Volley.newRequestQueue(Company_register.this);
                    // String url="http:// 192.168.1.19/test/login3.php";


///////////////////////////////volley   ///////////////////////////////////////////////////////////////

                    StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("jobin", "string response is : " + response);

                            try {
                                JSONObject jobject = new JSONObject(response);
                                String result = jobject.getString("result").trim();
                                String error = jobject.getString("error").trim();
                                Log.d("jobin","result from server is: "+result+"error is:"+error);
                                if(result.equals("success"))
                                {
                                //   Toast.makeText(getApplicationContext(),"Thank you for registering with Synopsis. We will contact you soon.", Toast.LENGTH_LONG).show();
                                    String message = "Thank you for registering with Synopsis. We will contact you soon.";
                                    new AlertDialog.Builder(Company_register.this)
                                            .setTitle("Synopsis")
                                            .setMessage(message)
                                            .setPositiveButton("ok", null)
                                            .show();
                                }
                                else
                                {
                                    Log.d("jobin","error from server:  "+result.toString());
                                    String message = "Kindly check your internet settings";
                                    new AlertDialog.Builder(Company_register.this)
                                            .setTitle("Synopsis")
                                            .setMessage(message)
                                            .setPositiveButton("ok", null)
                                            .show();
                                }

                            }
                            catch (JSONException e)
                            {
                                Log.d("jobin","json errror:"+e);
                            }



                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("jobin","volley error :  "+error.toString());
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