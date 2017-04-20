package com.example.cdu.medstracker;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListViewPhoneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListViewPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewPhoneFragment extends Fragment {

    /**
     * colorCode value is injected from the MainActivity, when the user selects a setting for Card Color in the Settings Menu
     */
    public static int colorCode;

    /**
     * Permission properties for asking the user for corresponding permissions
     */
    private static final int REQUEST_CODE = 0x12;
    String[] permissions = {"android.permission.CALL_PHONE"};

    /**
     *  Fragment manager used for taking the user to the CreateDrugFragment view
     */
    FragmentManager fm;

    /**
     * Floating Action Button from the MainActivity
     */
    FloatingActionButton fab = MainActivity.fab;

    /**
     * CardView text fields for displaying information about a phone number
     */
    ListView phoneListView;
    TextView phoneNameTextView;
    TextView phoneNumberTextView;
    TextView phoneNoteTextView;

    /**
     * CardView icons for the user to tap on
     */
    LinearLayout deletePhoneIcon;
    LinearLayout editPhoneIcon;
    LinearLayout phoneIcon;

    /**
     * Contains phone objects from which all phone CardViews are created
     */
    public static ArrayList<Phone> phoneArrayList;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListViewPhoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListViewPhoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListViewPhoneFragment newInstance(String param1, String param2) {
        ListViewPhoneFragment fragment = new ListViewPhoneFragment();
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
     * Creates the ListViewPhoneFragment view for the app
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
        View view = inflater.inflate(R.layout.fragment_list_view_phone, container, false);

        /**
         * Show the Floating Action Button, if it's not shown
         */
        if(!fab.isShown()){
            fab.setVisibility(View.VISIBLE);
        }

        /**
         * Asks the user for permission to access phone on the user's device
         */
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE);

        /**
         * Floating Action Button with attached click listener for taking the user to the CreatePhoneFragment form
         */
        fm = getActivity().getSupportFragmentManager();
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.content_main, new CreatePhoneFragment());
                ft.commit();
            }
        });

        /**
         * Gets an arrayList of phone object for all phone numbers stored in the database phones table
         */
        phoneListView = (ListView) view.findViewById(R.id.phoneListView);
        DatabaseHandler db = new DatabaseHandler(getContext());
        phoneArrayList = db.getAllPhones();
        db.closeDB();


        /**
         * Sets an adapter for creating and displaying phone CardViews in the ListViewPhoneFragment
         */
        final ListViewPhoneFragment.CustomAdapter adapter = new ListViewPhoneFragment.CustomAdapter(getContext(), phoneArrayList);
        phoneListView.setAdapter(adapter);

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
    public class CustomAdapter extends ArrayAdapter<Phone> {

        public CustomAdapter(Context context, ArrayList<Phone> items) {
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
            final Phone phone = getItem(positionFinal);

            /**
             * Show the Floating Action Button, if it's not shown
             * (shows the fab button after the user returns to the ListViewFragment using the phone's back button)
             */
            if(!fab.isShown()){
                fab.setImageResource(R.drawable.ic_add_black_24dp);
                fab.setVisibility(View.VISIBLE);
            }

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.phone_item_view, parent, false);
            }

            /**
             * Gets all the CardView's TextViews
             */
            CardView phoneCardView = (CardView) convertView;
            TextView textView1 = (TextView) convertView.findViewById(R.id.phoneNameTextView1);
            TextView textView2 = (TextView) convertView.findViewById(R.id.phoneNameTextView);
            TextView textView3 = (TextView) convertView.findViewById(R.id.phoneTextView2);
            TextView textView4 = (TextView) convertView.findViewById(R.id.phoneNumberTextView);
            TextView textView5 = (TextView) convertView.findViewById(R.id.phoneTextView3);
            TextView textView6 = (TextView) convertView.findViewById(R.id.phoneNoteTextView);

            /**
             * Gets all the CardView's icons
             */
            ImageView imageView1 = (ImageView) convertView.findViewById(R.id.phoneDeleteImageView);
            ImageView imageView2 = (ImageView) convertView.findViewById(R.id.phoneEditImageView);
            ImageView imageView3 = (ImageView) convertView.findViewById(R.id.phoneTalkImageView);

            /**
             * Sets the CardView's color for text, icons, and background, depending on user selected setting in the Settings Menu
             */
            switch(colorCode){
                case 0:
                    phoneCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.whiteCardView));

                    textView1.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView2.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView3.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView4.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView5.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));
                    textView6.setTextColor(ContextCompat.getColor(getContext(), R.color.blackTextColor));

                    imageView1.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_black_24dp));
                    imageView2.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_black_24dp));
                    imageView3.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_phone_in_talk_black_24dp));

                    break;
                case 1:
                    phoneCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.brown));

                    textView1.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView2.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView3.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView4.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView5.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                    textView6.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));

                    imageView1.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_white_24dp));
                    imageView2.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_white_24dp));
                    imageView3.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_phone_in_talk_white_24dp));

                    break;
                default: break;

            }

            /**
             * Sets the text values for the CardView
             */
            phoneNameTextView = (TextView) convertView.findViewById(R.id.phoneNameTextView);
            phoneNameTextView.setText(phone.getPhoneName());

            phoneNumberTextView = (TextView) convertView.findViewById(R.id.phoneNumberTextView);
            phoneNumberTextView.setText(phone.getPhoneNumber());

            phoneNoteTextView = (TextView) convertView.findViewById(R.id.phoneNoteTextView);
            phoneNoteTextView.setText(phone.getPhoneNote());

            /**
             * Delete icon with click listener, which deletes the CardView, after asking the user for confirmation
             * with an alert pop-up dialogue
             */
            deletePhoneIcon = (LinearLayout) convertView.findViewById(R.id.deletePhoneIcon);
            deletePhoneIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle(getString(R.string.delete_phone_confirm));
                    //alert.setMessage("");
                    alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseHandler db = new DatabaseHandler(getContext());
                            int phone_id = phone.getId();
                            db.deletePhone(phone_id);
                            db.closeDB();


                            phoneArrayList.remove(positionFinal);
                            phoneListView.setAdapter(phoneListView.getAdapter());

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
            editPhoneIcon = (LinearLayout) convertView.findViewById(R.id.editPhoneIcon);
            editPhoneIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i("**EditButton**", "Clicked!");
                    EditPhoneFragment.phone = phone;
                    ((MainActivity)getActivity()).goToEditPhone();
                }
            });


            /**
             * editIcon with click listener that takes the user to the device's phone, with the phone number already entered
             */
            phoneIcon = (LinearLayout) convertView.findViewById(R.id.phoneIcon);
            phoneIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone.getPhoneNumber()));
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
        phoneListView.setAdapter(phoneListView.getAdapter());
    }




    /**
     * Method displays a Toast message after the user either gives or denies permission to use the device's phone
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
