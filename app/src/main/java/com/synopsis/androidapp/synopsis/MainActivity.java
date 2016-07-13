package com.synopsis.androidapp.synopsis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

    }
public void registerfn(View view)
{
    startActivity( new Intent(MainActivity.this, RegisterUserClass.class));
}
}
