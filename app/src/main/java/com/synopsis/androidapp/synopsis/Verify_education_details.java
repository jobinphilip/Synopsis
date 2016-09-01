package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kumar on 9/1/2016.
 */
public class Verify_education_details extends Activity {
    EditText register_number_ET, college_nameET, universityET, percentageET, courseNameET, courseTypeET, verify_education_datepickerBtnET;
    Button eduverifysubmitbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_education);


        Intent intent = getIntent();
        HashMap<String, String> extracted_edu_detail = (HashMap<String, String>) intent.getSerializableExtra("extracted_edu_detail");
        Log.d("jobin", "extracted edu details are:" + extracted_edu_detail.toString());
        eduverifysubmitbtn = (Button) findViewById(R.id.education_verificationSubmitBtn);
        eduverifysubmitbtn.setVisibility(View.INVISIBLE);
        register_number_ET = (EditText) findViewById(R.id.regNoET);
        college_nameET = (EditText) findViewById(R.id.collegeET);
        universityET = (EditText) findViewById(R.id.universityET);

        percentageET = (EditText) findViewById(R.id.percentET);
        courseNameET = (EditText) findViewById(R.id.courseET);
        courseTypeET = (EditText) findViewById(R.id.courseTypeET);
        verify_education_datepickerBtnET = (EditText) findViewById(R.id.verify_education_datepickerBtnET);


        register_number_ET.setText(extracted_edu_detail.get("register_number").toString());

        college_nameET.setText(extracted_edu_detail.get("collegename").toString());

        universityET.setText(extracted_edu_detail.get("university").toString());

        percentageET.setText(extracted_edu_detail.get("percentage").toString());

        courseNameET.setText(extracted_edu_detail.get("course_name").toString());

        courseTypeET.setText(extracted_edu_detail.get("course_type").toString());

        verify_education_datepickerBtnET.setText(extracted_edu_detail.get("month_and_year_of_pass").toString());


// edu_ver_tbl_register_number, edu_ver_tbl_college_name,edu_ver_tbl_university_name,
// edu_ver_tbl_course_name,edu_ver_tbl_course_type,edu_ver_tbl_month_and_year_of_pass,
// edu_ver_tbl_gpa_percent FROM education_verification_table WHERE unique_id='".$unique_id."' ");

    }
}
