package com.synopsis.androidapp.synopsis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_start_page);
    }



    public void loginfn(View view) {
        startActivity(new Intent(MainActivity.this, LoginClass.class));
    }

    public void registerfn(View view) {
        startActivity(new Intent(MainActivity.this, RegisterUserClass.class));
    }


}
