package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by User on 7/15/2016.
 */
public class BasicInfoClass extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_info);

    }
    public void submitbasicinfofn(View view)
    {
        startActivity(new Intent(getApplicationContext(),Dash_board.class));
    }
}
