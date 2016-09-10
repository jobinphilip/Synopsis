package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
public class Verify_Employment_List extends Fragment {
    private ListView lv;
    int employment_details_length;
    Button verify_employment_list_add_employerBtn;
    String verification_status;
    ArrayList<HashMap<String, String>> employment_details;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verify_employment_list, container, false);
        lv = (ListView) view.findViewById(R.id.emp_verification_listview);

        verify_employment_list_add_employerBtn = (Button) view.findViewById(R.id.verify_employment_list_add_employerBtn);
        verify_employment_list_add_employerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.Fragment fragment = new Verify_employment();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                    fragmentManager.popBackStack();
                }
                fragmentTransaction.remove(fragment);
                fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();

                //   startActivity(new Intent(getActivity(), Verify_employment.class));
            }
        });
///////////////////////////////volley   ///////////////////////////////////////////////////////////////
        String url = Constants.baseUrl + "employment_verification_submit.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("jobin", "string response is : " + response);

                try {
                    JSONObject emp_verify_details = new JSONObject(response);
                    String result = emp_verify_details.getString("result");
                    verification_status = emp_verify_details.getString("verification_status");

                    if (result.equals("success")) {

                        String employment_details_string = emp_verify_details.getString("employment_details");

                        employment_details = new ArrayList<HashMap<String, String>>();
                        JSONArray employment_detailsJsonArray = new JSONArray(employment_details_string);
                        employment_details_length = employment_detailsJsonArray.length();

                        for (int i = 0; i < employment_details_length; i++) {
                            JSONObject obj = employment_detailsJsonArray.getJSONObject(i);

                            HashMap<String, String> temp = new HashMap<String, String>();
                            temp.put("random_code", obj.getString("emp_ver_tbl_row_referance_id"));
                            temp.put("employer_name", obj.getString("emp_ver_tbl_employer_name"));
                            temp.put("contact", obj.getString("emp_ver_tbl_contact"));
                            temp.put("employee_id", obj.getString("emp_ver_tbl_employee_id"));
                            temp.put("working_from", obj.getString("emp_ver_tbl_working_from"));
                            temp.put("worked_till", obj.getString("emp_ver_tbl_worked_till"));
                            temp.put("designation", obj.getString("emp_ver_tbl_designation"));
                            temp.put("currency", obj.getString("emp_ver_tbl_currency"));
                            temp.put("compensation", obj.getString("emp_ver_tbl_compensation"));
                            temp.put("location", obj.getString("emp_ver_tbl_location"));

                            temp.put("superviser_name", obj.getString("emp_ver_tbl_superviser_name"));
                            temp.put("superviser_contact", obj.getString("emp_ver_tbl_superviser_contact"));
                            temp.put("leaving_reason", obj.getString("emp_ver_tbl_leaving_reason"));

                            employment_details.add(temp);


                        }

                        Employment_List_View_Adapter adapter = new Employment_List_View_Adapter(getActivity(), employment_details);
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
                SharedPreferences prefs = getActivity().getSharedPreferences("Login_details", getActivity().MODE_PRIVATE);
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


                HashMap<String, String> extracted_emp_detail = employment_details.get(position);

          /*      Verify_Employment_Details ldf = new Verify_Employment_Details ();
                Bundle args = new Bundle();
                args.putString("verification_status", verification_status);
                args.putSerializable("extracted_emp_detail", extracted_emp_detail);
                ldf.setArguments(args);

//Inflate the fragment
              //  getFragmentManager().beginTransaction().add(R.id.container, ldf).commit();


*/

                android.support.v4.app.Fragment fragment = new Verify_Employment_Details();
                Bundle args = new Bundle();
                args.putString("verification_status", verification_status);
                args.putSerializable("extracted_emp_detail", extracted_emp_detail);

                fragment.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                    fragmentManager.popBackStack();
                }

                fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();


            }
        });

        return view;
    }

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_employment_list);
        lv = (ListView) findViewById(R.id.emp_verification_listview);

        verify_employment_list_add_employerBtn = (Button) findViewById(R.id.verify_employment_list_add_employerBtn);
        verify_employment_list_add_employerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Verify_employment.class));
            }
        });
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
                    verification_status = emp_verify_details.getString("verification_status");

                    if (result.equals("success")) {

                        String employment_details_string = emp_verify_details.getString("employment_details");

                        employment_details = new ArrayList<HashMap<String, String>>();
                        JSONArray employment_detailsJsonArray = new JSONArray(employment_details_string);
                        employment_details_length = employment_detailsJsonArray.length();

                        for (int i = 0; i < employment_details_length; i++) {
                            JSONObject obj = employment_detailsJsonArray.getJSONObject(i);

                            HashMap<String, String> temp = new HashMap<String, String>();
                            temp.put("random_code", obj.getString("emp_ver_tbl_row_referance_id"));
                            temp.put("employer_name", obj.getString("emp_ver_tbl_employer_name"));
                            temp.put("contact", obj.getString("emp_ver_tbl_contact"));
                            temp.put("employee_id", obj.getString("emp_ver_tbl_employee_id"));
                            temp.put("working_from", obj.getString("emp_ver_tbl_working_from"));
                            temp.put("worked_till", obj.getString("emp_ver_tbl_worked_till"));
                            temp.put("designation", obj.getString("emp_ver_tbl_designation"));
                            temp.put("currency", obj.getString("emp_ver_tbl_currency"));
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
                intent.putExtra("verification_status", verification_status);
                intent.putExtra("extracted_emp_detail", extracted_emp_detail);
                startActivity(intent);
            }
        });


    }
*/

}
