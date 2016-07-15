package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by User on 7/15/2016.
 */
public class Terms_conditionsClass  extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions);
    }
    public void verify_emailfn(View view)
    {
        Toast.makeText(getApplicationContext(),"verify mail",Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(),LoginClass.class));
    }

}
