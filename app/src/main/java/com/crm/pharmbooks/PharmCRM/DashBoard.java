package com.crm.pharmbooks.PharmCRM;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.ExpandableListAdapterDashboard;

import static android.content.Context.MODE_PRIVATE;
import static com.crm.pharmbooks.PharmCRM.Login.MyPREFERENCES;


/**
 * Created by Dell on 18/09/17.
 */

public class DashBoard extends Fragment  implements View.OnClickListener{


    private OnFragmentInteractionListener mListener;
    Button NoOfCustomersToBeRefilled,NoOFCustomersOnboard,NoOfRepeatedCustomers,NoOfMedicinesRefilled,VisitInfo;
    TextView listHeading;
    String username;
    ExpandableListAdapterDashboard listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeaderValue;
    HashMap<String, List<String>> listDataChild_Value;
    String Noofuserstoberefilled,Noofusers,Noofmedicines;
    ArrayList<String> visitinfolist = new ArrayList<String>();
    int flag=0;
    int noOfRepeatedCustomer=0;
    ProgressBar pb;
    TextView txt;
    RelativeLayout rl,r2;


    public DashBoard() {
        // Required empty public constructor
    }

    public static DashBoard newInstance() {
        DashBoard fragment = new DashBoard();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fetchListData();
        fetchDashboardData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        String restoredText = sharedpreferences.getString("username", null);
        if (restoredText != null) {
            username = sharedpreferences.getString("username", "No name defined");//"No name defined" is the default value.
        }



        View rootView = inflater.inflate(R.layout.activity_dash_board, container, false);

        rl=(RelativeLayout)rootView.findViewById(R.id.rel);
        r2=(RelativeLayout)rootView.findViewById(R.id.empty_list);
        pb= (ProgressBar) rootView.findViewById(R.id.pb) ;
        txt=(TextView) rootView.findViewById(R.id.loadingtxt);

        NoOfCustomersToBeRefilled = (Button) rootView.findViewById(R.id.NoOfCustomersToBeRefilled);
        NoOFCustomersOnboard = (Button) rootView.findViewById(R.id.NoOFCustomersOnboard);
        NoOfRepeatedCustomers = (Button) rootView.findViewById(R.id.NoOfRepeatedCustomers);
        NoOfMedicinesRefilled = (Button) rootView.findViewById(R.id.NoOfMedicinesRefilled);
        VisitInfo= (Button) rootView.findViewById(R.id.VisitInfo);
        VisitInfo.setVisibility(View.GONE);


        //r2.setVisibility(View.GONE);
        NoOfCustomersToBeRefilled.setOnClickListener(this);
        NoOFCustomersOnboard.setOnClickListener(this);
        NoOfRepeatedCustomers.setOnClickListener(this);
        NoOfMedicinesRefilled.setOnClickListener(this);
        VisitInfo.setOnClickListener(this);
        NoOfCustomersToBeRefilled.setVisibility(View.GONE);
        NoOFCustomersOnboard.setVisibility(View.GONE);
        NoOfRepeatedCustomers.setVisibility(View.GONE);
        NoOfMedicinesRefilled.setVisibility(View.GONE);

        boolean b =isNetworkAvailable();
        if(b==false){
            Toast.makeText(getActivity().getApplicationContext(), "This application requires internet...Please connect to a WiFi or Mobile Network", Toast.LENGTH_LONG).show();
        }
        return rootView;



    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);


        listAdapter = new ExpandableListAdapterDashboard(getActivity(), listDataHeaderValue, listDataChild_Value);
        expListView.setAdapter(listAdapter);


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
        if(v == VisitInfo){
            Intent intent = new Intent(getActivity(), VisitorInformation.class);
            intent.putStringArrayListExtra("visitinfolist", (ArrayList<String>) visitinfolist);
            startActivity(intent);
        }

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }


    public void fetchListData(){

        String url = "https://pharmcrm.herokuapp.com/api/latestlist/";

        listDataHeaderValue = new ArrayList<String>();
        listDataChild_Value = new HashMap<String, List<String>>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                
                expListView.setVisibility(View.VISIBLE);
                rl.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for(int groupPosition =0; groupPosition<jsonArray.length();groupPosition++) {


                        JSONObject object = jsonArray.getJSONObject(groupPosition);
                        String name = object.getString("custmorname");
                        String phone = object.getString("custmornumber");

                        listDataHeaderValue.add(name + " : "+ phone);
                        JSONArray array1 = object.getJSONArray("data");
                        List<String> prescriptionlist = new ArrayList<String>();
                        for(int i=0;i<array1.length();i++) {
                            flag=1;
                            r2.setVisibility(View.GONE);
                            JSONObject dataobject = array1.getJSONObject(i);
                            String medicineName = dataobject.getString("medicinename");
                            String dosageenddate = dataobject.getString("dosageenddate");
                            String medicineid = dataobject.getString("id");
                            String prescriptionid = dataobject.getString("prescid");

                            prescriptionlist.add("Medicine Name: " + medicineName + "\nDosage End Date: " + dosageenddate);
                            listDataChild_Value.put(listDataHeaderValue.get(groupPosition), prescriptionlist);
                            Log.d("yolo",medicineid +"    "+prescriptionid);
                        }
                    }
                    Log.d("dashboardlistcheck",response+"");
                    Log.d("parent valuecheck",listDataHeaderValue+"");
                    Log.d("child valuecheck",listDataChild_Value+"");
                    listAdapter.notifyDataSetChanged();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("chemist",username);
                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 50000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
//        if(flag==0){
//            expListView.setVisibility(View.GONE);
//            r2.setVisibility(View.VISIBLE);
//        }
    }

    public void fetchDashboardData(){

            String url = "https://pharmcrm.herokuapp.com/api/dashboard/";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                        JSONObject object = new JSONObject(response);
                        Noofuserstoberefilled = object.getString("no_of_user_toberi");
                        Noofusers = object.getString("no_of_user");
                        Noofmedicines = object.getString("no_of_medicine_toberi");
                        JSONArray array1 = object.getJSONArray("visitinfo");

                        for(int i=0;i<array1.length();i++) {


                            JSONObject dataobject = array1.getJSONObject(i);
                            String custmorname = dataobject.getString("custmorname");
                            String custmornumber = dataobject.getString("custmornumber");
                            String No_of_Visit = dataobject.getString("No_of_Visit");

                            visitinfolist.add("Customer Name: " +custmorname + "\nCustomer Number: " + custmornumber+ "\nNo of visit: " + No_of_Visit);
                            if(Integer.parseInt(No_of_Visit)>1){
                                noOfRepeatedCustomer++;
                            }

                        }

                        NoOfCustomersToBeRefilled.setText(Noofuserstoberefilled);
                        NoOFCustomersOnboard.setText(Noofusers);
                        NoOfRepeatedCustomers.setText(noOfRepeatedCustomer+"");
                        NoOfMedicinesRefilled.setText(Noofmedicines);
                    NoOfCustomersToBeRefilled.setVisibility(View.VISIBLE);
                    NoOFCustomersOnboard.setVisibility(View.VISIBLE);
                    NoOfRepeatedCustomers.setVisibility(View.VISIBLE);
                    NoOfMedicinesRefilled.setVisibility(View.VISIBLE);
                        Log.d("response",response+"");
                        Log.d("visitinfolist",visitinfolist+"");




                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("chemist",username);
                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 50000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }
}
