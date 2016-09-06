package com.synopsis.androidapp.synopsis;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


import de.hdodenhof.circleimageview.CircleImageView;

public class Dash_board extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView nameTV, synopsis_idTV, companyTV, educationTV, designationTV, placeTV, emailTV, mobileTV;
    CircleImageView profile_image;
    Bitmap profilebitmap = null;
    private int PICK_IMAGE_REQUEST = 1;
    private int CAMERA_PIC_REQUEST = 0;
    String email, password, url, image_base64string;
    RequestQueue requestQueue;
    public static final String Login_details = "Login_details";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(Login_details, MODE_PRIVATE);
        email = prefs.getString("email", "");
        password = prefs.getString("password", "");
        url = Constants.baseUrl + "dashboard_details.php";


        requestQueue = Volley.newRequestQueue(Dash_board.this);

        setContentView(R.layout.home_layout);
        nameTV = (TextView) findViewById(R.id.dashboard_nameTV);
        synopsis_idTV = (TextView) findViewById(R.id.dashboard_synopsis_idTV);
        synopsis_idTV.setVisibility(View.INVISIBLE);
        companyTV = (TextView) findViewById(R.id.dashboard_companyTV);
        companyTV.setVisibility(View.INVISIBLE);
        educationTV = (TextView) findViewById(R.id.dashboard_educationTV);
        educationTV.setVisibility(View.INVISIBLE);
        designationTV = (TextView) findViewById(R.id.dashboard_designationTV);
        designationTV.setVisibility(View.INVISIBLE);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);

        placeTV = (TextView) findViewById(R.id.dashboard_placeTV);

        emailTV = (TextView) findViewById(R.id.dashboard_emailTV);
        mobileTV = (TextView) findViewById(R.id.dashboard_mobileTV);


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
                    if (result.equals("success")) {
                        if (img_url.matches("")) {
                            Picasso.with(Dash_board.this).load(R.drawable.ic_menu_camera).into(profile_image);
                        } else {
                            Picasso.with(Dash_board.this).load(img_url).into(profile_image);
                        }
                        nameTV.setText(first_name);

                        placeTV.setText(place);
                        mobileTV.setText(mobile);
                        emailTV.setText(email);
                        if (!(synopsis_id.equals("null"))) {
                            synopsis_idTV.setVisibility(View.VISIBLE);
                            synopsis_idTV.setText(synopsis_id);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Error fetching image", Toast.LENGTH_LONG).show();
                        finish();
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


        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        Toolbar bottom_toolbar = (Toolbar) findViewById(R.id.bottom_toolbar);

        setSupportActionBar(toolbar1);


        bottom_toolbar.inflateMenu(R.menu.toolbar_menu);


        //toolbar2 menu items CallBack listener
        bottom_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                if (arg0.getItemId() == R.id.verify_user) {
                    startActivity(new Intent(getApplicationContext(), VerifyClass.class));
                } else if (arg0.getItemId() == R.id.credits) {
                    startActivity(new Intent(getApplicationContext(), Credits.class));
                } else if (arg0.getItemId() == R.id.chat) {
                    String chaturl = "https://chatserver5.comm100.com/ChatWindow.aspx?siteId=107734&planId=2079&visitType=1&byHref=1&partnerId=-1";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(chaturl));
                    startActivity(i);
                }
                return false;
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void camera_activityfn(View v) {
        final AlertDialog alertDialog = new AlertDialog.Builder(Dash_board.this).create();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            String message = "Exit Synopsis?";


            final AlertDialog.Builder builder = new AlertDialog.Builder(Dash_board.this);
            builder.setTitle("Warning");
            builder.setMessage(message);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            builder.create().show();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        android.support.v4.app.Fragment fragment = null;
        if (id == R.id.nav_my_synopsis) {
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();

        }
        else if(id == R.id.nav_share)
        {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/html");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>This is the text that will be shared.</p>"));
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
        else {
            switch (id) {

                case R.id.nav_about_us: {
                    fragment = new Nav_about_usFragment();
                    break;
                }
                case R.id.nav_settings: {
                    fragment = new Nav_settingsFragment();
                    break;
                }
                case R.id.nav_faq: {
                    fragment = new Nav_FaqFragment();
                    break;
                } case R.id.nav_contact_us: {
                    fragment = new Nav_ContactUsFragment();
                    break;
                }
                case R.id.nav_rate_us: {
                    fragment = new Nav_Rate_usFragment();
                    break;
                }
                case R.id.nav_testimonial: {
                    fragment = new Nav_Testimonial_Fragment();
                    break;
                }
                case R.id.nav_share: {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    break;
                }
                case R.id.nav_logout: {
                    fragment = new Nav_LogoutFragment();
                    break;
                }


                default:
                    fragment = new Nav_about_usFragment();
                    break;
            }
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.dashboard_content_layout, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
