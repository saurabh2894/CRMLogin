package com.crm.pharmbooks.PharmCRM;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.net.InetAddress;

/*public class DashBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
    }
}*/



public class DashBoard extends Fragment  implements View.OnClickListener{


    private OnFragmentInteractionListener mListener;
    Button NoOfCustomersToBeRefilled,NoOFCustomersOnboard,NoOfRepeatedCustomers,NoOfMedicinesRefilled;

    public DashBoard() {
        // Required empty public constructor
    }

    public static DashBoard newInstance() {
        DashBoard fragment = new DashBoard();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public boolean isInternetAvailable(){
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.activity_dash_board, container, false);
        NoOfCustomersToBeRefilled = (Button) rootView.findViewById(R.id.NoOfCustomersToBeRefilled);
        NoOFCustomersOnboard = (Button) rootView.findViewById(R.id.NoOFCustomersOnboard);
        NoOfRepeatedCustomers = (Button) rootView.findViewById(R.id.NoOfRepeatedCustomers);
        NoOfMedicinesRefilled = (Button) rootView.findViewById(R.id.NoOfMedicinesRefilled);

        NoOfCustomersToBeRefilled.setOnClickListener(this);
        NoOFCustomersOnboard.setOnClickListener(this);
        NoOfRepeatedCustomers.setOnClickListener(this);
        NoOfMedicinesRefilled.setOnClickListener(this);
        boolean b =isInternetAvailable();
        if(!b){
            Toast.makeText(getActivity().getApplicationContext(), "This application requires internet...Please connect to a WiFi or Mobile Network", Toast.LENGTH_SHORT).show();

        }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
