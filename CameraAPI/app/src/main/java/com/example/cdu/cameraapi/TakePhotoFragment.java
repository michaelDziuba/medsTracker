package com.example.cdu.cameraapi;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Policy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TakePhotoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TakePhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TakePhotoFragment extends Fragment implements TextureView.SurfaceTextureListener  {



    @Deprecated
    private Camera mCamera;
    private TextureView mTextureView;
    private File pictureDirectory;
    private static File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "com.example.cdu.cameraapi");



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TakePhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TakePhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TakePhotoFragment newInstance(String param1, String param2) {
        TakePhotoFragment fragment = new TakePhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_take_photo, container, false);

        //makePictureDirectory();

        mTextureView = (TextureView)view.findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);

        Button takePicture = (Button) view.findViewById(R.id.takePicture);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera!=null) {
                    mCamera.takePicture(null, null, pictureCallback);
                }
            }
        });

        Button anotherPicture = (Button) view.findViewById(R.id.anotherPicture);
        anotherPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera!=null) {
                    mCamera.startPreview();
                }
            }
        });



//        String[] pictureDirectoryFiles = pictureDirectory.list();
//        for(int i = 0; i < pictureDirectoryFiles.length; i++) {
//            Log.i("****", pictureDirectoryFiles[i]);
//        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();




        if (mCamera!=null) {
            try {
                Camera.Parameters params = mCamera.getParameters();
                params.setRotation(90);
                params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);


                params.setZoom(2);

                mCamera.setDisplayOrientation(90);


                getView().getLayoutParams().width=params.getPreviewSize().height;
                getView().getLayoutParams().height=params.getPreviewSize().width;

                mCamera.setPreviewTexture(surface);
                mCamera.startPreview();

                params.setPictureSize(params.getPreviewSize().width, params.getPreviewSize().height);

//                List<Camera.Size> sizes =  params.getSupportedPictureSizes();
//                Log.i("****", sizes.size() + "" );
//
//
//                //params.setPictureSize(sizes.get(0).width, sizes.get(0).height);
//                for(Camera.Size size : sizes){
//                    Log.i("***","" + size.width + ", "+ size.height);
////                    if(size.width < 2050){
////                        params.setPictureSize (size.width,size.height);
////                        break;
////                    }
//                }


//                List<String> focusModes = params.getSupportedFocusModes();
//                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
//                    // Autofocus mode is supported
//                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//                }
                mCamera.setParameters(params);






            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mCamera!=null) {
            mCamera.stopPreview();
            mCamera.release();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    Camera.PictureCallback pictureCallback = new
            Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {

                    File pictureFile = getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);

                    if (pictureFile == null){
                        Log.d("*****", "Error creating media file, check storage permissions!!!");
                        return;
                    }

                    try {
//                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis());
//                        String fileName = "PHOTO_" + timeStamp + ".jpg";
//
//                        File pictureFile = new File(pictureDirectory,fileName);


//                        FileOutputStream fileOutputStream =new FileOutputStream(pictureFile.getPath());
//                        fileOutputStream.write(data);
//                        fileOutputStream.close();

                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(data);
                        fos.close();
                        Toast.makeText(getActivity(), "Picture Taken", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            };

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


//    public void makePictureDirectory(){
//        String appPath = getActivity().getApplicationInfo().dataDir;
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


    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "com.example.cdu.cameraapi");


        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("CameraApi", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".png");
        } else if(type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }





//    public Bitmap rotateBitmapOrientation(String photoFilePath) {
//
//        // Create and configure BitmapFactory
//        BitmapFactory.Options bounds = new BitmapFactory.Options();
//        bounds.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(photoFilePath, bounds);
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
//        // Read EXIF Data
//        ExifInterface exif =  null;
//        try {
//            exif = new ExifInterface(photoFilePath);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
//        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
//        int rotationAngle = 0;
//        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
//        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
//        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
//        // Rotate Bitmap
//        Matrix matrix = new Matrix();
//        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
//        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
//        // Return result
//        return rotatedBitmap;
//    }


}
