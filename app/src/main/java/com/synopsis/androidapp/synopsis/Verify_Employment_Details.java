package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

/**
 * Created by Kumar on 9/2/2016.
 */
public class Verify_Employment_Details extends Activity {
    EditText employment_verification_employerET, employment_verification_employer_contact, employment_verification_employee_id, verify_emp_joindateET, verify_emp_resigndateET, employment_verification_designationET, employment_verification_compensationET, verify_employent_autoCompleteTextViewET, employment_verification_supervisor_nameET, employment_verification_supervisor_contactET, employment_verification_reason_for_leavingET;
    Button verify_employment_submitBtn;

    /* , employment_verification_employee_id,
    verify_emp_joindateET, verify_emp_resigndateET, employment_verification_designation,
    employment_verification_compensation,verify_employent_autoCompleteTextView,
    employment_verification_supervisor_name,employment_verification_supervisor_contact,employment_verification_reason_for_leaving;
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_employment_details);


        Intent intent = getIntent();
        HashMap<String, String> extracted_emp_detail = (HashMap<String, String>) intent.getSerializableExtra("extracted_emp_detail");
        Log.d("jobin", "extracted emp details are:" + extracted_emp_detail.toString());
        verify_employment_submitBtn = (Button) findViewById(R.id.verify_employment_submitBtn);
        verify_employment_submitBtn.setVisibility(View.INVISIBLE);

        employment_verification_employerET = (EditText) findViewById(R.id.employment_verification_employerET);
        employment_verification_employer_contact = (EditText) findViewById(R.id.employment_verification_employer_contact);
        employment_verification_employee_id = (EditText) findViewById(R.id.employment_verification_employee_id);

        verify_emp_joindateET = (EditText) findViewById(R.id.verify_emp_joindateET);
        verify_emp_resigndateET = (EditText) findViewById(R.id.verify_emp_resigndateET);
        employment_verification_designationET = (EditText) findViewById(R.id.employment_verification_designation);
        employment_verification_compensationET = (EditText) findViewById(R.id.employment_verification_compensation);
        verify_employent_autoCompleteTextViewET = (EditText) findViewById(R.id.verify_employent_autoCompleteTextView);
        employment_verification_supervisor_nameET = (EditText) findViewById(R.id.employment_verification_supervisor_name);
        employment_verification_supervisor_contactET = (EditText) findViewById(R.id.employment_verification_supervisor_contact);
        employment_verification_reason_for_leavingET = (EditText) findViewById(R.id.employment_verification_reason_for_leaving);


        employment_verification_employerET.setText(extracted_emp_detail.get("employer_name").toString());

        employment_verification_employer_contact.setText(extracted_emp_detail.get("contact").toString());

        employment_verification_employee_id.setText(extracted_emp_detail.get("employee_id").toString());

        verify_emp_joindateET.setText(extracted_emp_detail.get("working_from").toString());

        verify_emp_resigndateET.setText(extracted_emp_detail.get("worked_till").toString());

        employment_verification_designationET.setText(extracted_emp_detail.get("designation").toString());

        employment_verification_compensationET.setText(extracted_emp_detail.get("compensation").toString());
        verify_employent_autoCompleteTextViewET.setText(extracted_emp_detail.get("location").toString());
        employment_verification_supervisor_nameET.setText(extracted_emp_detail.get("superviser_name").toString());
        employment_verification_supervisor_contactET.setText(extracted_emp_detail.get("superviser_contact").toString());
        employment_verification_reason_for_leavingET.setText(extracted_emp_detail.get("leaving_reason").toString());


    }
}
