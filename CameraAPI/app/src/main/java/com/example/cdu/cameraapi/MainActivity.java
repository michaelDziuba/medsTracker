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

//        makePictureDirectory();
//
//        mTextureView = (TextureView)findViewById(R.id.textureView);
//        mTextureView.setSurfaceTextureListener(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

//    @Override
//    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//        mCamera = Camera.open();
//        if (mCamera!=null) {
//            try {
//                mCamera.setPreviewTexture(surface);
//                mCamera.startPreview();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//        if (mCamera!=null) {
//            mCamera.stopPreview();
//            mCamera.release();
//        }
//        return true;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//
//    }
//
//
//    Camera.PictureCallback pictureCallback = new
//            Camera.PictureCallback() {
//                @Override
//                public void onPictureTaken(byte[] data, Camera camera) {
//                    try {
//                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis());
//                        String fileName = "PHOTO_" + timeStamp + ".jpg";
////                        File pictureFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileName);
//                        File pictureFile = new File(pictureDirectory,fileName);
//                        FileOutputStream fileOutputStream =new FileOutputStream(pictureFile.getPath());
//                        fileOutputStream.write(data);
//                        fileOutputStream.close();
//                        Toast.makeText(MainActivity.this, "Picture Taken", Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//
//    public void takePicture(View view) {
//        if (mCamera!=null) {
//            mCamera.takePicture(null, null, pictureCallback);
//        }
//    }
//
//    public void takeAnotherPicture(View view) {
//        if (mCamera!=null) {
//            mCamera.startPreview();
//        }
//    }
//
//
//    public void makePictureDirectory(){
//        String appPath = getApplicationInfo().dataDir;
//        File appDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + appPath);
//
//        if(!appDir.exists() && !appDir.isDirectory()) {
//            // create empty directory
//            if (appDir.mkdirs()) {
//                Log.i("CreateDir","App dir created");
//                pictureDirectory = appDir;
//            } else {
//                Log.w("CreateDir","Unable to create app dir!");
//            }
//        }else {
//            Log.i("CreateDir","App dir already exists");
//            pictureDirectory = appDir;
//        }
//    }
//
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQUEST_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // save file
//            } else {
//                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    public void takePhotos(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.activity_main, new TakePhotoFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
