package com.synopsis.androidapp.synopsis;

import android.app.AlertDialog;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class Dashboard_fragment extends Fragment {
    /*
    TextView nameTV, synopsis_idTV, companyTV, educationTV, designationTV, placeTV, emailTV, mobileTV,toolbar_creditsTv;
    CircleImageView profile_image;
    Bitmap profilebitmap = null;
    private int PICK_IMAGE_REQUEST = 1;
    private int CAMERA_PIC_REQUEST = 0;
    String email, password, url, image_base64string;
    RequestQueue requestQueue;
    public static final String Login_details = "Login_details";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=   inflater.inflate(R.layout.dashboard_contents_layout, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(Login_details, getActivity().MODE_PRIVATE);
        email = prefs.getString("email", "");
        password = prefs.getString("password", "");
        url = Constants.baseUrl + "dashboard_details.php";

        nameTV = (TextView)view. findViewById(R.id.dashboard_nameTV);
        synopsis_idTV = (TextView) view. findViewById(R.id.dashboard_synopsis_idTV);
        synopsis_idTV.setVisibility(View.INVISIBLE);
        companyTV = (TextView) view. findViewById(R.id.dashboard_companyTV);
        companyTV.setVisibility(View.INVISIBLE);
        educationTV = (TextView) view. findViewById(R.id.dashboard_educationTV);
        educationTV.setVisibility(View.INVISIBLE);
        designationTV = (TextView)view.  findViewById(R.id.dashboard_designationTV);
        designationTV.setVisibility(View.INVISIBLE);
        profile_image = (CircleImageView) view. findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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

        });


      //  toolbar_creditsTv=(TextView)view. findViewById(R.id.toolbar_creditsTv);
        placeTV = (TextView) view. findViewById(R.id.dashboard_placeTV);

        emailTV = (TextView) view. findViewById(R.id.dashboard_emailTV);
        mobileTV = (TextView)view.  findViewById(R.id.dashboard_mobileTV);


        ///////////////////////////////volley 5 by me  ///////////////////////////////////////////////////////////////

        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("jobin", "string response is : " + response);

                try {
                    JSONObject jobj = new JSONObject(response);
                    String result = jobj.getString("result");
                    String error = jobj.getString("error");
                    String img_url = jobj.getString("img_url");
                    String first_name = jobj.getString("first_name");
                    String synopsis_id = jobj.getString("synopsis_id");
                    String city = jobj.getString("city");
                    String state = jobj.getString("state");
                    String country = jobj.getString("country");
                    String place = city + ", " + state + ", " + country;
                    String mobile = jobj.getString("mobile");
                    String total_credits=jobj.getString("total_credits");
                    Log.d("jobin","credits at home:"+total_credits);

                    if (result.equals("success")) {
                        if (img_url.matches("")) {
                            Picasso.with(getActivity()).load(R.drawable.ic_menu_camera).into(profile_image);
                        } else {
                            Picasso.with(getActivity()).load(img_url).into(profile_image);
                        }
                        nameTV.setText(first_name);
                        toolbar_creditsTv.setText(total_credits);
                        placeTV.setText(place);
                        mobileTV.setText(mobile);
                        emailTV.setText(email);
                        if (!(synopsis_id.equals("null"))) {
                            synopsis_idTV.setVisibility(View.VISIBLE);
                            synopsis_idTV.setText(synopsis_id);
                        }

                    } else {
                        Toast.makeText(getActivity(), "Error fetching image", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {

                } catch (NullPointerException e) {
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
                parameters.put("email", email);
                parameters.put("password", password);
                parameters.put("Action", "image_download");


                return parameters;
            }
        };
        requestQueue.add(stringrequest);


        stringrequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


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
            prefs.edit().putString("class", "Dashboard").commit();

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
    */
}
