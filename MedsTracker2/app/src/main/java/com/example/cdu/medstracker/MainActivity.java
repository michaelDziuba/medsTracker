package com.example.cdu.medstracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
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
        EditPhoneFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener{


    SharedPreferences sharedPreferences;
    public int cardViewBackgroundChoice;
    public final String CARDVIEW_BACKGROUND_CHOICE = "cardview_background_choice";
//    public int drugListBackgroundChoice;
//    public final String DRUG_BACKGROUND_CHOICE = "drug_background_choice";
//    public int phoneListBackgroundChoice;
//    public final String PHONE_BACKGROUND_CHOICE = "phone_background_choice";


    public static FloatingActionButton fab;

    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        PreferenceManager.getDefaultSharedPreferences(this.getBaseContext()).registerOnSharedPreferenceChangeListener(PreferencesChangeHandler);
        cardViewBackgroundChoice = Integer.parseInt(sharedPreferences.getString(CARDVIEW_BACKGROUND_CHOICE, "0"));
        if(cardViewBackgroundChoice == 0){
            PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
        }
        changeBackgroundColor(CARDVIEW_BACKGROUND_CHOICE, cardViewBackgroundChoice);


//        drugListBackgroundChoice = Integer.parseInt(sharedPreferences.getString(DRUG_BACKGROUND_CHOICE, "0"));
//        phoneListBackgroundChoice = Integer.parseInt(sharedPreferences.getString(PHONE_BACKGROUND_CHOICE, "0"));
//        changeBackgroundColor(DRUG_BACKGROUND_CHOICE, drugListBackgroundChoice);
//        changeBackgroundColor(PHONE_BACKGROUND_CHOICE, phoneListBackgroundChoice);



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEA00")));
        fab.setRippleColor(Color.parseColor("#00E676"));


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
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, 1);
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
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            transaction.addToBackStack(null);
            transaction.replace(R.id.content_main, new AboutFragment());
            transaction.commit();

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
    public void goToDrugs(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, new ListViewFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void goToPhone(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, new ListViewPhoneFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }




    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No installed software to complete the task", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Shared preferences listener that listens for a change in user selected settings,
     *  assigns a user selected value to the menuChoice property and calls on a method to change the recipe names and descriptions
     *   available to the user through the ListView
     */
    private SharedPreferences.OnSharedPreferenceChangeListener PreferencesChangeHandler = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals(CARDVIEW_BACKGROUND_CHOICE)){
                cardViewBackgroundChoice = Integer.parseInt(sharedPreferences.getString(key, "0"));
                changeBackgroundColor(CARDVIEW_BACKGROUND_CHOICE, cardViewBackgroundChoice);
            }

//            else if(key.equals(PHONE_BACKGROUND_CHOICE)){
//                phoneListBackgroundChoice = Integer.parseInt(sharedPreferences.getString(key, "0"));
//                changeBackgroundColor(PHONE_BACKGROUND_CHOICE, phoneListBackgroundChoice);
//            }
        }
    };


    //assigns color code to ListViewFragment for changing the color of its CardViews
    private void changeBackgroundColor(String backgroundType, int choice){
        if(backgroundType.equals(CARDVIEW_BACKGROUND_CHOICE)){
            ListViewFragment.colorCode = choice;
            ListViewPhoneFragment.colorCode = choice;
        }

//        else if(backgroundType.equals(PHONE_BACKGROUND_CHOICE)){
//            ListViewPhoneFragment.colorCode = choice;
//        }
    }

}
