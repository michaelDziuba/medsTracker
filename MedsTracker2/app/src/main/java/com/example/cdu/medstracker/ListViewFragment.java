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

    /**
     * colorCode value is injected from the MainActivity, when the user selects a setting for Card Color in the Settings Menu
     */
    public static int colorCode;

    /**
     * Permission properties for asking the user for corresponding permissions
     */
    private static final int REQUEST_CODE = 0x11;
    String[] permissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};


    /**
     *  Fragment manager used for taking the user to the CreateDrugFragment view
     */
    FragmentManager fm;

    /**
     * Floating Action Button from the MainActivity
     */
    FloatingActionButton fab = MainActivity.fab;

    /**
     * The listView for displaying Drug CardViews
     */
    ListView drugList;

    /**
     * CardView text fields for displaying information about a drug
     */
    TextView drugNameTextView;
    TextView drugDoseTextView;
    TextView whenToTakeTextView;
    TextView notesTextView;

    /**
     * Contains images for a drug CardView
     */
    LinearLayout imagesLinearLayout;

    /**
     * Contains drug objects from which all drug CardViews are created
     */
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


    /**
     * Creates the ListViewFragment view for the app
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
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        /**
         * Show the Floating Action Button, if it's not shown
         */
        if(!fab.isShown()){
            fab.setVisibility(View.VISIBLE);
        }

        /**
         * Asks the user for permission to access camera and files on the user's device
         */
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE);

        /**
         * Floating Action Button with attached click listener for taking the user to the CreateDrugFragment form
         */
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

        /**
         * Gets an arrayList of drug object for all drugs stored in the database drugs table
         */
        drugList = (ListView) view.findViewById(R.id.drugListView);
        DatabaseHandler db = new DatabaseHandler(getContext());
        drugsArrayList = db.getAllDrugs();
        db.closeDB();

        /**
         * Sets an adapter for creating and displaying drug CardViews in the ListViewFragment
         */
        final CustomAdapter adapter = new CustomAdapter(getContext(), drugsArrayList);
        drugList.setAdapter(adapter);

        /**
         * CardView listener for showing and hiding photo(s) in a CardView, if the CardView has photo(s)
         */
        drugList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView detailsTextView = (TextView) view.findViewById(R.id.detailsTextView);

                imagesLinearLayout =  (LinearLayout) view.findViewById(R.id.imagesLinearLayout);

                final ImageView imageViewChevron = (ImageView) view.findViewById(R.id.chevronImageView);

                if(imagesLinearLayout.getVisibility() == View.GONE || imagesLinearLayout.getVisibility() == View.INVISIBLE){
                    imagesLinearLayout.setVisibility(View.VISIBLE);

                    //update the text of the show more
                    detailsTextView.setText(getString(R.string.hide_photo));
                    //update the chevron image
                    //imageViewChevron.setImageResource(R.drawable.ic_expand_less_black_24dp);
                    switch(colorCode){
                        case 0: imageViewChevron.setImageResource(R.drawable.ic_expand_less_black_24dp); break;
                        case 1: imageViewChevron.setImageResource(R.drawable.ic_expand_less_white_24dp); break;
                    }

                }
                else{
                    imagesLinearLayout.setVisibility(View.GONE);
                    //update the text of the show more
                    detailsTextView.setText(getString(R.string.show_photo));
                    //update the chevron image
                    //imageViewChevron.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    switch(colorCode){
                        case 0: imageViewChevron.setImageResource(R.drawable.ic_expand_more_black_24dp); break;
                        case 1: imageViewChevron.setImageResource(R.drawable.ic_expand_more_white_24dp); break;
                    }
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


    /**
     * Custom adapter for creating CardViews with their functionality
     */
    public class CustomAdapter extends ArrayAdapter<Drug> {

        public CustomAdapter(Context context, ArrayList<Drug> items) {
            super(context, 0, items);
        }

        /**
         * * getView is used to take every item in a list
         * and assign a view to it.
         * With this specific adapter we specified item_view as the view
         * we want every item in a list to look like.
         * After that item has item_view attached to it
         * we populate the item_view's name TextView
         *
         * @param position listView position for the CardView
         * @param convertView the CardView
         * @param parent a special view that contains other views, CardView in this case
         * @return returns the CardView
         */
        public View getView(int position, View convertView, ViewGroup parent){
            final int positionFinal = position;
            final Drug drug = getItem(positionFinal);
            CardView cardView = (CardView)drugList.getChildAt(positionFinal);

            /**
             * Show the Floating Action Button, if it's not shown
             * (shows the fab button after the user returns to the ListViewFragment using the phone's back button)
             */
            if(!fab.isShown()){
                fab.setImageResource(R.drawable.ic_add_black_24dp);
                fab.setVisibility(View.VISIBLE);
            }

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_view, parent, false);
            }

            /**
             * Gets all the CardView's TextViews
             */
            CardView drugCardView = (CardView) convertView;
            TextView textView1 = (TextView) convertView.findViewById(R.id.textViewDrugCard1);
            TextView textView2 = (TextView) convertView.findViewById(R.id.drugNameTextView);
            TextView textView3 = (TextView) convertView.findViewById(R.id.textViewDrugCard2);
            TextView textView4 = (TextView) convertView.findViewById(R.id.drugDoseTextView);
            TextView textView5 = (TextView) convertView.findViewById(R.id.textViewDrugCard3);
            TextView textView6 = (TextView) convertView.findViewById(R.id.whenToTakeTextView);
            TextView textView7 = (TextView) convertView.findViewById(R.id.textViewDrugCard4);
            TextView textView8 = (TextView) convertView.findViewById(R.id.notesTextView);
            TextView textView9 = (TextView) convertView.findViewById(R.id.detailsTextView);

            /**
             * Gets all the CardView's icons
             */
            ImageView imageView1 = (ImageView) convertView.findViewById(R.id.deleteDrugImageView);
            ImageView imageView2 = (ImageView) convertView.findViewById(R.id.editDrugImageView);
            ImageView imageView3 = (ImageView) convertView.findViewById(R.id.addPhotoDrugImageView);
            ImageView imageView4 = (ImageView) convertView.findViewById(R.id.webDrugImageView);
            ImageView imageViewChevron = (ImageView) convertView.findViewById(R.id.chevronImageView);

            /**
             * Sets the CardView's color for text, icons, and background, depending on user selected setting in the Settings Menu
             */
            switch(colorCode){
                case 0:
                    drugCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteCardView));

                    textView1.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView2.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView3.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView4.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView5.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView6.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView7.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView8.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView9.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));

                    imageView1.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_black_24dp));
                    imageView2.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_black_24dp));
                    imageView3.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_a_photo_black_24dp));
                    imageView4.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_language_black_24dp));
                    imageViewChevron.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    break;
                case 1:
                    drugCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.brown));

                    textView1.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView2.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView3.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView4.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView5.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView6.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView7.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView8.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView9.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));

                    imageView1.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_white_24dp));
                    imageView2.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_white_24dp));
                    imageView3.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_a_photo_white_24dp));
                    imageView4.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_language_white_24dp));
                    imageViewChevron.setImageResource(R.drawable.ic_expand_more_white_24dp);
                    break;
                default: break;

            }

            /**
             * Gets all the images for the CardView (if any) and puts them in the CardView's ImageViews
             */
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
                    final File imageFile = new File(image.getResource());

                    final ImageView imageView = new ImageView(getContext());

                    /**
                     * Picasso is used to get and format the CardView's images, except when Picasso fails to get an existing picture (which happens on some devices)
                     * When Picasso fails, then code in the onError method gets the image from its file location
                     */
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

            /**
             * Sets the text values for the CardView
             */
            drugNameTextView = (TextView) convertView.findViewById(R.id.drugNameTextView);
            drugNameTextView.setText(drug.getDrugName());

            drugDoseTextView = (TextView) convertView.findViewById(R.id.drugDoseTextView);
            drugDoseTextView.setText(drug.getDrugDose());

            whenToTakeTextView = (TextView) convertView.findViewById(R.id.whenToTakeTextView);
            whenToTakeTextView.setText(drug.getWhenToTake());

            notesTextView = (TextView) convertView.findViewById(R.id.notesTextView);
            notesTextView.setText(drug.getNotes());

            /**
             * Delete icon with click listener, which deletes the CardView, after asking the user for confirmation
             * with an alert pop-up dialogue
             */
            LinearLayout deleteIcon = (LinearLayout) convertView.findViewById(R.id.deleteIcon);
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle(getString(R.string.delete_drug_confirm));
                    //alert.setMessage("");
                    alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseHandler db = new DatabaseHandler(getContext());
                            int drug_id = drug.getId();

                            //Deletes stored image files for the CardView, when the CardView is deleted
                                ArrayList<Image> drugImages = db.getAllImages(drug.getId());
                                for(int i = 0; i < drugImages.size(); i++){
                                    Image image = drugImages.get(i);
                                    File file = new File(image.getResource());
                                    file.delete();
                                }
                            //}

                            db.deleteDrug(drug_id);
                            db.deleteImage(drug_id);
                            db.closeDB();


                            drugsArrayList.remove(positionFinal);
                            drugList.setAdapter(drugList.getAdapter());

                            dialog.dismiss();
                        }
                    });

                    alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                }
            });


            /**
             * editIcon with click listener that takes the user to the EditDrugFragment form
             */
            LinearLayout editIcon = (LinearLayout) convertView.findViewById(R.id.editIcon);
            editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditDrugFragment.drug = drug;
                    ((MainActivity)getActivity()).goToEditDrug();
                }
            });

            /**
             * addPhotoIcon with click listener that takes the user to the TakePhotoFragment (which is the app's camera preview)
             */
            LinearLayout addPhotoIcon = (LinearLayout) convertView.findViewById(R.id.addPhotoIcon);
            addPhotoIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TakePhotoFragment.drug = drug;
                    ((MainActivity)getActivity()).takePhotos();
                }
            });

            /**
             * webIcon with click listener that open's the phone's Internet browser and activates its search function
             * for information related to the drug name of this CardView
             */
            LinearLayout webIcon = (LinearLayout) convertView.findViewById(R.id.webIcon);
            webIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, drug.getDrugName() + " " + getString(R.string.search_intent_query));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }else{
                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.no_installed_software), Snackbar.LENGTH_SHORT);
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

    /**
     * Method displays a Toast message after the user either gives or denies permission to use the device's camera and files for reading and writing
     *
     * @param requestCode the code for the permission being asked
     * @param permissions permissions that the user being asked
     * @param grantResults granted or denied permissions, depending on the user's response to permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext().getApplicationContext(), getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext().getApplicationContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
