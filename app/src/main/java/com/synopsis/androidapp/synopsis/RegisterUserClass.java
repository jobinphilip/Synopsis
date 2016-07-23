package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by User on 7/13/2016.
 */

public class RegisterUserClass extends Activity {
    public static final String Login_details = "Login_details";
    EditText  emailET, confirmEmailET, passwordET;
    String  email_string, confirm_email_string, password_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user_page);

        emailET = (EditText) findViewById(R.id.email_Et);
        confirmEmailET = (EditText) findViewById(R.id.confirm_email_Et);
        passwordET = (EditText) findViewById(R.id.passwordET);

    }

    public void termsfn(View view) {


        email_string = emailET.getText().toString().trim();
        confirm_email_string = confirmEmailET.getText().toString().trim();
        password_string = passwordET.getText().toString().trim();


        if ( email_string.matches("") || confirm_email_string.matches("") || password_string.matches("")) {
            Toast.makeText(getApplicationContext(), "Kindly fill all the mandatory fields", Toast.LENGTH_LONG).show();

        } else {
            if (!(email_string.matches(confirm_email_string))) {
                Toast.makeText(getApplicationContext(), "Email address do not match.", Toast.LENGTH_LONG).show();
            } else if (password_string.length() <= 5 || password_string.length() >= 12) {
                Toast.makeText(getApplicationContext(), "password length should be between 6 to 12 characters", Toast.LENGTH_LONG).show();
            } else {
                SharedPreferences.Editor editor = getSharedPreferences(Login_details, MODE_PRIVATE).edit();



                editor.putString("email", email_string);
                editor.putString("password", password_string);

                editor.commit();
                startActivity(new Intent(getApplicationContext(), Terms_conditionsClass.class));
            }

        }

     }
}
