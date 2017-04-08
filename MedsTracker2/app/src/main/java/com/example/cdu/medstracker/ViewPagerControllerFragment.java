package com.example.cdu.medstracker;


import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.cdu.medstracker.transforms.DepthPageTransformer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewPagerControllerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewPagerControllerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPagerControllerFragment extends Fragment {

    private SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;

    public ViewPagerContentFragment[] viewPagerContentFragments;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ViewPagerControllerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPagerControllerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPagerControllerFragment newInstance(String param1, String param2) {
        ViewPagerControllerFragment fragment = new ViewPagerControllerFragment();
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
        viewPagerContentFragments = new ViewPagerContentFragment[]{
                ViewPagerContentFragment.newInstance("1", getActivity().getString(R.string.drug_faqs_header), getActivity().getString(R.string.drug_faqs_text)),
                ViewPagerContentFragment.newInstance("2", getActivity().getString(R.string.drug_interactions_header), getActivity().getString(R.string.drug_interactions_text)),
                ViewPagerContentFragment.newInstance("3", getActivity().getString(R.string.drug_tips_header), getActivity().getString(R.string.drug_tips_text)),
                ViewPagerContentFragment.newInstance("4", getActivity().getString(R.string.drug_side_effects_header), getActivity().getString(R.string.drug_side_effects_text)),
                ViewPagerContentFragment.newInstance("5", getActivity().getString(R.string.drug_disposal_header), getActivity().getString(R.string.drug_disposal_text))
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_view_pager_controller, container, false);

        if(MainActivity.fab.isShown()){
            MainActivity.fab.setVisibility(View.INVISIBLE);
        }

        FragmentManager fm = getChildFragmentManager();

        //Sets ViewPager adapter
        sectionsPagerAdapter = new SectionsPagerAdapter(fm);
        viewPager = (ViewPager) view.findViewById(R.id.viewPagerContent);

        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(1000);
        viewPager.setOffscreenPageLimit(0);

        //Sets animation for View Pager
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        ImageButton leftButton = (ImageButton) view.findViewById(R.id.left_nav);
        ImageButton rightButton = (ImageButton) view.findViewById(R.id.right_nav);

        //simulates swipe gesture across the screen from left to right
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.beginFakeDrag();
                viewPager.fakeDragBy(view.getWidth() * 0.75f);
                viewPager.endFakeDrag();
            }
        });

        //simulates swipe gesture across the screen from right to left
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.beginFakeDrag();
                viewPager.fakeDragBy(-view.getWidth() * 0.75f);
                viewPager.endFakeDrag();
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
    public void onDestroy(){
        super.onDestroy();
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


    //View Pager adapter for displaying view pager items
    //Changed extends FragmentPagerAdapater to FragmentStatePagerAdapter
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //cycles through the view pager fragments array
            return  viewPagerContentFragments[position % 5];
        }

        @Override
        public int getCount() {
            //returns a value to allow practically endless looping of the viewpager fragments
            return 1999;
        }
    }


}
