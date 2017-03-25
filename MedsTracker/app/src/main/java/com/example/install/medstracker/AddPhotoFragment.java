package com.example.install.medstracker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
    Spinner spin;
    ImageView cameraButton;
    LinearLayout galleryLayout;
    ArrayList<Drug> bookList;

    private static final int CAMERA_INTENT = 1;
    private String imageLocation;

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

        galleryLayout = (LinearLayout) view.findViewById(R.id.galleryLayout);
        spin = (Spinner) view.findViewById(R.id.bookSpinner);


        DatabaseHandler db = new DatabaseHandler(getContext());

        bookList = db.getAllBooks();

        db.closeDB();

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, bookList);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spin.setAdapter(adapter);


        Drug book = (Drug) spin.getSelectedItem();
        if(book == null){
            Toast toast = Toast.makeText(getActivity(), "Please add a book,\nbefore adding a photo", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                galleryLayout.removeAllViews();
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
                File image = null;
                try{
                    image = createImage();
                }catch(IOException e){
                    e.printStackTrace();
                }


                if (image != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(), "com.example.install.medstracker.android.fileprovider", image);

                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);


                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(intent, CAMERA_INTENT);

                    }


                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_INTENT && resultCode == RESULT_OK){
           Bitmap bitmapImage = BitmapFactory.decodeFile(imageLocation);
            int height = bitmapImage.getHeight() ;
            int width = bitmapImage.getWidth();
            int newWidth = 400;
            int newHeight = newWidth * height / width;
            bitmapImage = getResizedBitmap(bitmapImage, newWidth, newHeight);

//            Matrix matrix = new Matrix();
//            matrix.postRotate(90);
//            bitmapImage = Bitmap.createBitmap(bitmapImage, 0, 0, bitmapImage.getWidth(), bitmapImage.getHeight(), matrix, true);

            FileOutputStream fOut;
            try {
                fOut = new FileOutputStream(imageLocation);
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();

            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

           
//            int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
//            Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
           // your_imageview.setImageBitmap(scaled);


            ImageView imageView = new ImageView(getContext());
            imageView.setMaxHeight(400);
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            imageView.setAdjustViewBounds(true);
            //imageView.setImageBitmap(scaled);
            imageView.setImageBitmap(bitmapImage);
            imageView.setPadding(0,15,0,15);
            galleryLayout.addView(imageView);

            Drug book = (Drug) spin.getSelectedItem();
           if(book != null) {
               Image image = new Image(book.getId(), imageLocation);

               /**
                * Add the photo to the database
                */
               DatabaseHandler db = new DatabaseHandler(getContext());
               int id = (int) db.addImage(image);
               if (id != -1) {
                   image.setId(id);
                   Toast.makeText(getActivity(), "Photo Added", Toast.LENGTH_LONG).show();
               } else {
                   Toast.makeText(getActivity(), "Photo Not Added", Toast.LENGTH_LONG).show();
               }
               db.closeDB();
           }else{
               Toast.makeText(getActivity(), "Please add a book,\nbefore adding a photo", Toast.LENGTH_LONG).show();
           }
        }
    }

    /**
     * This method is used to create a temp
     * file that the Camera will use to save a photo
     * We will generate a file with a collision free name
     * (hiking_log_timestamp.jpg)
     * This method throws an IOException because we might not
     * be able to create the file
     * @return
     * @throws IOException
     */
    File createImage() throws IOException{
        //Create a timestamp to help create a collision free name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHss").format(new Date());
        //Create the name of the image
        String fileName = "book_list_" + timeStamp;
        //Grab the directory we want to save the image
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //Create the image in that directory
        File bitmapImage = File.createTempFile(fileName, ".jpg", directory);

        //Save the location of the image
        imageLocation = bitmapImage.getAbsolutePath();
        return bitmapImage;
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



    public File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
    }



}
