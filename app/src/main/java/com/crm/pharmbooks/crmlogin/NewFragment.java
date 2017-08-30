package com.crm.pharmbooks.crmlogin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class NewFragment extends Fragment implements View.OnClickListener,MedicineData.OnFragmentInteractionListener {

    private OnFragmentInteractionListener mListener;
    Button addNew,repeat,existing;

    public NewFragment() {
        // Required empty public constructor
    }

    public static NewFragment newInstance() {
        NewFragment fragment = new NewFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new, container, false);
        addNew = (Button) rootView.findViewById(R.id.addnew);
        existing = (Button) rootView.findViewById(R.id.existing);
         repeat = (Button) rootView.findViewById(R.id.repeat);
        addNew.setOnClickListener(this);
        existing.setOnClickListener(this);
        repeat.setOnClickListener(this);
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
        if(view==addNew){
            Fragment fragment = new MedicineData();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, "Refill");
            fragmentTransaction.commitAllowingStateLoss();
        }
        else if(view == existing){
            startActivity(new Intent(getActivity(),CustomerNameFetch.class));
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
