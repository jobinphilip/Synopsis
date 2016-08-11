package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Kumar on 7/29/2016.
 */
public class Camera_activity extends Activity{
    ImageView imageview;
    Bitmap bitmap,bitmap2;
    Button upload_image,choose_image;
    public static final String Login_details = "Login_details";
   String UPLOAD_URL=Constants.baseUrl+"image_upload.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadphoto);



imageview=(ImageView)findViewById(R.id.imageView);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
         bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        bitmap2=getResizedBitmap(bitmap,500);
        imageview.setImageBitmap(bitmap);
        choose_image=(Button)findViewById(R.id.buttonChoose);
        choose_image.setVisibility(View.INVISIBLE);
        upload_image=(Button)findViewById(R.id.buttonUpload);
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });


    }



    private void uploadImage() {
        SharedPreferences prefs = getSharedPreferences(Login_details, MODE_PRIVATE);
        final String email = prefs.getString("email", "");

//Showing the progress dialog

        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("jobin", "response from server" + s);
                        //Disimissing the progress dialog
                        bitmap.recycle();
                        bitmap2.recycle();
                        loading.dismiss();
                        try {
                            JSONObject resultobj = new JSONObject(s);
                            String result = resultobj.getString("result");
                            String error = resultobj.getString("error");
                            String path = resultobj.getString("path");
                            if (result.equals("success")) {
                                Intent I= new Intent(getApplicationContext(),BasicInfoClass.class);
                                I.putExtra("path",path);
                                startActivity(I);
                            } else {
                                Toast.makeText(getApplicationContext(), "There was an unexpected error. Kindly try again", Toast.LENGTH_LONG).show();
                                finish();
                            }

                        } catch (JSONException e) {
                            Log.d("jobin", "json errror:" + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        Log.d("jobin", volleyError.toString());
                        loading.dismiss();



                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap2);

                //Getting Image Name


                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("image", image);
                params.put("email", email);
                Log.d("jobin", "parameters added to upload image");
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

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

}
