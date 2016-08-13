package com.synopsis.androidapp.synopsis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ActionMenuView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 7/15/2016.
 */
public class Dash_board extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView nameTV, synopsis_idTV, companyTV, educationTV,designationTV;
    CircleImageView profile_image;
    Bitmap profilebitmap = null;
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
        companyTV. setVisibility(View.INVISIBLE);
        educationTV = (TextView) findViewById(R.id.dashboard_educationTV);
        educationTV.setVisibility(View.INVISIBLE);
        designationTV= (TextView) findViewById(R.id.dashboard_designationTV);
        designationTV.setVisibility(View.INVISIBLE);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);


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


                    //    image_base64string=jobj.getString("image");
                    //     Log.d("jobin","image:"+image_base64string);

                    Log.d("jobin", "result" + result);
                    Log.d("jobin", "img_url: " + img_url);
                    Log.d("jobin", "error" + error);
                    Log.d("jobin", "sid" + synopsis_id);
                    if (result.equals("success")) {

                        Picasso.with(Dash_board.this).load(img_url).into(profile_image);
                        nameTV.setText(first_name);
                        if(!(synopsis_id.equals("null")))
                        {
                            synopsis_idTV.setVisibility(View.VISIBLE);
                            synopsis_idTV.setText(synopsis_id);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Error fetching image", Toast.LENGTH_LONG).show();
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_synopsis) {
            startActivity(new Intent(getApplicationContext(), Dash_board.class));
            // Handle the camera action
        } else if (id == R.id.nav_about_us) {
            startActivity(new Intent(getApplicationContext(), About_us.class));


        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(getApplicationContext(), Settings_class.class));

        } else if (id == R.id.nav_faq) {
            startActivity(new Intent(getApplicationContext(), Faq_class.class));

        } else if (id == R.id.nav_contact_us) {
            startActivity(new Intent(getApplicationContext(), Contact_Us.class));

        } else if (id == R.id.nav_rate_us) {

            startActivity(new Intent(getApplicationContext(), Rate_us.class));
        } else if (id == R.id.nav_testimonial) {
            startActivity(new Intent(getApplicationContext(), Testimonial.class));

        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(getApplicationContext(), LoginClass.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
