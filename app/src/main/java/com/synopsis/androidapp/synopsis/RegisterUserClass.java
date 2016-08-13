package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by User on 7/13/2016.
 */

public class RegisterUserClass extends Activity {
    public static final String Login_details = "Login_details";
    EditText  emailET, confirmEmailET, passwordET,first_nameET,last_nameET,phoneET;
    String firstname_string,lastname_string, email_string, confirm_email_string, password_string,phone_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user_page);
        first_nameET= (EditText) findViewById(R.id.firstname_ET);
        last_nameET= (EditText) findViewById(R.id.last_nameET);
phoneET=(EditText)findViewById(R.id.phone_ET);
        emailET = (EditText) findViewById(R.id.email_Et);
        confirmEmailET = (EditText) findViewById(R.id.confirm_email_Et);
        passwordET = (EditText) findViewById(R.id.passwordET);

    }

    public void termsfn(View view) {


        email_string = emailET.getText().toString().trim();
        confirm_email_string = confirmEmailET.getText().toString().trim();
        password_string = passwordET.getText().toString().trim();
        firstname_string= first_nameET.getText().toString().trim();
        lastname_string = last_nameET.getText().toString().trim();
        phone_string=phoneET.getText().toString().trim();

        if ( email_string.matches("") || confirm_email_string.matches("") || password_string.matches("")|| firstname_string.matches("")|| lastname_string.matches("")) {
            Toast.makeText(getApplicationContext(), "Kindly fill all the mandatory fields", Toast.LENGTH_LONG).show();

        } else {
            if (!(email_string.matches(confirm_email_string))) {
                Toast.makeText(getApplicationContext(), "Email address do not match.", Toast.LENGTH_LONG).show();
            } else if (password_string.length() <= 5 || password_string.length() >= 12) {
                Toast.makeText(getApplicationContext(), "password length should be between 6 to 12 characters", Toast.LENGTH_LONG).show();
            }
            else {
                SharedPreferences.Editor editor = getSharedPreferences(Login_details, MODE_PRIVATE).edit();



                editor.putString("email", email_string);
                editor.putString("password", password_string);
                editor.putString("first_name", firstname_string);
                editor.putString("last_name", lastname_string);
                editor.putString("mobile", phone_string);


                editor.commit();
                startActivity(new Intent(getApplicationContext(), Terms_conditionsClass.class));





            }

        }

     }
}
