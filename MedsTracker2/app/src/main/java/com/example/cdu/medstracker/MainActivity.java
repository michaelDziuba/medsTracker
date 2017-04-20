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
import android.support.v4.content.ContextCompat;
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

    public static FloatingActionButton fab;

    FragmentManager fm = getSupportFragmentManager();

    /**
     * Sets up the initial view for the user, when the user starts the app
     *
     * @param savedInstanceState A bundle of saved items for restoring the the view from memory
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Assigns cardView background color setting from memory, or the default value of zero, if no setting is stored in memory
         */
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        PreferenceManager.getDefaultSharedPreferences(this.getBaseContext()).registerOnSharedPreferenceChangeListener(PreferencesChangeHandler);
        cardViewBackgroundChoice = Integer.parseInt(sharedPreferences.getString(CARDVIEW_BACKGROUND_CHOICE, "0"));

        /**
         * Selects the 1st choice in the settings menu and makes it default
         */
        if(cardViewBackgroundChoice == 0){
            PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
        }

        /**
         * sets the background color for cardViews, depending on the user's choice
         */
        changeBackgroundColor(CARDVIEW_BACKGROUND_CHOICE, cardViewBackgroundChoice);

        /**
         * Sets background and ripple colors for the Floating Acion Button
         */
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.fab_color)));
        fab.setRippleColor(ContextCompat.getColor(getApplicationContext(), R.color.fab_ripple_color));

        /**
         * Sets up the Nav Drawer menu for the app
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /**
         *  Displays the Apps' Home Page (MainFragment.java)
         */
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content_main, new MainFragment());
        transaction.commit();
    }

    /**
     * Closes the Nav Drawer Menu if it's open, when the phone's back button is pressed
     * or hides the Floating Action Button, if Nav Drawer is not open
     */
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


    /**
     * Inflates the Settings Menu
     *
     * @param menu  The Settings Menu
     * @return boolean true, if menu is successfully inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    /**
     *  Starts the Settings Activity
     *
      * @param item The Settings Menu item
     * @return either boolean true, if menu item id is action_settings,
     *          or boolean true from the parent object, if the menu item id is not action_settings
     */
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


    /**
     * Responds to user selection in the Nav Drawer and carries out the Fragment transaction specified by the menu id
     *
     * @param item Nav Drawer Menu item selected by the user
     * @return true, if the response to user menu selection is successful and the Nav Drawer Menu closes
     */
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


    /**
     * Displays the App's Custom Camera preview for taking a photo
     * This method is called on from a Drug CardView displayed in the ListViewFragment
     */
    public void takePhotos(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, new TakePhotoFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }


    /**
     * Takes the user to a drug CardView's editing fragment for editing its contents
     * This method is called on from a Drug CardView displayed in the ListViewFragment
     */
    public void goToEditDrug(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, new EditDrugFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Takes the user to the list of drug CardViews displayed in the ListViewFragment
     * This method is called on either from the CreateDrugFragment or from the EditDrugFragment
     */
    public void goToListView(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, new ListViewFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Takes the user to a phone CardView's editing fragment for editing its contents
     * This method is called on from a Phone CardView displayed in the ListViewPhoneFragment
     */
    public void goToEditPhone(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, new EditPhoneFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }


    /**
     * Takes the user to the list of phone CardViews displayed in the ListViewPhoneFragment
     * This method is called on either from the CreatePhoneFragment or from the EditPhoneFragment
     */
    public void goToPhone(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, new ListViewPhoneFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Method calls on the phone's Internet browser to open a web page specified by the url parameter
     *
     * @param url the url for the web page to open in the phone's Internet browser
     */
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_installed_software), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    /**
     * Method required by OnFragmentInteractionListener interface
     * (This method is not used in this App)
     *
     * @param uri
     */
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
        }
    };


    /**
     * Method assigns color code to ListViewFragment for changing the color of its CardViews
     */
    private void changeBackgroundColor(String backgroundType, int choice){
        if(backgroundType.equals(CARDVIEW_BACKGROUND_CHOICE)){
            ListViewFragment.colorCode = choice;
            ListViewPhoneFragment.colorCode = choice;
        }

    }

}
