package com.crm.pharmbooks.PharmCRM;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactUs extends Fragment implements View.OnClickListener{

    TextView email,phone,address;
    private OnFragmentInteractionListener mListener;

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

        View rootView=inflater.inflate(R.layout.fragment_about_us, container, false);
        email=(TextView)rootView.findViewById(R.id.email);
        phone=(TextView)rootView.findViewById(R.id.phone);
        address=(TextView)rootView.findViewById(R.id.address);
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
            intent.putExtra(Intent.EXTRA_EMAIL,"hello@medicians.in");
            intent.putExtra(Intent.EXTRA_SUBJECT,"");
            intent.putExtra(Intent.EXTRA_TEXT,"");

            startActivity(Intent.createChooser(intent, "Send Email"));
        }

        else if(view==phone){

        }
        else if(view==address){

        }

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}

