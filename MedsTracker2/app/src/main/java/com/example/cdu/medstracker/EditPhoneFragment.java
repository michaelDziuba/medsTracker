package com.example.cdu.medstracker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditPhoneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPhoneFragment extends Fragment {

    public static Phone phone = null;

    FragmentManager fm;

    private EditText editPhoneName;
    private EditText editPhoneNumber;
    private EditText editPhoneNote;

    private Button savePhoneButton;

    FloatingActionButton fab = MainActivity.fab;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EditPhoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPhoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPhoneFragment newInstance(String param1, String param2) {
        EditPhoneFragment fragment = new EditPhoneFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_phone, container, false);

        if(fab.isShown()){
            fab.setVisibility(View.INVISIBLE);
        }

//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

        fm = getChildFragmentManager();

        if(phone != null) {
            editPhoneName = (EditText) view.findViewById(R.id.editPhoneName);
            editPhoneName.setText(phone.getPhoneName());
            editPhoneNumber = (EditText) view.findViewById(R.id.editPhoneNumber);
            editPhoneNumber.setText(phone.getPhoneNumber());
            editPhoneNote = (EditText) view.findViewById(R.id.editPhoneNote);
            editPhoneNote.setText(phone.getPhoneNote());
        }

        savePhoneButton = (Button) view.findViewById(R.id.savePhoneButton);
        savePhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dismiss the keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm.isAcceptingText()) { // verify if the soft keyboard is open
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }

                phone.setPhoneName(editPhoneName.getText().toString());
                phone.setPhoneNumber(editPhoneNumber.getText().toString());
                phone.setPhoneNote(editPhoneNote.getText().toString());

                DatabaseHandler db = new DatabaseHandler(getContext());
                db.updatePhone(phone);

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
