package com.crm.pharmbooks.PharmCRM;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.crm.pharmbooks.PharmCRM.Login.MyPREFERENCES;

public class ContactUs extends Fragment implements View.OnClickListener{

    TextView email,phone,address;
    private OnFragmentInteractionListener mListener;
    SharedPreferences sharedpreferences;
    String restoredTextuser;

    public ContactUs() {
        // Required empty public constructor
    }

    public static ContactUs newInstance() {
        ContactUs fragment = new ContactUs();
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

        View rootView=inflater.inflate(R.layout.fragment_contact_us, container, false);
        email=(TextView)rootView.findViewById(R.id.email);
        phone=(TextView)rootView.findViewById(R.id.phone);
        address=(TextView)rootView.findViewById(R.id.address);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        restoredTextuser= sharedpreferences.getString("username", null);
        email.setText("hello@medicians.in");
        phone.setText("011-65808808");
        address.setText("Plot No.67, Near Dwarka Mor Metro Station,Gate No.2\nBlock A, Sewak Park, Dwarka, \nDelhi-1100059");
        email.setOnClickListener(this);
        phone.setOnClickListener(this);
        address.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {

        if(view==email){
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "hello@medicians.in" });
            //intent.putExtra(Intent.EXTRA_EMAIL,"hello@medicians.in");
            intent.putExtra(Intent.EXTRA_SUBJECT,"Query : ");
            intent.putExtra(Intent.EXTRA_TEXT,"Hello\nI "+restoredTextuser+" a user of your application...");

            //startActivity(Intent.createChooser(intent, "Send Email"));
            startActivity(intent);
        }

        else if(view==phone){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" +"011-65808808"));
            //startActivity(Intent.createChooser(intent, "Make a Call"));
            startActivity(intent);
        }
        else if(view==address){
            double lat =28.619043312672225;
            double  lng=77.03297664596334;
            String mTitle = "Medicians Software Solutions Pvt. Ltd.";
            String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + mTitle + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            startActivity(intent);

        }
    }




    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}

