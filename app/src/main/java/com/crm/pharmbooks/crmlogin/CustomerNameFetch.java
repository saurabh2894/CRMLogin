package com.crm.pharmbooks.crmlogin;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;

import Adapters.CustomerAdapter;
import Model.*;

public class CustomerNameFetch extends AppCompatActivity {

    private ArrayList<CustomerDetails> customerDetailsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomerAdapter mAdapter;
    ProgressBar pb;
    TextView txt;
    String name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_name_fetch);
        recyclerView = (RecyclerView) findViewById(R.id.re);
        pb= (ProgressBar) findViewById(R.id.pb) ;
        txt=(TextView) findViewById(R.id.loadingtxt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        recyclerView.setVisibility(View.GONE);

        mAdapter = new CustomerAdapter(customerDetailsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // set the adapter
        recyclerView.setAdapter(mAdapter);
        getSupportActionBar().setTitle("Existing Customers");
        String url = "https://pharmcrm.herokuapp.com/api/namedata/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                        try {
                            JSONArray jarr = new JSONArray(response);
                            for(int i=0;i<jarr.length();i++){
                                JSONObject ob1 = jarr.getJSONObject(i);
                                 String name1= ob1.getString("custmorname");
                                String phone1 = ob1.getString("custmornumber");
                                Log.v("name",name1);
                                Log.v("phone",phone1);
                                CustomerDetails detail = new CustomerDetails(name1,phone1);
                                customerDetailsList.add(detail);
                                makeVisible();
                                //customerDetailsList.add(new CustomerDetails(name1,phone1));
                            }

                        }
                           catch (JSONException e) {
                            e.printStackTrace();
                        }

                mAdapter.notifyDataSetChanged();
                Log.d("mytag",customerDetailsList.get(1).getCName());

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("chemist", "balkeerat");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerNameFetch.this);
        requestQueue.add(stringRequest);

    }

    private void makeVisible(){
        recyclerView.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
        txt.setVisibility(View.GONE);
    }

    }

