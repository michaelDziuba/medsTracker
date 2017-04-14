package com.example.cdu.medstracker;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment {

    public static int colorCode;

    private static final int REQUEST_CODE = 0x11;
    String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"};


    FragmentManager fm;
    FloatingActionButton fab = MainActivity.fab;
    ListView drugList;
    TextView drugNameTextView;
    TextView drugDoseTextView;
    TextView whenToTakeTextView;
    TextView notesTextView;

    LinearLayout imagesLinearLayout;

    public static ArrayList<Drug> drugsArrayList;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListViewFragment newInstance(String param1, String param2) {
        ListViewFragment fragment = new ListViewFragment();
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
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);


        if(!fab.isShown()){
            fab.setVisibility(View.VISIBLE);
        }

        //Asks the user for permission to access camera and files on the user's device
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE);

        fm = getActivity().getSupportFragmentManager();
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.content_main, new CreateDrugFragment());
                ft.commit();
            }
        });

        drugList = (ListView) view.findViewById(R.id.drugListView);
        DatabaseHandler db = new DatabaseHandler(getContext());
        drugsArrayList = db.getAllDrugs();



        db.closeDB();
        final CustomAdapter adapter = new CustomAdapter(getContext(), drugsArrayList);
        drugList.setAdapter(adapter);
        drugList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView detailsTextView = (TextView) view.findViewById(R.id.detailsTextView);

                imagesLinearLayout =  (LinearLayout) view.findViewById(R.id.imagesLinearLayout);

                ImageView chevronImageView = (ImageView) view.findViewById(R.id.chevronImageView);


                if(imagesLinearLayout.getVisibility() == View.GONE || imagesLinearLayout.getVisibility() == View.INVISIBLE){
                    imagesLinearLayout.setVisibility(View.VISIBLE);

                    //update the text of the show more
                    detailsTextView.setText("Hide photo");
                    //update the chevron image
                    chevronImageView.setImageResource(R.drawable.ic_expand_less_black_24dp);

                }
                else{
                    imagesLinearLayout.setVisibility(View.GONE);
                    //update the text of the show more
                    detailsTextView.setText("Show photo");
                    //update the chevron image
                    chevronImageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
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









    public class CustomAdapter extends ArrayAdapter<Drug> {

        public CustomAdapter(Context context, ArrayList<Drug> items) {
            super(context, 0, items);
        }



        /**
         * getView is used to take every item in a list
         * and assign a view to it.
         * With this specific adapter we specified item_view as the view
         * we want every item in a list to look like.
         * After that item has item_view attached to it
         * we populate the item_view's name TextView
         */
        public View getView(int position, View convertView, ViewGroup parent){
            final int positionFinal = position;
            final Drug drug = getItem(positionFinal);

            if(!fab.isShown()){
                fab.setImageResource(R.drawable.ic_add_black_24dp);
                fab.setVisibility(View.VISIBLE);
            }

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_view, parent, false);
            }

            CardView drugCardView = (CardView) convertView;

            switch(colorCode){
                case 0:
                    drugCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteCardView));
                    break;
                case 1:
                    drugCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.yellowCardView));
                    break;
                case 2:
                    drugCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.brownCardView));
                    break;
                case 3:
                    drugCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.roseCardView));
                    break;
                default: break;

            }

            imagesLinearLayout = (LinearLayout) convertView.findViewById(R.id.imagesLinearLayout);
            imagesLinearLayout.setVisibility(View.GONE);
            if(imagesLinearLayout.getChildCount() == 0){
                //Grab all the photos that match the id of the current location
                DatabaseHandler db = new DatabaseHandler(getContext());
                final ArrayList<Image> drugImages = db.getAllImages(drug.getId());
                db.closeDB();


                //Add those photos to the gallery
                for(int i =0; i < drugImages.size(); i++){
                    final Image image = drugImages.get(i);
                    //Bitmap bitmapImage = BitmapFactory.decodeFile(image.getResource());
                    final File imageFile = new File(image.getResource());

                    final ImageView imageView = new ImageView(getContext());

                    Picasso.with(getContext().getApplicationContext()).load(imageFile).resize(image.getPictureWidth(), image.getPictureHeight()).centerInside().into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Bitmap bitmapImage = BitmapFactory.decodeFile(image.getResource());
                            imageView.setImageBitmap(bitmapImage);
                            imageView.setAdjustViewBounds(true);
                            imageView.setPadding(0,0,0,20);
                        }
                    });


                    imagesLinearLayout.addView(imageView);
                }

            }

            drugNameTextView = (TextView) convertView.findViewById(R.id.drugNameTextView);
            drugNameTextView.setText(drug.getDrugName());

            drugDoseTextView = (TextView) convertView.findViewById(R.id.drugDoseTextView);
            drugDoseTextView.setText(drug.getDrugDose());

            whenToTakeTextView = (TextView) convertView.findViewById(R.id.whenToTakeTextView);
            whenToTakeTextView.setText(drug.getWhenToTake());

            notesTextView = (TextView) convertView.findViewById(R.id.notesTextView);
            notesTextView.setText(drug.getNotes());

            LinearLayout deleteIcon = (LinearLayout) convertView.findViewById(R.id.deleteIcon);
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle(getString(R.string.delete_drug_confirm));
                    //alert.setMessage("");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseHandler db = new DatabaseHandler(getContext());
                            int drug_id = drug.getId();

                            //Deletes stored image files for the CardView, when the CardView is deleted
                            CardView cardView = (CardView)drugList.getChildAt(positionFinal);
                            LinearLayout imagesLayout = (LinearLayout) cardView.findViewById(R.id.imagesLinearLayout);
                            if(imagesLayout.getChildCount() > 0){
                                ArrayList<Image> drugImages = db.getAllImages(drug.getId());
                                for(int i = 0; i < drugImages.size(); i++){
                                    Image image = drugImages.get(i);
                                    File file = new File(image.getResource());
                                    file.delete();
                                }
                            }

                            db.deleteDrug(drug_id);
                            db.deleteImage(drug_id);
                            db.closeDB();


                            drugsArrayList.remove(positionFinal);
                            drugList.setAdapter(drugList.getAdapter());

                            dialog.dismiss();
                        }
                    });

                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                }
            });


            LinearLayout editIcon = (LinearLayout) convertView.findViewById(R.id.editIcon);
            editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i("**EditButton**", "Clicked!");
                    EditDrugFragment.drug = drug;
                    ((MainActivity)getActivity()).goToEditDrug();
                }
            });

            LinearLayout addPhotoIcon = (LinearLayout) convertView.findViewById(R.id.addPhotoIcon);
            addPhotoIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TakePhotoFragment.drug = drug;
                    ((MainActivity)getActivity()).takePhotos();
                }
            });

            LinearLayout webIcon = (LinearLayout) convertView.findViewById(R.id.webIcon);
            webIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, drug.getDrugName() + " drug information -pdf");
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }else{
                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "No installed software to complete the task", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
            });

            return  convertView;
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        //refreshes the ListView layout
        drugList.setAdapter(drugList.getAdapter());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext().getApplicationContext(), "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext().getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
