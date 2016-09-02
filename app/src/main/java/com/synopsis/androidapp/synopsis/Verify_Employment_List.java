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

/**
 * Created by Kumar on 9/1/2016.
 */
public class Verify_Employment_List extends Activity {
    private ListView lv;
    int employment_details_length;
    ArrayList<HashMap<String, String>> employment_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_employment_list);
        lv = (ListView) findViewById(R.id.emp_verification_listview);


///////////////////////////////volley   ///////////////////////////////////////////////////////////////
        String url = Constants.baseUrl + "employment_verification_submit.php";
        RequestQueue requestQueue = Volley.newRequestQueue(Verify_Employment_List.this);
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("jobin", "string response is : " + response);

                try {
                    JSONObject emp_verify_details = new JSONObject(response);
                    String result = emp_verify_details.getString("result");

                    if (result.equals("success")) {

                        String employment_details_string = emp_verify_details.getString("employment_details");

                        employment_details = new ArrayList<HashMap<String, String>>();
                        JSONArray employment_detailsJsonArray = new JSONArray(employment_details_string);
                        employment_details_length = employment_detailsJsonArray.length();

                        for (int i = 0; i < employment_details_length; i++) {
                            JSONObject obj = employment_detailsJsonArray.getJSONObject(i);
/*(','designation'=>'','compensation'=>'','location'=>'','superviser_name'=>'','superviser_contact'=>'','leaving_reason'=>''));
        */
                            HashMap<String, String> temp = new HashMap<String, String>();
                            temp.put("employer_name", obj.getString("emp_ver_tbl_employer_name"));
                            temp.put("contact", obj.getString("emp_ver_tbl_contact"));
                            temp.put("employee_id", obj.getString("emp_ver_tbl_employee_id"));
                            temp.put("working_from", obj.getString("emp_ver_tbl_working_from"));
                            temp.put("worked_till", obj.getString("emp_ver_tbl_worked_till"));
                            temp.put("designation", obj.getString("emp_ver_tbl_designation"));
                            temp.put("compensation", obj.getString("emp_ver_tbl_compensation"));
                            temp.put("location", obj.getString("emp_ver_tbl_location"));

                            temp.put("superviser_name", obj.getString("emp_ver_tbl_superviser_name"));
                            temp.put("superviser_contact", obj.getString("emp_ver_tbl_superviser_contact"));
                            temp.put("leaving_reason", obj.getString("emp_ver_tbl_leaving_reason"));

                            employment_details.add(temp);


                        }

                        Employment_List_View_Adapter adapter = new Employment_List_View_Adapter(Verify_Employment_List.this, employment_details);
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
                parameters.put("Action", "retrieve_emp_verification_details");


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

                Intent intent = new Intent(getApplicationContext(), Verify_Employment_Details.class);
                HashMap<String, String> extracted_emp_detail = employment_details.get(position);

                intent.putExtra("extracted_emp_detail", extracted_emp_detail);
                startActivity(intent);
            }
        });


    }

    public void add_educationfn(View v) {
        startActivity(new Intent(getApplicationContext(), Verify_educaton.class));
        finish();
    }
}
