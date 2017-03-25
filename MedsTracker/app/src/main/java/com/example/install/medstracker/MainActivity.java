package com.example.install.medstracker;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnFragmentInteractionListener,
        ViewPagerControllerFragment.OnFragmentInteractionListener,
        ViewPagerContentFragment.OnFragmentInteractionListener,
        ListViewFragment.OnFragmentInteractionListener,
        CreateDrugFragment.OnFragmentInteractionListener,
        AddPhotoFragment.OnFragmentInteractionListener{




    public static FloatingActionButton fab;



    FragmentManager fm = getSupportFragmentManager();
    Fragment[] listContentFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Log.i("****Internal Memory****", "" + internalMemory());
//        Log.i("****External Memory****", "" + externalMemory());


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEA00")));
        fab.setRippleColor(Color.parseColor("#00E676"));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    //puts the main fragment content for display, when the app's view is displayed for the first time
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content_main, new MainFragment());
        transaction.commit();


        //puts the Nav Drawer menu for display, when the app's view is displayed
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
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
        } else if (id == R.id.nav_add_photo) {

            FragmentTransaction tran = fm.beginTransaction();
            tran.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            tran.addToBackStack(null);
            tran.replace(R.id.content_main, new AddPhotoFragment());
            tran.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    //Method called from the onclick listener of MyItemRecyclerViewAdapter
    //It displays content for the clicked item
    public void replaceFragment(int listPosition){

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction.replace(R.id.content_main, listContentFragments[listPosition]);
        transaction.addToBackStack(null);
        transaction.commit();

        //Log.i("MainActivity", String.valueOf(listPosition));
    }

    public void goToUrl(String url){
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(webpage);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

//    public long internalMemory(){
//        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
//        long blockSize = statFs.getBlockSize();
//        long totalSize = statFs.getBlockCount()*blockSize;
//        long availableSize = statFs.getAvailableBlocks()*blockSize;
//        long freeSize = statFs.getFreeBlocks()*blockSize;
//
//        return availableSize;
//    }
//
//    public long externalMemory(){
//        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
//        long blockSize = statFs.getBlockSize();
//        long totalSize = statFs.getBlockCount()*blockSize;
//        long availableSize = statFs.getAvailableBlocks()*blockSize;
//        long freeSize = statFs.getFreeBlocks()*blockSize;
//
//        return availableSize;
//    }
}
