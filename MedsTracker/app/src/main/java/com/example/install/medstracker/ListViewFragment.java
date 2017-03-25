package com.example.install.medstracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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



    FragmentManager fm;
    FloatingActionButton fab = MainActivity.fab;
    ListView bookList;
    TextView bookDescriptionTextView;
    LinearLayout imagesLayout;


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

        bookList = (ListView) view.findViewById(R.id.bookListView);
        DatabaseHandler db = new DatabaseHandler(getContext());
        final ArrayList<Drug> booksArrayList = db.getAllBooks();
        //final ArrayList<Image> images = db.getAllImages();


        db.closeDB();
        final CustomAdapter adapter = new CustomAdapter(getContext(), booksArrayList);
        bookList.setAdapter(adapter);
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView details = (TextView) view.findViewById(R.id.details);

                imagesLayout =  (LinearLayout) view.findViewById(R.id.imagesLayout);

                ImageView chevron = (ImageView) view.findViewById(R.id.chevron);


                 if(imagesLayout.getVisibility() == View.GONE || imagesLayout.getVisibility() == View.INVISIBLE){
                     imagesLayout.setVisibility(View.VISIBLE);

                    //update the text of the show more
                    details.setText("Click to show less");
                    //update the chevron image
                    chevron.setImageResource(R.drawable.ic_expand_less_black_24dp);

                }
                else{
                     imagesLayout.setVisibility(View.GONE);
                    //update the text of the show more
                    details.setText("Click to show more");
                    //update the chevron image
                    chevron.setImageResource(R.drawable.ic_expand_more_black_24dp);
                }
            }
        });

        bookList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseHandler db = new DatabaseHandler(getContext());
                Drug book = booksArrayList.get(position);
                int book_id = book.getId();

                db.deleteBook(book_id);
                db.deleteImage(book_id);
                db.closeDB();


                booksArrayList.remove(position);
                bookList.setAdapter(bookList.getAdapter());

                return false;
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
            final Drug book = getItem(position);

            if(!fab.isShown()){
                fab.setImageResource(R.drawable.ic_add_black_24dp);
                fab.setVisibility(View.VISIBLE);
            }

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_view, parent, false);
            }

            imagesLayout = (LinearLayout) convertView.findViewById(R.id.imagesLayout);
            imagesLayout.setVisibility(View.GONE);
            if(imagesLayout.getChildCount() == 0){
                //Grab all the photos that match the id of the current location
                DatabaseHandler db = new DatabaseHandler(getContext());
                ArrayList<Image> bookImages = db.getAllImages(book.getId());
                db.closeDB();

                //Add those photos to the gallery
                for(int i =0; i < bookImages.size(); i++){
                    Bitmap image = BitmapFactory.decodeFile(bookImages.get(i).getResource());

                    Log.i("***Image Size Bytes***", "" + image.getByteCount());

                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageBitmap(image);
                    imageView.setAdjustViewBounds(true);
                    imageView.setPadding(0,20,0,20);
                    imagesLayout.addView(imageView);
                }
            }



            bookDescriptionTextView = (TextView) convertView.findViewById(R.id.description);
            bookDescriptionTextView.setText(book.getDescription());

            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(book.getTitle());
            ImageView image = (ImageView) convertView.findViewById(R.id.intentIcon);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri webpage = Uri.parse(book.getUrl());
                   //Uri webpage = Uri.parse("http://www.doctorwho.tv/");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(webpage);
                    //startActivity(intent);
                    if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                        startActivity(intent);
                    }

                }
            });

            return  convertView;
        }
    }





}
