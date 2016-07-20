package com.synopsis.androidapp.synopsis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ActionMenuView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by User on 7/15/2016.
 */
public class Dash_board extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        Toolbar bottom_toolbar = (Toolbar) findViewById(R.id.bottom_toolbar);

        setSupportActionBar(toolbar1);


        bottom_toolbar.inflateMenu(R.menu.toolbar_menu);//changed


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

        //toolbar2 menu items CallBack listener
    /*    bottom_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.verify_user) {

                  startActivity(new Intent(getApplicationContext(),VerifyClass.class));
                } else if (item.getItemId() == R.id.credits) {
                    startActivity(new Intent(getApplicationContext(),Credits.class));

                } else if (item.getItemId() == R.id.chat) {


                    String chaturl = "https://chatserver5.comm100.com/ChatWindow.aspx?siteId=107734&planId=2079&visitType=1&byHref=1&partnerId=-1";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(chaturl));
                    startActivity(i);
                }
                return false;
            }
        });
*/

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
