package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

//
public class BasicInfoClass extends Activity implements AdapterView.OnItemClickListener {
    public static final String Login_details = "Login_details";
    ImageButton selfyButton;
    private int year, month, day;
    CircleImageView basic_info_image;
    private Calendar calendar;
    EditText datepickerBtnET;
    AutoCompleteTextView autoCompView;

    RadioGroup genderRadioGroup;

    String dateofbirth, place, country, state, city, gender, email, password;

    private int PICK_IMAGE_REQUEST = 1;
    private int CAMERA_PIC_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.basic_info);


        selfyButton = (ImageButton) findViewById(R.id.selfyButton);
        datepickerBtnET = (EditText) findViewById(R.id.datepickerBtnET);

        String url = Constants.baseUrl + "basic_info_image.php";
        SharedPreferences prefs = getSharedPreferences(Login_details, MODE_PRIVATE);

        email = prefs.getString("email", "");
        password = prefs.getString("password", "");
        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radiogroup);
        basic_info_image = (CircleImageView) findViewById(R.id.basic_info_image);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
       // datepickerBtnET.setText("" + day + "/" + month + "/" + year);

        RequestQueue requestQueue2 = Volley.newRequestQueue(BasicInfoClass.this);


        if(CheckNetwork.isInternetAvailable(getApplicationContext())) //returns true if internet available
        {

            try {
                ///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////

                StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jobj = new JSONObject(response);
                            String result = jobj.getString("result");
                            String error = jobj.getString("error");
                            String img_url = jobj.getString("img_url");
                            if ((result.equals("success")) && img_url != null && !img_url.isEmpty() && !img_url.equals("null")) {
                                selfyButton.setVisibility(View.INVISIBLE);
                                basic_info_image.setVisibility(View.VISIBLE);
                                Log.d("jobin", "condition check is not right");
                                Picasso.with(BasicInfoClass.this).invalidate(img_url);
                                Picasso.with(BasicInfoClass.this).load(img_url) .memoryPolicy(MemoryPolicy.NO_CACHE )
                                        .networkPolicy(NetworkPolicy.NO_CACHE).into(basic_info_image);
                            } else {

                                selfyButton.setVisibility(View.VISIBLE);
                                basic_info_image.setVisibility(View.INVISIBLE);
                            }

                        } catch (JSONException e) {
                            Log.d("jobin", "json errror:" + e);
                        }

                        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(BasicInfoClass.this, R.layout.list_item));
                        autoCompView.setOnItemClickListener(BasicInfoClass.this);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("email", email);
                        parameters.put("password", password);
                        parameters.put("Action", "image_download");
                        return parameters;
                    }
                };
                requestQueue2.add(stringrequest);
                stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            } catch (NullPointerException e) {

                selfyButton.setVisibility(View.VISIBLE);
                basic_info_image.setVisibility(View.INVISIBLE);
            }

        }
        else
        {
            startActivity(new Intent(getApplicationContext(),Internet_ErrorMessage.class));
        }


    }

    public void submitbasicinfofn(View view) {

        SharedPreferences prefs = getSharedPreferences(Login_details, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.remove("Verify_tutorial_visited");
        }
        catch (NullPointerException e)
        {

        }

       editor.putBoolean("Verify_tutorial_visited",false);// this value is reset to true on visiting the verify tutorial fragment to avoid further visits of the same.
        editor.commit();
        dateofbirth = datepickerBtnET.getText().toString().trim();


            place = autoCompView.getText().toString();
            List<String> place_list = Arrays.asList(place.split(","));
            int length = place_list.size();
        String year_ofbirth = null;
        if (!dateofbirth.matches("")) {
            year_ofbirth = dateofbirth.substring(dateofbirth.length() - 4, dateofbirth.length());


        }

        if (length <= 2) {
            Toast toast = Toast.makeText(getApplicationContext(), "Kindly select a city name or place name with more details", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else if (dateofbirth.matches("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Date of Birth cannot be blank", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else if (Integer.parseInt(year_ofbirth) > year - 16) {

            Toast toast = Toast.makeText(getApplicationContext(), "Minimum age should be 16", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else {


            country = place_list.get(length - 1).toString();
            state = place_list.get(length - 2).toString();
            city = place_list.get(length - 3).toString();


            int selectedId = genderRadioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            gender = radioButton.getText().toString();


            String url = Constants.baseUrl + "submit_basic_info.php";


            if(CheckNetwork.isInternetAvailable(getApplicationContext())) //returns true if internet available
            {

//////////////////////////////volley starts  ///////////////////////////////////////////////////////////////
                RequestQueue requestQueue = Volley.newRequestQueue(BasicInfoClass.this);
                StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject person = new JSONObject(response);
                            String result = person.getString("result");
                            String error = person.getString("error");
                            if (result.equals("success")) {
                                startActivity(new Intent(getApplicationContext(), Dash_board.class));
                                finish();
                            }

                        } catch (JSONException e) {
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("dateofbirth", dateofbirth);

                        parameters.put("gender", gender);
                        parameters.put("country", country);
                        parameters.put("state", state);
                        parameters.put("city", city);
                        parameters.put("email", email);
                        parameters.put("Action", "basic_info_form");


                        return parameters;
                    }
                };
                requestQueue.add(stringrequest);


                stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }
            else
            {
                startActivity(new Intent(getApplicationContext(),Internet_ErrorMessage.class));
                finish();
            }

        }
    }
//////////////////////////volley ends////////////////////////////////////////////////
///////////////////////////image opoerations start/////////////////////////////////

    public void photo_actions(View v) {

        final AlertDialog alertDialog = new AlertDialog.Builder(BasicInfoClass.this).create();
        alertDialog.setTitle("Title");
        alertDialog.setMessage("Message");
        alertDialog.setButton("camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, CAMERA_PIC_REQUEST);


            }

        });
        alertDialog.setButton2("gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    }
                }
        );
        alertDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {


            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Bitmap Bitmap2 = getResizedBitmap(bitmap, 500);


            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/synopsis");
            myDir.mkdirs();
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String profilename = "Image-" + n + ".jpg";
            File file = new File(myDir, profilename);
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                Bitmap2.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            String url_to_profile = root + "/synopsis/" + profilename;

            SharedPreferences prefs = this.getSharedPreferences(
                    "image_processing_class", Context.MODE_PRIVATE);
            prefs.edit().putString("class", "BasicInfo").commit();
            Intent I = new Intent(this, Camera_activity.class);

            I.putExtra("url_to_profile", url_to_profile);
            startActivity(I);
            finish();
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap Bitmap2 = getResizedBitmap(bitmap, 300);


                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/synopsis");
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String profilename = "Image-" + n + ".jpg";
                File file = new File(myDir, profilename);
                if (file.exists()) file.delete();
                try {

                    FileOutputStream out = new FileOutputStream(file);
                    Bitmap2.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }


                SharedPreferences prefs = this.getSharedPreferences(
                        "image_processing_class", Context.MODE_PRIVATE);
                prefs.edit().putString("class", "BasicInfo").commit();

                String url_to_profile = root + "/synopsis/" + profilename;
                Intent I = new Intent(this, Camera_activity.class);
                I.putExtra("url_to_profile", url_to_profile);


                startActivity(I);
                finish();

            } catch (IOException e) {

                Log.d("jobin", "exception in setting bitmap to imageview:" + e.toString());
            }


        }
    }

    ////////////////////////choose from gallery or from camera ends//////////////////////
    ///////////////////////////resize image/////////////////////////////////////
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }

    /////////////////////////////resize image ends///////////////////////////////////
    ///////////////////////////date picker fn////////////////////////////////////////////////////////////

    public void date_pickerfn(View v) {
        showDialog(999);
    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int date) {
            month = month + 1;
            datepickerBtnET.setText("" + date + "/" + month + "/" + year);
        }
    };
    ///////////////////////////date picker ends////////////////////////////////////////////////////////////
///////////////////////////auto complete place starts/////////////////////////////////////////////////////

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);


    }

    @Override
    public void onBackPressed() {
        String message = "Exit Synopsis?";
        final AlertDialog.Builder builder = new AlertDialog.Builder(BasicInfoClass.this);
        builder.setTitle("Warning");
        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });
        builder.create().show();

    }
}