package com.example.cdu.medstracker;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

    /**
     * The pixel height and the width of the picture taken by the camera
     */
    private int pictureWidth;
    private int pictureHeight;

    /**
     * Drug object injected from the ListViewFragment
     */
    public static Drug drug = null;

    /**
     * Floating Action Button from the MainActivity
     */
    FloatingActionButton fab = MainActivity.fab;

    /**
     * Camera and Camera Preview objects
     */
    @Deprecated
    private Camera mCamera;
    private TextureView mTextureView;

    /**
     * The directory path where a picture file is stored, after a photo is taken
     */
    private static File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "com.example.cdu.medstracker");

    /**
     * Indicator of whether the camera's perview is showing or not
     */
    private boolean previewShowing = true;

    /**
     * Contains raw image data, after a photo is taken
     */
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

    /**
     * Creates the TakePhotoFragment view for the app
     *
     * @param inflater  inflates the fragment's view
     * @param container  A special view that contains the fragment's view
     * @param savedInstanceState  bundle of saved items for restoring the fragment's view from memory
     * @return  Returns the inflated view of this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         *  Inflate the layout for this fragment
         */
        View view = inflater.inflate(R.layout.fragment_take_photo, container, false);

        /**
         * Hide the Floating Action Button, if it's shown
         */
        if(fab.isShown()){
            fab.setVisibility(View.INVISIBLE);
        }

        /**
         * Sets the camera preview display
         */
        mTextureView = (TextureView)view.findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);

        /**
         * Button with click listener that deletes the taken picture
         */
        Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera!=null && !previewShowing) {
                    dataGlobal = null;
                    mCamera.startPreview();
                    previewShowing = true;
                    Toast.makeText(getActivity(), getString(R.string.photo_deleted), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), getString(R.string.no_photo_to_delete), Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * Icon with click listener that takes the picture
         */
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

        /**
         * Button with click listener that saves the taken picture
         */
        Button saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dataGlobal != null) {
                    /**
                     * Creates a picture file from the taken picture
                     */
                    File pictureFile = getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);

                    if (pictureFile == null) {
                        return;
                    }

                    try {
                        /**
                         * Writes the picture file to a memory location specified by the picture files path property
                         */
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(dataGlobal);
                        fos.close();

                        DatabaseHandler db = new DatabaseHandler(getContext());

                        if(drug != null){
                            Image image = new Image(drug.getId(), pictureFile.getPath(), pictureWidth, pictureHeight);

                            /**
                             * Add the photo to the database
                             */
                            int id = (int) db.addImage(image);
                            if (id != -1) {
                                image.setId(id);
                                Toast.makeText(getActivity(), getString(R.string.photo_added), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.photo_not_added), Toast.LENGTH_LONG).show();
                            }
                        }

                        db.closeDB();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /**
                     * Takes the user back to the listView with drug CardViews
                     */
                    ((MainActivity)getActivity()).goToListView();


                }else{
                    Toast.makeText(getActivity(), getString(R.string.please_take_picture), Toast.LENGTH_LONG).show();
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
        //Log.i("****","onPause() called");
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


    /**
     * Method sets up camera parameters for taking a picture
     *
     * @param surface The camera's preview
     * @param width  The width of the picture for the camera to take
     * @param height  The height of the picture for the camera to take
     */
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

                /**
                 * Checks if the device's camera has a flash, and sets its mode
                 */
               if (getActivity().getPackageManager().hasSystemFeature("android.hardware.camera.FLASH_MODE_AUTO")){
                   params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
               }

                /**
                 * Checks if the device's camera has a focus feature, and sets its mode
                 */
                if(getActivity().getPackageManager().hasSystemFeature("android.hardware.camera.FOCUS_MODE_AUTO")){
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }

                params.setZoom(2);

                /**
                 * Rotates the Camera's preview into portrait orientation (The default is landscape)
                 */
                mCamera.setDisplayOrientation(90);

                /**
                 * Gets the picture's height and width from the camera's preview window
                 */
                pictureWidth = params.getPreviewSize().width;
                pictureHeight = params.getPreviewSize().height;

                getView().getLayoutParams().width = pictureWidth;
                getView().getLayoutParams().height = pictureHeight;

                mCamera.setPreviewTexture(surface);
                mCamera.startPreview();

                /**
                 * Sets the picture's height and width to those of the Camera's preview window (Practically, the whole screen of the device)
                 */
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


    /**
     * Method executed after the camera takes a picture
     * assigns picture data to a byte array (dataGlobal) for saving as a file
     */
    Camera.PictureCallback pictureCallback = new
            Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    dataGlobal = data;
                    Toast.makeText(getActivity(), getString(R.string.picture_taken), Toast.LENGTH_SHORT).show();
                }
            };


    /**
     * Method creates a file for saving an image
     *
     * @param type the type of image data (eg. still photo or video)
     * @return returns the image file with a path for its storage
     */
    private static File getOutputMediaFile(int type){

        /**
         * Create a new File with path for storing the image
         */
        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "com.example.cdu.medstracker");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }



}
