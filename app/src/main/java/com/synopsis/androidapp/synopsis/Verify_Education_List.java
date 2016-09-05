package com.synopsis.androidapp.synopsis;

import android.app.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Verify_Education_List extends Activity {
    private ListView lv;
    int education_details_length;
    ArrayList<HashMap<String, String>> education_details;
    String verification_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_education_list);
        lv = (ListView) findViewById(R.id.educ_verification_listview);

///////////////////////////////volley   ///////////////////////////////////////////////////////////////
        String url = Constants.baseUrl + "education_verification.php";
        RequestQueue requestQueue = Volley.newRequestQueue(Verify_Education_List.this);
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("jobin", "string response is : " + response);

                try {
                    JSONObject edu_verify_details = new JSONObject(response);
                    String result = edu_verify_details.getString("result");
                    String error = edu_verify_details.getString("error");
                    verification_status = edu_verify_details.getString("verification_status");

                    if (result.equals("success")) {

                        String education_details_string = edu_verify_details.getString("education_details");

                        education_details = new ArrayList<HashMap<String, String>>();
                        JSONArray education_detailsJsonArray = new JSONArray(education_details_string);
                        education_details_length = education_detailsJsonArray.length();

                        for (int i = 0; i < education_details_length; i++) {
                            JSONObject obj = education_detailsJsonArray.getJSONObject(i);

                            HashMap<String, String> temp = new HashMap<String, String>();
                            temp.put("random_code", obj.getString("edu_ver_tbl_row_referance_id"));
                            temp.put("register_number", obj.getString("edu_ver_tbl_register_number"));
                            temp.put("collegename", obj.getString("edu_ver_tbl_college_name"));
                            temp.put("university", obj.getString("edu_ver_tbl_university_name"));
                            temp.put("course_name", obj.getString("edu_ver_tbl_course_name"));
                            temp.put("course_type", obj.getString("edu_ver_tbl_course_type"));
                            temp.put("month_and_year_of_pass", obj.getString("edu_ver_tbl_month_and_year_of_pass"));
                            temp.put("percentage", obj.getString("edu_ver_tbl_gpa_percent"));

                            education_details.add(temp);


                        }


                        Education_List_View_Adapter adapter = new Education_List_View_Adapter(Verify_Education_List.this, education_details);
                        lv.setAdapter(adapter);


                    } else {

                        Log.d("jobin", "error in verify education list retrieval");
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
                SharedPreferences prefs = getSharedPreferences("Login_details", MODE_PRIVATE);
                final String email = prefs.getString("email", "");
                final String password = prefs.getString("password", "");
                parameters.put("email", email);
                parameters.put("password", password);
                parameters.put("Action", "retrieve_edu_verification_details");


                return parameters;
            }
        };
        requestQueue.add(stringrequest);


        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent intent = new Intent(getApplicationContext(), Verify_education_details.class);
                HashMap<String, String> extracted_edu_detail = education_details.get(position);

                intent.putExtra("extracted_edu_detail", extracted_edu_detail);
                intent.putExtra("verification_status", verification_status);

                startActivity(intent);
            }
        });


    }

    public void add_educationfn(View v) {
        startActivity(new Intent(getApplicationContext(), Verify_educaton.class));
        finish();
    }
}
