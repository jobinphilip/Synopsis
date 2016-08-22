package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Kumar on 8/2/2016.
 */
public class Start_Application extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page_6);
    }

    public  void employer_intentFn(View v)
    {
        startActivity(new Intent(getApplicationContext(),Company_register.class));

    }



    public  void individual_intentFn(View v)
    {
        startActivity(new Intent(getApplicationContext(),LoginClass.class));

    }
}
