package com.synopsis.androidapp.synopsis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
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
 * Created by Kumar on 9/9/2016.
 */
public class Verification_Terms_And_Authorization extends Fragment {
    TextView terms_and_conditionsTV,registered_emailTV;
    CheckBox verification_terms_agreementCB;
    Button accepttermsBtn;
    String email,password;

    Button verify_submitBtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verification_terms_and_authorization, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences("Login_details", getActivity().MODE_PRIVATE);
        email = prefs.getString("email", "");
        password = prefs.getString("password", "");

        String agreeement="I have registered with synopsis solutions through one of the channel used to fill the details for credentials verification ( www.synopsissolutions.com / Synopsis APP ) hereby authorize Synopsis Solutions to investigate my credentials. I understand that Synopsis Solutions may utilize internal verification team or an outside firm or firms to assist it in checking such information, and I specifically authorize such an investigation by information services and outside entities of the company's choice. I also understand that I may withhold my permission and that in such a case, no investigation will be done, I am authorizing  after reading and understood the terms and conditions mentioned in the website www.synopsissolutions.com and when registered. I further authorize any individual, Institute, University, company, firm, corporation, or public agency (including the government and law enforcement agencies) to divulge any and all information, verbal or written, pertaining to me to Synopsis Solutions representatives, or assigned agencies related to both individually and collectively. I also knew that Synopsis Solutions acts as an mediator agency to get verified my credentials from the respective units. However Synopsis Solutions is not responsible for any errors occurred in providing details either by me or the verified company or the institution.<br> Authorization is valid till it gets verified all credentials or if it is required to do again or for any additional checks required or re-verification request in the future from the registered Email ID.<br> Does not require any signature as this is filled online, if you have any queries can be contacted to my Registered Email Id";

                terms_and_conditionsTV=(TextView)view.findViewById(R.id.terms_and_conditionsTV);
        registered_emailTV=(TextView)view.findViewById(R.id.registered_emailTV);
        registered_emailTV.setText(email);
        terms_and_conditionsTV.setText(Html.fromHtml(agreeement));
        verification_terms_agreementCB=(CheckBox)view.findViewById(R.id.verification_terms_agreementCB);
        verify_submitBtn=(Button)view.findViewById(R.id.verify_submitBtn);
        verify_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
                {
                    if (!verification_terms_agreementCB.isChecked()) {

                        Toast toast = Toast.makeText(getActivity(), "Please accept the agreement to proceed", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    } else {


///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////
                        String url = Constants.baseUrl + "verify_request.php";
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


                                        String message = "Your verification request is submitted.Our agent will contact you within 24 hours. Kindly send your identity,education and employment documents (if applicable) to verify@synopsissolutions.com ";


                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle("Message");
                                        builder.setMessage(message);

                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


                                                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                                    fragmentManager.popBackStack();
                                                }
                                            }
                                        });
                                        builder.create().show();


                                    } else {

                                        String message = " An error occured while submiting. Kindly try after sometime";


                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle("Message");
                                        builder.setMessage(message);

                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.create().show();


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


                                parameters.put("Action", "final_verify_request_form");


                                return parameters;
                            }
                        };
                        requestQueue.add(stringrequest);


                        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                                10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    }
                }

                else
                {
                    startActivity(new Intent(getActivity(),Internet_ErrorMessage.class));

                }




            }
        });
        return view;
    }
}
