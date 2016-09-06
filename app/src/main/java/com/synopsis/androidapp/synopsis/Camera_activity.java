package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
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

import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;


public class Camera_activity extends Activity {
    ImageView preview;
    CropImageView arthurhub_imageView;
    Bitmap bitmap, bitmap2, cropped;

    public static final String Login_details = "Login_details";
    String UPLOAD_URL = Constants.baseUrl + "image_upload.php";
    Button crop_button, upload_image_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadphoto);
        arthurhub_imageView = (CropImageView) findViewById(R.id.arthurhub_imagecropper);
        preview = (ImageView) findViewById(R.id.croppedimageview);
        preview.setVisibility(View.INVISIBLE);

        String url_to_profile_picutre = getIntent().getStringExtra("url_to_profile");
        Uri file_url = Uri.fromFile(new File(url_to_profile_picutre));
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file_url);
        } catch (IOException e) {
            e.printStackTrace();
        }


        crop_button = (Button) findViewById(R.id.cropBtn);
        upload_image_button = (Button) findViewById(R.id.buttonUpload);
        upload_image_button.setVisibility(View.INVISIBLE);
        arthurhub_imageView.setImageBitmap(bitmap);


        upload_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    uploadImage();


                SharedPreferences prefs = getSharedPreferences(Login_details, MODE_PRIVATE);
                final String email = prefs.getString("email", "");

                //Showing the progress dialog

                final ProgressDialog loading = ProgressDialog.show(Camera_activity.this, "Uploading...", "Please wait...", false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                loading.dismiss();
                                try {
                                    JSONObject resultobj = new JSONObject(s);
                                    String result = resultobj.getString("result");


                                    if (result.equals("success")) {

                                        SharedPreferences prefs = Camera_activity.this.getSharedPreferences("image_processing_class", Context.MODE_PRIVATE);
                                        String prev_class_name = prefs.getString("class", "");

                                        if (prev_class_name.matches("BasicInfo")) {

                                            Intent I = new Intent(getApplicationContext(), BasicInfoClass.class);
                                            startActivity(I);
                                            finish();
                                        } else {
                                            Intent I = new Intent(getApplicationContext(), Dash_board.class);
                                            startActivity(I);
                                        }


                                    } else {

                                        Toast toast = Toast.makeText(getApplicationContext(), "There was an unexpected error. Kindly try again", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                                        toast.show();

                                        finish();
                                    }

                                } catch (JSONException e) {
                                    Log.d("jobin", "error uploading image");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                loading.dismiss();


                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        String image = getStringImage(cropped);
                        Map<String, String> params = new Hashtable<String, String>();
                        params.put("image", image);
                        params.put("email", email);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(Camera_activity.this);
                requestQueue.add(stringRequest);


            }
        });
    }



    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    /////////////////////////crop image/////////////////////////////////////
    public void cropfn(View view) {
        cropped = arthurhub_imageView.getCroppedImage();
        preview.setImageBitmap(cropped);
        preview.setVisibility(View.VISIBLE);
        arthurhub_imageView.setVisibility(View.INVISIBLE);

        upload_image_button.setVisibility(View.VISIBLE);
        crop_button.setVisibility(View.INVISIBLE);

    }
    ///////////////////////////crop image ends///////////////////////////////////


}
