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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filterable;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
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
    /*  private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
      private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
      private static final String OUT_JSON = "/json";
      private static final String API_KEY = "AIzaSyCQHYiVSoaKudLpualnCGZNBQ-yWnlyu5s";
      */
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(BasicInfoClass.this);
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


                        //    image_base64string=jobj.getString("image");
                        //     Log.d("jobin","image:"+image_base64string);


                        if ((result.equals("success")) && img_url != null && !img_url.isEmpty() && !img_url.equals("null")) {
                            selfyButton.setVisibility(View.INVISIBLE);
                            basic_info_image.setVisibility(View.VISIBLE);

                            Log.d("jobin", "condition check is not right");
                            Picasso.with(BasicInfoClass.this).load(img_url).into(basic_info_image);


                        } else {

                            selfyButton.setVisibility(View.VISIBLE);
                            basic_info_image.setVisibility(View.INVISIBLE);
                        }


                    } catch (JSONException e) {
                        Log.d("jobin", "json errror:" + e);
                    }
                    calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH) + 1;
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    datepickerBtnET.setText("" + day + "/" + month + "/" + year);

                    autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

                    autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(BasicInfoClass.this, R.layout.list_item));
                    autoCompView.setOnItemClickListener(BasicInfoClass.this);

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
            Log.d("jobin", "null pointer");
            selfyButton.setVisibility(View.VISIBLE);
            basic_info_image.setVisibility(View.INVISIBLE);
        }


        Log.d("jobin", "on create complete");


    }


    public void submitbasicinfofn(View view) {

        Log.d("jobin", "submit basic info fn");
        dateofbirth = datepickerBtnET.getText().toString().trim();
        String year_ofbirth = dateofbirth.substring(dateofbirth.length() - 4, dateofbirth.length());
        if (Integer.parseInt(year_ofbirth) > year - 16) {

            Toast toast = Toast.makeText(getApplicationContext(), "Minimum age should be 16", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else {


            place = autoCompView.getText().toString();


            List<String> place_list = Arrays.asList(place.split(","));
            int length = place_list.size();

            country = place_list.get(length - 1).toString();
            state = place_list.get(length - 2).toString();
            city = place_list.get(length - 3).toString();

            int selectedId = genderRadioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            gender = radioButton.getText().toString();


            String url = Constants.baseUrl + "submit_basic_info.php";

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
        Log.d("jobin", "activityresults");

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            Log.d("jobin", "trying to load  camera to bitmap");

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Log.d("jobin", "ibitmap loaded");
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


            Intent I = new Intent(this, Camera_activity.class);
            I.putExtra("url_to_profile", url_to_profile);
            startActivity(I);
            finish();
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                Log.d("jobin", "trying to load  file chooder to bitmap");
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Log.d("jobin", "bitmap loaded");
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


                Intent I = new Intent(this, Camera_activity.class);
                I.putExtra("url_to_profile", url_to_profile);
                startActivity(I);
                finish();

            } catch (IOException e) {

                Log.d("jobin", "exception in setting bitmap to imageview:" + e.toString());
            }


        } else {
            Log.d("jobin", "in photo activityresults result not  ok");
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
        Log.d("jobin", "image resized");
        return Bitmap.createScaledBitmap(image, width, height, true);

    }

    /////////////////////////////resize image ends///////////////////////////////////
    ///////////////////////////date picker fn////////////////////////////////////////////////////////////

    public void date_pickerfn(View v) {
        Log.d("jobin", "date picker");
        showDialog(999);
    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        Log.d("jobin", "idate pick2");
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int date) {
            Log.d("jobin", "datepick3");
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
        Log.d("jobin", "item click");
        String str = (String) adapterView.getItemAtPosition(position);


    }
    /*

    public static ArrayList<String> autocomplete(String input) {
        Log.d("jobin", "auto complete");
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

                        Log.e("jobin", "google auto complete");
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
*/    ////////////////////////////auto complete place ends////////////////////////////////////////////////

    @Override
    public void onBackPressed() {


        Log.d("jobin", "back press");
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