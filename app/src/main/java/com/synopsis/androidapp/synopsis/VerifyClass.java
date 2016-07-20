package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by User on 7/15/2016.
 */
public class VerifyClass extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_page);
    }
    public   void identity_verify_fn (View view)
    {
        startActivity(new Intent(getApplicationContext(),Verify_Identity.class));
    }
    public   void education_verifiy_fn (View view)
    {
        startActivity(new Intent(getApplicationContext(),Verify_educaton.class));
    }

    public   void employment_verify_fn (View view)
    {
        startActivity(new Intent(getApplicationContext(),Verify_employment.class));
    }

    public   void submit_verification_fn (View view)
    {
      //  startActivity(new Intent(getApplicationContext(),Verify_Identity.class));
    }


}