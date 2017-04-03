package com.example.cdu.medstracker;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnFragmentInteractionListener,
        TakePhotoFragment.OnFragmentInteractionListener,
        ListViewFragment.OnFragmentInteractionListener,
        CreateDrugFragment.OnFragmentInteractionListener,
        ViewPagerControllerFragment.OnFragmentInteractionListener,
        ViewPagerContentFragment.OnFragmentInteractionListener,
        EditDrugFragment.OnFragmentInteractionListener,
        ListViewPhoneFragment.OnFragmentInteractionListener,
        CreatePhoneFragment.OnFragmentInteractionListener,
        EditPhoneFragment.OnFragmentInteractionListener{


    DisplayMetrics displayMetrics = new DisplayMetrics();
    public static int screenWidth;
    public static int screenHeight;

    public static FloatingActionButton fab;

    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.heightPixels;
        screenHeight = displayMetrics.widthPixels;

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEA00")));
        fab.setRippleColor(Color.parseColor("#00E676"));



//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content_main, new MainFragment());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            fab.setImageResource(android.R.drawable.ic_dialog_email);
            if(fab.isShown()){
                fab.setVisibility(View.INVISIBLE);
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

        if (id == R.id.nav_home) {
            if(fab.isShown()){
                fab.setVisibility(View.INVISIBLE);
            }
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            transaction.addToBackStack(null);
            transaction.replace(R.id.content_main, new MainFragment());
            transaction.commit();

        } else if (id == R.id.nav_your_drugs) {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            transaction.addToBackStack(null);
            //transaction.replace(R.id.content_main, new ItemFragment());
            transaction.replace(R.id.content_main, new ListViewFragment());
            transaction.commit();

        } else if (id == R.id.nav_drugs_guide) {

            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            transaction.addToBackStack(null);
            transaction.replace(R.id.content_main, new ViewPagerControllerFragment());
            transaction.commit();
        } else if (id == R.id.nav_phone) {

            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            transaction.addToBackStack(null);
            transaction.replace(R.id.content_main, new ListViewPhoneFragment());
            transaction.commit();
        } else if (id == R.id.nav_about) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void takePhotos(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, new TakePhotoFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void goToEditDrug(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, new EditDrugFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void goToListView(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, new ListViewFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void goToEditPhone(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, new EditPhoneFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
