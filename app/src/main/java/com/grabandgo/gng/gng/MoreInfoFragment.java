package com.grabandgo.gng.gng;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoreInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreInfoFragment extends Fragment {

    private Button OkBtn;
    private ImageView headerImageView, catImg1, catImg2, catImg3;
    private TextView titleTextView, catTxt1, catTxt2, catTxt3;
    private ScrollView scrollView;

    View myView;


    public static MoreInfoFragment newInstance(String param1, String param2) {
        MoreInfoFragment fragment = new MoreInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public MoreInfoFragment() {
        // Required empty public constructor
    }

    private OnOkClickedListener mListener;


    public interface OnOkClickedListener {
        public void onOkClickedListener();
    }


    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnOkClickedListener) getActivity();
            headerImageView = (ImageView) getView().findViewById(R.id.header_image);
            headerImageView.setClipToOutline(true);
            titleTextView = (TextView)getView().findViewById(R.id.title_text_tv);


            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeueLight.ttf");

            titleTextView.setTypeface(font);



        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmwwent

        View view =  inflater.inflate(R.layout.fragment_more_info, container, false);
        OkBtn = (Button)view.findViewById(R.id.more_info_ok_btn);
        OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onOkClickedListener();
            }
        });

        catImg1 = (ImageView)view.findViewById(R.id.imageView2);
        catImg2 = (ImageView)view.findViewById(R.id.imageView3);
        catImg3 = (ImageView)view.findViewById(R.id.imageView4);


        catTxt1 = (TextView)view.findViewById(R.id.cat_txt_1);
        catTxt2 = (TextView)view.findViewById(R.id.cat_txt_2);
        catTxt3 = (TextView)view.findViewById(R.id.cat_txt_3);



        scrollView = (ScrollView)view.findViewById(R.id.scrollView);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                float scaledScroll = (float)scrollY/200;

                if(scaledScroll <= 1) {
                    headerImageView.setScaleX(1 - scaledScroll);
                    headerImageView.setScaleY(1 - scaledScroll);
                    titleTextView.setAlpha(1 - scaledScroll+0.2f);
                }

                if(scrollY > 250){
                    float newY = (float)scrollY - 250;
                    float newAlpa = (float)newY/200;
                    catImg1.setAlpha(1-newAlpa);
                    catImg2.setAlpha(1-newAlpa);
                    catImg3.setAlpha(1-newAlpa);
                    catTxt1.setAlpha(1-newAlpa);
                    catTxt2.setAlpha(1-newAlpa);
                    catTxt3.setAlpha(1-newAlpa);
                }


            }
        });

        myView = view;
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
