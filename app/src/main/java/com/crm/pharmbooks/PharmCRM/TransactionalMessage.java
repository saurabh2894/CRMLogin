package com.crm.pharmbooks.PharmCRM;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.crm.pharmbooks.PharmCRM.Login.MyPREFERENCES;

public class TransactionalMessage extends android.support.v4.app.Fragment implements DashBoard.OnFragmentInteractionListener {

    EditText Name;
    EditText MobileNo;
    EditText BillAmount;
    Button Next;
    String Name_var, MobileNo_var, BillAmount_var;
    int result;
    String username;
    private OnFragmentInteractionListener mListener;


    public TransactionalMessage() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        String restoredText = sharedpreferences.getString("username", null);
        if (restoredText != null) {
            username = sharedpreferences.getString("username", "No name defined");//"No name defined" is the default value.
        }



        View view = inflater.inflate(R.layout.activity_transactional_message, container, false);
        Name = (EditText) view.findViewById(R.id.Name);
        MobileNo = (EditText) view.findViewById(R.id.MobileNo);
        BillAmount = (EditText) view.findViewById(R.id.BillAmount);
        Next = (Button) view.findViewById(R.id.Next);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((!(TextUtils.isEmpty(Name.getText())) && !(TextUtils.isEmpty(MobileNo.getText().toString().trim()))&& !(TextUtils.isEmpty(BillAmount.getText().toString().trim()) ))) {
                    Name_var = Name.getText().toString();
                    MobileNo_var = MobileNo.getText().toString();
                    BillAmount_var = BillAmount.getText().toString();
                    Toast.makeText(getActivity().getApplicationContext(), username, Toast.LENGTH_LONG).show();
                    sendR(Name_var, MobileNo_var, BillAmount_var);
                    // Sending the sms here
                    SendMsg sendMsg = new SendMsg(Name_var,MobileNo_var,BillAmount_var);


                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter Some Values!!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
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
    public void onFragmentInteraction(Uri uri) {

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void sendR(final String Name_var, final String MobileNo_var, final String BillAmount_var){


        String url = "https://pharmcrm.herokuapp.com/api/transactional/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String msg = null;
                        try {
                            JSONObject object = new JSONObject(response);
                            msg = object.getString("msg");
                            result = object.getInt("res");
                            if(result == 1)
                            {

                                //SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                //SharedPreferences.Editor editor = sharedpreferences.edit();

                                //editor.putString("Name", String.valueOf(Name));
                                //editor.commit();

                                //Intent i = new Intent(getApplicationContext(),MedicineData.class);
                                //startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity().getApplicationContext(),msg+""+ result +"",Toast.LENGTH_LONG).show();


                        //Dashboard switcing from here
                        //MainActivity.LOAD_FRAG_TAG="";
                        getActivity().finish();
                        startActivity(new Intent(getActivity(),MainActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){




                Map<String, String> params = new HashMap<>();
                params.put("name",Name_var);
                params.put("number",MobileNo_var);
                params.put("amount",BillAmount_var);
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