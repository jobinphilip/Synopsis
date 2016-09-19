package com.synopsis.androidapp.synopsis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by Kumar on 8/23/2016.
 */
public class Nav_settingsFragment extends Fragment {
    EditText settings_old_pass_ET, settings_newPassET, settings_confirmPassET;
    Button save_passBtn, edit_profileBtn;
    String old_pass, new_pass, confirm_pass,email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_page, container, false);
        settings_old_pass_ET = (EditText) view.findViewById(R.id.setings_old_passET);
        settings_newPassET = (EditText) view.findViewById(R.id.settings_new_passET);
        settings_confirmPassET = (EditText) view.findViewById(R.id.settings_confirm_passET);
        save_passBtn = (Button) view.findViewById(R.id.settings_save_passBtn);
        SharedPreferences prefs =getActivity().getSharedPreferences("Login_details", getActivity().MODE_PRIVATE);
        email = prefs.getString("email", "");
        save_passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                old_pass = settings_old_pass_ET.getText().toString().trim();
                new_pass = settings_newPassET.getText().toString().trim();
                confirm_pass = settings_confirmPassET.getText().toString().trim();
                if(!new_pass.matches(confirm_pass))
                {
                    settings_confirmPassET.setError("passwords do not match. Kindly re enter");
                }
                else if(new_pass.length()<6||new_pass.length()>12)
                {
                    settings_newPassET.setError("Password Length should be atleast 6 characters");
                }
                else if(old_pass.matches("")||new_pass.matches("")||confirm_pass.matches(""))
                {
                    Toast toast= new Toast(getActivity());
                    toast.setText("Kindly fill all the required fields");
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                }
                else
                {
                    String url=Constants.baseUrl+"password_reset.php";
                    /////////////////////////////volley starts  ///////////////////////////////////////////////////////////////
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject person = new JSONObject(response);
                                String result = person.getString("result");

                                if (result.equals("success")) {
                                    String message = "Password reset successfully";


                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Message");
                                    builder.setMessage(message);

                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                                fragmentManager.popBackStack();
                                            }

                                        }
                                    });
                                    builder.create().show();

                                }
                                else
                                {
                                    String message = "Wrong Password entered";


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
                            }


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("old_pass", old_pass);
                            parameters.put("new_pass", new_pass);

                            parameters.put("email", email);
                            parameters.put("Action", "edit_password");


                            return parameters;
                        }
                    };
                    requestQueue.add(stringrequest);


                    stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                            10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                }


            }
        });
        edit_profileBtn = (Button) view.findViewById(R.id.settings_edit_profileBtn);
        edit_profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getActivity(),BasicInfoClass.class));

            }
        });


        return view;

    }
}
