package com.example.cdu.cameraapi;

import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements
        MainFragment.OnFragmentInteractionListener,
        TakePhotoFragment.OnFragmentInteractionListener{

    FragmentManager fm = getSupportFragmentManager();

    @Deprecated
    private Camera mCamera;
    private TextureView mTextureView;
    private File pictureDirectory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //puts the main fragment content for display, when the app's view is displayed for the first time
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.activity_main, new MainFragment());
        transaction.commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void takePhotos(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.activity_main, new TakePhotoFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void returnToAddPhotos(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.activity_main, new MainFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
