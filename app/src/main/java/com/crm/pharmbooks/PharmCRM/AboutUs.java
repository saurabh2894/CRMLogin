package com.crm.pharmbooks.PharmCRM;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutUs extends Fragment {




    private OnFragmentInteractionListener mListener;
    TextView textView;

    public AboutUs() {
        // Required empty public constructor
    }

    public static AboutUs newInstance() {
        AboutUs fragment = new AboutUs();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        View rootView=inflater.inflate(R.layout.fragment_about_us, container, false);

        // Inflate the layout for this fragment
        textView=(TextView)rootView.findViewById(R.id.textview);
        textView.setText("Medicians is a healthcare innovation company founded in 2015.\nOur mission is to make Medicians preferred business solution company for small/medium scale companies in the pharmacy sector by delivering outstanding value, continuous innovation and exceptional user experience. \nPharmbooks is the POS/ERP software product of the company for Pharmacy stores. The software helps in Invoicing, Inventory Management and accounting along with a lot of Unique features exclusive to our software product, such as:\n1. Payment solution integration (with mSwipe)\n2. Pharmahelpline service\n3. Automatic purchase addition and inventory updation\nThe software is capable of doing everything a conventional solution being used does and also pushes the possibilities of value that can be extracted out of these business software. We ensure that our clients save a lot of time and money by using our solution and in exchange, they remunerate us in cash and data.\n\nPharmCRM is a solution to a very different problem for pharmacy stores of Customer Acquisition and retainment. With no real competitor out there, our proposition is for pharmacy stores to increase their cash inflow and number of loyal paying repeat customers. We offer features such as:\n 1. Regular Transactional Messages\n2. Campaigns and announcement messages\n3. Loyalty programs\n4. Prescription refilling\nThis solution can be purchased separately or with the POS software, so we have an entry point into the stores that already use our competitors' products in POS/ERP space. ");


        return rootView;
    }

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


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
