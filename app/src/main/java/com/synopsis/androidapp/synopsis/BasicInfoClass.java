package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * Created by User on 7/15/2016.
 */
public class BasicInfoClass extends Activity implements AdapterView.OnItemClickListener {
    public static final String Login_details = "Login_details";
    ImageButton selfyButton;
    private int year, month, day;

    private Calendar calendar;
    EditText datepickerBtnET, referorIdEt;
    AutoCompleteTextView autoCompView;

    RadioGroup genderRadioGroup;

    String dateofbirth, referorId, place, country, state, city, gender, email;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCQHYiVSoaKudLpualnCGZNBQ-yWnlyu5s";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_info);
        selfyButton = (ImageButton) findViewById(R.id.selfyButton);
        datepickerBtnET = (EditText) findViewById(R.id.datepickerBtnET);
        referorIdEt = (EditText) findViewById(R.id.referorIdET);

        SharedPreferences prefs = getSharedPreferences(Login_details, MODE_PRIVATE);
        email = prefs.getString("email", "");
        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radiogroup);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datepickerBtnET.setText("" + year + "/" + month + "/" + day);

        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(BasicInfoClass.this, R.layout.list_item));
        autoCompView.setOnItemClickListener(BasicInfoClass.this);


    }


    public void submitbasicinfofn(View view) {


        dateofbirth = datepickerBtnET.getText().toString().trim();
        referorId = referorIdEt.getText().toString().trim();


        place = autoCompView.getText().toString();
        Log.d("jobin", "works till here");

        List<String> place_list = Arrays.asList(place.split(","));
        int length = place_list.size();
        Log.d("jobin", "place passed to submit fn:country" + place_list.get(length - 1) + " state:" + place_list.get(length - 2) + " city: " + place_list.get(length - 3));

        country = place_list.get(length - 1).toString();
        state = place_list.get(length - 2).toString();
        city = place_list.get(length - 3).toString();

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        Log.d("jobin", "radio id:" + selectedId);
        // find the radiobutton by returned id
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        gender = radioButton.getText().toString();


        String url = Constants.baseUrl + "submit_basic_info.php";

//////////////////////////////volley starts  ///////////////////////////////////////////////////////////////
        RequestQueue requestQueue = Volley.newRequestQueue(BasicInfoClass.this);
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("jobin", "string response basic  info  is : " + response);

                try {
                    JSONObject person = new JSONObject(response);
                    String result = person.getString("result");
                    String error = person.getString("error");
                    if (result.equals("success")) {
                        startActivity(new Intent(getApplicationContext(), BasicInfoClass.class));
                    } else if (error.equals("user_exists")) {
                        Toast.makeText(getApplicationContext(), "your email Id is already registered. Kindly login", Toast.LENGTH_LONG).show();
                        finish();
                    }

                } catch (JSONException e) {
                    Log.d("jobin", "json errror:" + e);
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jobin", "error response is : " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("dateofbirth", dateofbirth);
                parameters.put("referorId", referorId);
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

//////////////////////////volley ends////////////////////////////////////////////////


    ////////////////////////choose from gallery or from camera option//////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d("jobin", "in photo activityresults");
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            Log.d("jobin", "in photo activityresults result ok");
            //Convert to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent I = new Intent(this, Camera_activity.class);
            I.putExtra("image", byteArray);
            startActivity(I);
        } else {
            Log.d("jobin", "in photo activityresults result not  ok");
        }
    }

    public void photo_actions(View v) {
        Log.d("jobin", "in photo actions");
        final AlertDialog alertDialog = new AlertDialog.Builder(BasicInfoClass.this).create();
        alertDialog.setTitle("Title");
        alertDialog.setMessage("Message");
        alertDialog.setButton("camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int CAMERA_PIC_REQUEST = 0;
                Intent camera_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, CAMERA_PIC_REQUEST);


            }

        });


        alertDialog.setButton2("gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), ImageUpload.class));
                    }


                }
        );
        alertDialog.show();

    }
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

            // arg1 = year
            // arg2 = month
            month = month + 1;
            // arg3 = day

            datepickerBtnET.setText("" + date + "/" + month + "/" + year);
        }
    };


    ///////////////////////////date picker ends////////////////////////////////////////////////////////////
///////////////////////////auto complete place starts/////////////////////////////////////////////////////

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);


        Toast.makeText(BasicInfoClass.this, str, Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            Log.e("jobin", "URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("jobin", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("jobin", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results

            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            Log.e("jobin", "in the try of predictions json array got is: " + predsJsonArray.toString());
            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                //  System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                ///     System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                Log.e("jobin", "description of predictions:" + predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e("jobin", "Cannot process JSON results", e);
        } catch (Exception e) {
            Log.e("jobin", "error in catch of predictions", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public android.widget.Filter getFilter() {
            android.widget.Filter filter = new android.widget.Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.

                        Log.e("jobin", "auto complete called");
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    ////////////////////////////auto complete place ends////////////////////////////////////////////////


}