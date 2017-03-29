package com.example.cdu.medstracker;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewPagerContentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewPagerContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPagerContentFragment extends Fragment {

    public static int fragmentCounter = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;

    private OnFragmentInteractionListener mListener;

    public ViewPagerContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3 Parameter 3.
     * @return A new instance of fragment ViewPagerContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPagerContentFragment newInstance(String param1, String param2, String param3) {
        ViewPagerContentFragment fragment = new ViewPagerContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);

//        fragmentCounter++;
//        Log.i("***fragmentCounter***:","" + fragmentCounter);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager_content, container, false);

        //gets image view for displaying images in the view pager
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewPager);

        //sets viewpager fragment background color and image, depending on the item number in the View pager
        switch(mParam1) {
            case "1":
                imageView.setImageResource(R.drawable.drug_man);
                view.setBackgroundColor(Color.rgb(90,155,150));
                break;
            case "2":
                imageView.setImageResource(R.drawable.drug_man);
                view.setBackgroundColor(Color.rgb(195,125,180));
                break;
            case "3":
                imageView.setImageResource(R.drawable.drug_man);
                view.setBackgroundColor(Color.rgb(240,104,59));
                break;
            case "4":
                imageView.setImageResource(R.drawable.drug_man);
                view.setBackgroundColor(Color.rgb(240,104,120));
                break;
            case "5":
                imageView.setImageResource(R.drawable.drug_man);
                view.setBackgroundColor(Color.rgb(195,130,120));
                break;
            default: break;
        }

        //sets text for View Pager content
        TextView headerTextView = (TextView) view.findViewById(R.id.textViewPagerHeader);
        headerTextView.setText(mParam2);
        TextView bodyTextView = (TextView) view.findViewById(R.id.textViewPagerBody);
        bodyTextView.setText(mParam3);

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
