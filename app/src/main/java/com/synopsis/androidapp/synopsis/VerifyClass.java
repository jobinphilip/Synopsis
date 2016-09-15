package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class VerifyClass extends Fragment {
    String email, password;
    Button verify_main_id_verify_btn, verify_main_edu_verifyBtn, verify_main_emp_verifyBtn, verify_main_download_emailBtn, verify_main_proceed_to_verifyBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verify_page, container, false);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("Login_details", this.getActivity().MODE_PRIVATE);
        email = prefs.getString("email", "");
        password = prefs.getString("password", "");
        verify_main_id_verify_btn = (Button) view.findViewById(R.id.verify_main_id_verify_btn);


        verify_main_id_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   startActivity(new Intent(getActivity().getApplicationContext(), Verify_Identity.class));
                android.support.v4.app.Fragment fragment = new Verify_Identity();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();
                //  fragmentTransaction.commit();
            }
        });

        verify_main_proceed_to_verifyBtn = (Button) view.findViewById(R.id.verify_main_proceed_to_verifyBtn);
        verify_main_proceed_to_verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.Fragment fragment = new Upload_verification_docsFragment();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();
                ;
                //   fragmentTransaction.commit();

            }
        });
        verify_main_edu_verifyBtn = (Button) view.findViewById(R.id.verify_main_edu_verifyBtn);
        verify_main_edu_verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///////////////////////////////volley//////////////////////////////
                String url = Constants.baseUrl + "education_verification.php";
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("jobin", "string response is : " + response);

                        try {
                            JSONObject edu_verify_check = new JSONObject(response);
                            String result = edu_verify_check.getString("result");

                            if (result.equals("success")) {

                                android.support.v4.app.Fragment fragment = new Verify_Education_List();
                                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                    fragmentManager.popBackStack();
                                }
                                fragmentTransaction.remove(fragment);
                                fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();
                          //      Intent I = new Intent(getActivity().getApplicationContext(), Verify_Education_List.class);
                             //   startActivity(I);


                            } else {
                            //    Intent I = new Intent(getActivity().getApplicationContext(), Verify_educaton.class);
                              //  startActivity(I);
                                android.support.v4.app.Fragment fragment = new Verify_educaton();
                                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                             /*   for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                    fragmentManager.popBackStack();
                                }
                                */
                                fragmentTransaction.remove(fragment);
                                fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();
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


                        parameters.put("Action", "check_education");


                        return parameters;
                    }
                };
                requestQueue.add(stringrequest);


                stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            }
        });
        verify_main_emp_verifyBtn = (Button) view.findViewById(R.id.verify_main_emp_verifyBtn);
        verify_main_emp_verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //////////////////////////////volley ///////////////////////////////////////////////////////////////
                String url = Constants.baseUrl + "employment_verification_submit.php";
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("jobin", "string response is : " + response);

                        try {
                            JSONObject person = new JSONObject(response);
                            String result = person.getString("result");

                            if (result.equals("success")) {

                                android.support.v4.app.Fragment fragment = new Verify_Employment_List();
                                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                    fragmentManager.popBackStack();
                                }
                                fragmentTransaction.remove(fragment);
                                fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();


                                //  Intent I = new Intent(getActivity().getApplicationContext(), Verify_Employment_List.class);

//
                                //     startActivity(I);
                            } else {


                                android.support.v4.app.Fragment fragment = new Verify_employment();
                                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                    fragmentManager.popBackStack();
                                }
                                fragmentTransaction.remove(fragment);
                                fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();

                                //  Intent I = new Intent(getActivity().getApplicationContext(), Verify_employment.class);


                                //startActivity(I);
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


                        parameters.put("Action", "check_employment");


                        return parameters;
                    }
                };
                requestQueue.add(stringrequest);


                stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            }
        });


        verify_main_download_emailBtn = (Button) view.findViewById(R.id.verify_main_download_emailBtn);
        verify_main_download_emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog ad = new AlertDialog.Builder(getActivity())
                        .create();
                ad.setCancelable(true);
                ad.setTitle("Select an option");
                ad.setMessage("choose to download form or email to your email id");
                ad.setButton("download", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String chaturl = "http://synopsissolutions.com/mobile_api/Individual%20Background%20Verification%20Form%202016.docx";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(chaturl));
                        startActivity(i);


                    }

                });

                ad.setButton2("Email", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


///////////////////////////////volley //////////////////////////////////////////////////////////////
                                String url = Constants.baseUrl + "attach_verify_form.php";
                                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("jobin", "string response is : " + response);

                                        try {
                                            JSONObject person = new JSONObject(response);
                                            String result = person.getString("result");

                                            if (result.equals("success")) {

                                                Toast toast = Toast.makeText(getActivity(), "Kindly check your registered Email ", Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                                                toast.show();


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
                                        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("Login_details", getActivity().getApplicationContext().MODE_PRIVATE);
                                        final String email = prefs.getString("email", "");
                                        parameters.put("email", email);


                                        parameters.put("Action", "Email_form_to_user");


                                        return parameters;
                                    }
                                };
                                requestQueue.add(stringrequest);


                                stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                                        10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                            }
                        }


                );
                ad.show();


            }
        });

        ///////////////////////////////volley//////////////////////////////
        String url = Constants.baseUrl + "check_pending_verifications.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("jobin", "string response is : " + response);

                try {
                    JSONObject edu_verify_check = new JSONObject(response);
                    String identity_verification_status = edu_verify_check.getString("identity_verification_status");
                    String employment_verification_status = edu_verify_check.getString("employment_verification_status");
                    String education_verification_status = edu_verify_check.getString("education_verification_status");

                    if (identity_verification_status.equals("complete")) {

                        verify_main_id_verify_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.nav_my_synopsis, 0, R.drawable.tick, 0);

                    }

                    if (employment_verification_status.equals("complete")) {

                        verify_main_emp_verifyBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.drawable_identity, 0, R.drawable.tick, 0);

                    }
                    if (education_verification_status.equals("complete")) {


                        verify_main_edu_verifyBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.drawable_education, 0, R.drawable.tick, 0);
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


                parameters.put("Action", "check_pending_verifications");


                return parameters;
            }
        };
        requestQueue.add(stringrequest);


        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return view;
    }
}

