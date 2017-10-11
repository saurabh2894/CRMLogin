
/**
 * Created by Dell on 30-Aug-17.
 */

package com.crm.pharmbooks.PharmCRM;

import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;


public class NewFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    Button addNew,repeat,existing,customer;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new, container, false);
        existing = (Button) rootView.findViewById(R.id.existing);
        repeat = (Button) rootView.findViewById(R.id.repeat);
        customer = (Button) rootView.findViewById(R.id.customer);

        existing.setOnClickListener(this);
        repeat.setOnClickListener(this);
        customer.setOnClickListener(this);
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
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

        if(view == existing){
            startActivity(new Intent(getActivity(),PrescriptionListActivity.class));
        }
        else if(view == customer){
            startActivity(new Intent(getActivity(),CustomerDetail.class));
        }
        else if(view == repeat){
            startActivity(new Intent(getActivity(),RefillListActivity.class));
        }
    }



    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
