package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Kumar on 9/16/2016.
 */
public class Internet_ErrorMessage extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet_connection_error);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
