package com.example.cdu.medstracker;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditDrugFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditDrugFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDrugFragment extends Fragment {

    /**
     * Property for injecting a drug object to be edited in this fragment
     * The drug object is injected from a drug CardView, when the user taps on its edit icon
     */
    public static Drug drug = null;

    /**
     *  Fragment manager used for returning from this fragment to the  previous page
     */
    FragmentManager fm;

    /**
     * EditText properties contain values to be edited for Drug CardViews
     */
    private EditText editDrugName;
    private EditText editDrugDose;
    private EditText editDrugWhen;
    private EditText editDrugNotes;

    /**
     * Button for deleting photos
     */
    private LinearLayout removePhotoIcon;

    /**
     * Button for saving edited information
     */
    private Button saveButton;

    /**
     * Floating Action Button from the MainActivity
     */
    FloatingActionButton fab = MainActivity.fab;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EditDrugFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditDrugFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditDrugFragment newInstance(String param1, String param2) {
        EditDrugFragment fragment = new EditDrugFragment();
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
     * Creates the Edit Drug Page view for the app
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
        View view = inflater.inflate(R.layout.fragment_edit_drug, container, false);

        /**
         * Hide the Floating Action Button, if it's shown
         */
        if(fab.isShown()){
            fab.setVisibility(View.INVISIBLE);
        }


        fm = getChildFragmentManager();

        /**
         * Sets the existing values for the drug object to be edited in the corresponding EditText fields
         */
        if(drug != null) {
            editDrugName = (EditText) view.findViewById(R.id.editDrugName);
            editDrugName.setText(drug.getDrugName());
            editDrugDose = (EditText) view.findViewById(R.id.editDrugDose);
            editDrugDose.setText(drug.getDrugDose());
            editDrugWhen = (EditText) view.findViewById(R.id.editDrugWhen);
            editDrugWhen.setText(drug.getWhenToTake());
            editDrugNotes = (EditText) view.findViewById(R.id.editDrugNotes);
            editDrugNotes.setText(drug.getNotes());
        }

        /**
         * Button with click listener to remove photo(s) for the drug object to be edited
         */
        removePhotoIcon = (LinearLayout) view.findViewById(R.id.removePhotoIcon);
        removePhotoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Alert popup asks the user to confirm deletion of photo(s) for the drug object
                 */
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(getString(R.string.delete_photo));
                alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /**
                         * Deletes photos from the images table in the database and the corresponding image files in memory
                         */
                        DatabaseHandler db = new DatabaseHandler(getContext());
                        ArrayList<Image> drugImages = db.getAllImages(drug.getId());
                        for(int i = 0; i < drugImages.size(); i++){
                            Image image = drugImages.get(i);
                            File file = new File(image.getResource());
                            file.delete();
                        }
                        db.deleteImage(drug.getId());
                        db.closeDB();
                        dialog.dismiss();

                        /**
                         * Toast message for the user to inform of successful deletion of photos
                         */
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.photo_deleted), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                });

                /**
                 * Dismisses the alert pop-up, if the user clicks 'NO' on the pop-up
                 */
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
         * Button with click listener for saving edited information in the database and returning the user to the previous view (ListViewFragment)
         */
        saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Dismiss the keyboard
                 */
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm.isAcceptingText()) { // verify if the soft keyboard is open
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
                /**
                 * Sets the new values for the drug object and saves its edited information in the drugs table
                 */
                drug.setDrugName(editDrugName.getText().toString());
                drug.setDrugDose(editDrugDose.getText().toString());
                drug.setWhenToTake(editDrugWhen.getText().toString());
                drug.setNotes(editDrugNotes.getText().toString());

                DatabaseHandler db = new DatabaseHandler(getContext());
                db.updateDrug(drug);

                db.closeDB();
                fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
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
}
