package com.crm.pharmbooks.crmlogin;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import static com.crm.pharmbooks.crmlogin.Login.MyPREFERENCES;

import Adapters.CustomerAdapter;
import Model.*;

public class CustomerNameFetch extends AppCompatActivity {

    private ArrayList<CustomerDetailModel> customerDetailsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomerAdapter cAdapter;
    private EditText SearchBoxExistingRefill;
    ProgressBar pb;
    TextView txt;
    String name, phone;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_name_fetch);
        recyclerView = (RecyclerView) findViewById(R.id.re);
        pb= (ProgressBar) findViewById(R.id.pb) ;
        txt=(TextView) findViewById(R.id.loadingtxt);
        SearchBoxExistingRefill = (EditText) findViewById(R.id.SearchBoxExistingRefill);
        SharedPreferences sharedpreferences = this.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        String restoredText = sharedpreferences.getString("username", null);
        if (restoredText != null) {
            username = sharedpreferences.getString("username", "No name defined");//"No name defined" is the default value.
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        recyclerView.setVisibility(View.GONE);

        cAdapter = new CustomerAdapter(customerDetailsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // set the adapter
        recyclerView.setAdapter(cAdapter);
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
                                CustomerDetailModel detail = new CustomerDetailModel(name1,phone1);
                                customerDetailsList.add(detail);
                                makeVisible();
                                //customerDetailsList.add(new CustomerDetails(name1,phone1));
                            }

                        }
                           catch (JSONException e) {
                            e.printStackTrace();
                        }

                cAdapter.notifyDataSetChanged();
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
                params.put("chemist", username);
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

