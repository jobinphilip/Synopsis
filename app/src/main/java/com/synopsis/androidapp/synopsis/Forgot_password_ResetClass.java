package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
 * Created by Kumar on 9/13/2016.
 */
public class Forgot_password_ResetClass extends Activity {
    EditText usernameET, PassET, NewpassET, ConfirmPassET;
    String username, password, newpassword, confirm_password;
    Button resutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_reset_layout);

        usernameET = (EditText) findViewById(R.id.frgt_ResetPassUserIdET);
        PassET = (EditText) findViewById(R.id.frgt_ResetPass_passwordET);
        NewpassET = (EditText) findViewById(R.id.frgt_ResetPass_newPasswordET);
        ConfirmPassET = (EditText) findViewById(R.id.frgt_ResetPass_confirmPasswordET);
        resutBtn = (Button) findViewById(R.id.frgt_ResetPassBtn);
        resutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = usernameET.getText().toString().trim();
                password = PassET.getText().toString().trim();
                newpassword = NewpassET.getText().toString().trim();
                confirm_password = ConfirmPassET.getText().toString().trim();
                if(username.matches("")||password.matches("")||newpassword.matches("")||confirm_password.matches(""))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Kindly Fill All The Required Fields", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                else if(!(newpassword.matches(confirm_password)))
                {
                    ConfirmPassET.setError("Passwords Do Not Match");
                }
                else if(newpassword.length()<6||newpassword.length()>12)
                {
                    NewpassET.setError("Password Length Should Be Between 6 To 12 Characters");
                }
                else
                {
                    String url=Constants.baseUrl+"password_reset.php";
                    /////////////////////////////volley starts  ///////////////////////////////////////////////////////////////
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject person = new JSONObject(response);
                                String result = person.getString("result");

                                if (result.equals("success")) {
                                    String message = "Password Reset Successfully. Kindly Login";


                                    final AlertDialog.Builder builder = new AlertDialog.Builder(Forgot_password_ResetClass.this);
                                    builder.setTitle("Message");
                                    builder.setMessage(message);

                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(getApplicationContext(), Start_Application.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    builder.create().show();


                                }
                                else
                                {
                                    String message = "Wrong Credentials entered";


                                    final AlertDialog.Builder builder = new AlertDialog.Builder(Forgot_password_ResetClass.this);
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
                            parameters.put("old_pass", password);
                            parameters.put("new_pass", newpassword);

                            parameters.put("email", username);
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

    }
}
