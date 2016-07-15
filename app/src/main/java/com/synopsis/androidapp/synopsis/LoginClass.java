package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by User on 7/15/2016.
 */
public class LoginClass extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
    }
    public void loginfn(View view)
    {
        startActivity(new Intent(getApplicationContext(),BasicInfoClass.class));

    }
    public void registerfn(View view)
    {
        startActivity(new Intent(getApplicationContext(),RegisterUserClass.class));

    }

}
