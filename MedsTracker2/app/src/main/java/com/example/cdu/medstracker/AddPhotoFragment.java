package com.example.cdu.medstracker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddPhotoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPhotoFragment extends Fragment {


    public static String photoPath =  null;
    public static int selectedItem = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int REQUEST_CODE = 0x11;
    String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"};


    FloatingActionButton fab = MainActivity.fab;
    Spinner drugSpinner;
    ImageView cameraButton;
    LinearLayout galleryLinearLayout;
    ArrayList<Drug> drugList;

    private OnFragmentInteractionListener mListener;

    public AddPhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPhotoFragment newInstance(String param1, String param2) {
        AddPhotoFragment fragment = new AddPhotoFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);


        //Asks the user for permission to access photos, media, and files on the user's device
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE);

        if(fab.isShown()){
            fab.setVisibility(View.INVISIBLE);
        }


        galleryLinearLayout = (LinearLayout) view.findViewById(R.id.galleryLinearLayout);

        if(photoPath != null){
            ImageView imageView = new ImageView(getContext());
            imageView.setMaxHeight((int)(MainActivity.screenWidth * 0.8));
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            imageView.setAdjustViewBounds(true);
            //imageView.setImageBitmap(scaled);
            imageView.setImageBitmap(BitmapFactory.decodeFile(photoPath));
            imageView.setBackgroundColor(Color.LTGRAY);
            imageView.setPadding(0,15,0,15);
            galleryLinearLayout.addView(imageView);
        }

        drugSpinner = (Spinner) view.findViewById(R.id.drugSpinner);

        DatabaseHandler db = new DatabaseHandler(getContext());

        drugList = db.getAllDrugs();

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, drugList);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        drugSpinner.setAdapter(adapter);
        drugSpinner.setSelection(selectedItem);


        Drug drug = (Drug) drugSpinner.getSelectedItem();
        if(drug == null){
            Toast toast = Toast.makeText(getActivity(), "Please add a drug,\nbefore adding a photo", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }

        if(photoPath != null){
            Image image = new Image(drug.getId(), photoPath);

               /**
                * Add the photo to the database
                */
               //DatabaseHandler db = new DatabaseHandler(getContext());
               int id = (int) db.addImage(image);
               if (id != -1) {
                   image.setId(id);
                   Toast.makeText(getActivity(), "Photo Added", Toast.LENGTH_LONG).show();
               } else {
                   Toast.makeText(getActivity(), "Photo Not Added", Toast.LENGTH_LONG).show();
               }
        }

        db.closeDB();

        drugSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != selectedItem) {
                    galleryLinearLayout.removeAllViews();
                }
//                TextView textView = (TextView) parent.getSelectedView();
//                if(textView != null) {
//                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //parent.setSelection(0);
            }
        });



        cameraButton = (ImageView) view.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem = drugSpinner.getSelectedItemPosition();
                ((MainActivity)getActivity()).takePhotos();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        photoPath = null;
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // save file
            } else {
                Toast.makeText(getContext().getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
