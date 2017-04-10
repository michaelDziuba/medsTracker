package com.example.cdu.medstracker;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

    private static final int REQUEST_CODE = 0x12;
    String[] permissions = {"android.permission.CALL_PHONE"};

    FragmentManager fm;
    FloatingActionButton fab = MainActivity.fab;

    ListView phoneListView;
    TextView phoneNameTextView;
    TextView phoneNumberTextView;
    TextView phoneNoteTextView;

    LinearLayout deletePhoneIcon;
    LinearLayout editPhoneIcon;
    LinearLayout phoneIcon;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_view_phone, container, false);

        if(!fab.isShown()){
            fab.setVisibility(View.VISIBLE);
        }

        //Asks the user for permission to access phone on the user's device
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE);

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

        phoneListView = (ListView) view.findViewById(R.id.phoneListView);
        DatabaseHandler db = new DatabaseHandler(getContext());
        phoneArrayList = db.getAllPhones();
        db.closeDB();

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

            if(!fab.isShown()){
                fab.setImageResource(R.drawable.ic_add_black_24dp);
                fab.setVisibility(View.VISIBLE);
            }

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.phone_item_view, parent, false);
            }



            phoneNameTextView = (TextView) convertView.findViewById(R.id.phoneNameTextView);
            phoneNameTextView.setText(phone.getPhoneName());

            phoneNumberTextView = (TextView) convertView.findViewById(R.id.phoneNumberTextView);
            phoneNumberTextView.setText(phone.getPhoneNumber());

            phoneNoteTextView = (TextView) convertView.findViewById(R.id.phoneNoteTextView);
            phoneNoteTextView.setText(phone.getPhoneNote());

           deletePhoneIcon = (LinearLayout) convertView.findViewById(R.id.deletePhoneIcon);
            deletePhoneIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Delete this phone?");
                    //alert.setMessage("");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
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

                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                }
            });


            editPhoneIcon = (LinearLayout) convertView.findViewById(R.id.editPhoneIcon);
            editPhoneIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i("**EditButton**", "Clicked!");
                    EditPhoneFragment.phone = phone;
                    ((MainActivity)getActivity()).goToEditPhone();
                }
            });


            phoneIcon = (LinearLayout) convertView.findViewById(R.id.phoneIcon);
            phoneIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone.getPhoneNumber()));
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
