package us.tier5.u_rang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import layout.SchoolDonation_fragment;

/**
 * Created by root on 25/7/16.
 */
public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SchoolDonation_fragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SharedPreferences prefs = getSharedPreferences("U-rang", MODE_PRIVATE);
        int restoredText = prefs.getInt("user_id", 0);

        if (restoredText == 0)
        {
            Intent i = new Intent(Dashboard.this,LoginActivity.class);
            startActivity(i);
        }



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*if(Build.VERSION.SDK_INT < 23)
        {
            drawer.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.backimage));
        }
        else
        {
            drawer.setBackground(getDrawable(R.drawable.backimage));
        }*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private int backButtonCount = 0;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(backButtonCount >= 1)
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
                backButtonCount++;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = getSharedPreferences("U-rang", MODE_PRIVATE).edit();
            editor.putInt("user_id", 0);
            if(editor.commit())
            {
                Intent intent = new Intent(Dashboard.this,LoginActivity.class);
                startActivity(intent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_price) {
            // Handle the camera action
            //Toast.makeText(getApplication(),"camera clicked",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Dashboard.this,Prices.class);
            startActivity(i);
        } else if (id == R.id.nav_neighborhood) {

            Intent i = new Intent(Dashboard.this,Neighborhood.class);
            startActivity(i);

        } else if (id == R.id.nav_services) {
                Intent i = new Intent(Dashboard.this,DashboardNew.class);
                startActivity(i);

        } else if (id == R.id.nav_school_donation) {
            fragmentClass = SchoolDonation_fragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.containerView, fragment).commit();
            setTitle("Faq");

        } else if (id == R.id.nav_contact) {
            Intent i = new Intent(Dashboard.this,Contact.class);
            startActivity(i);

        } else if (id == R.id.nav_faq) {
            Intent intent = new Intent(Dashboard.this,Faq.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(String swag) {
        Toast.makeText(getApplicationContext(),swag,Toast.LENGTH_SHORT).show();

    }
}
