package com.example.cdu.medstracker;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
    //private File pictureDirectory;
    private static File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "com.example.cdu.medstracker");
    private boolean previewShowing = true;
    public static byte[] dataGlobal =  null;


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


        Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera!=null && !previewShowing) {
                    dataGlobal = null;
                    mCamera.startPreview();
                    previewShowing = true;
                    Toast.makeText(getActivity(), "Picture Deleted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "There is no picture to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ImageButton takePicture = (ImageButton) view.findViewById(R.id.takePicture);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera!=null && previewShowing) {
                    mCamera.takePicture(null, null, pictureCallback);
                    previewShowing = false;
                }
            }
        });


        Button saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("*****", "DataGlobal" + dataGlobal);
                if(dataGlobal != null) {
                    File pictureFile = getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);


                    if (pictureFile == null) {
                        Log.d("*****", "Error creating media file, check storage permissions!!!");
                        return;
                    }

                    try {
                        AddPhotoFragment.photoPath = pictureFile.getPath();

                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(dataGlobal);
                        fos.close();
                        //Toast.makeText(getActivity(), "Picture Saved", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    mCamera.startPreview();
//                    previewShowing = true;
                    ((MainActivity)getActivity()).returnToAddPhotos();
                }else{
                    Toast.makeText(getActivity(), "Please take a picture,\nthen press SAVE", Toast.LENGTH_LONG).show();
                }
            }
        });




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
    public void onPause(){
        super.onPause();
        Log.i("****","onPause() called");
        dataGlobal = null;
        previewShowing = true;
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

        if (Build.VERSION.SDK_INT >= 17) {
            mCamera.enableShutterSound(true);
        }

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
                    dataGlobal = data;
                    Toast.makeText(getActivity(), "Picture Taken", Toast.LENGTH_SHORT).show();
                }
            };



    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        Log.i("External Storage state", Environment.getExternalStorageState());

        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "com.example.cdu.medstracker");


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
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }



}
