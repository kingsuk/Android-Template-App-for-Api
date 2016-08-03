package us.tier5.u_rang;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import layout.Contact_fragment;
import layout.Dashboard_fragment;
import layout.Faq_fragment;
import layout.Neighborhood_fragment;
import layout.Price_fragment;
import layout.SchoolDonation_fragment;
import layout.Service_fragment;

public class DashboardNew extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SchoolDonation_fragment.OnFragmentInteractionListener,Faq_fragment.OnFragmentInteractionListener,Neighborhood_fragment.OnFragmentInteractionListener, Price_fragment.OnFragmentInteractionListener, Contact_fragment.OnFragmentInteractionListener,Service_fragment.OnFragmentInteractionListener, Dashboard_fragment.OnFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = null;
        Class fragmentClass = null;

        fragmentClass = Dashboard_fragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.containerView, fragment).commit();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        Button pickUp = (Button) findViewById(R.id.fab);
        pickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
                showPopup();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void showPopup()
    {
        Dialog settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.category_popup
                , null));

        ImageView ivFast = (ImageView) settingsDialog.findViewById(R.id.ivFast);
        ivFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardNew.this,PickupRequest.class);
                i.putExtra("order_type", "1");//one for fast pickup
                startActivity(i);
            }
        });

        ImageView ivDetailed = (ImageView) settingsDialog.findViewById(R.id.ivDetailed);
        ivDetailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardNew.this,PickupRequest.class);
                i.putExtra("order_type", "0");//zero for detailed pickup
                startActivity(i);
            }
        });

        settingsDialog.show();
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
                Intent intent = new Intent(DashboardNew.this,LoginActivity.class);
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

        if (id == R.id.nav_dashboard) {

            fragmentClass = Dashboard_fragment.class;

        } else if (id == R.id.nav_price) {

            fragmentClass = Price_fragment.class;

        } else if (id == R.id.nav_neighborhood) {

            fragmentClass = Neighborhood_fragment.class;

        } else if (id == R.id.nav_services) {

            fragmentClass = Service_fragment.class;

        } else if (id == R.id.nav_school_donation) {

            fragmentClass = SchoolDonation_fragment.class;

        } else if (id == R.id.nav_contact) {

            fragmentClass = Contact_fragment.class;

        } else if (id == R.id.nav_faq) {

            fragmentClass = Faq_fragment.class;

        }
        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.containerView, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(String swag) {

    }
}
