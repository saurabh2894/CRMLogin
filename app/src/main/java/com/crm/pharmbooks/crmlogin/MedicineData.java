package com.crm.pharmbooks.crmlogin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Vector;

import Adapters.MedicineAdapter;
import Model.MedicineDetail;

import static android.R.id.message;


public class MedicineData extends AppCompatActivity {
    private ArrayList<MedicineDetail> medicineDetailList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MedicineAdapter mAdapter;
    private FloatingActionButton fab;
    private EditText MedicineName, MedicineQuantity;
    private Button addButton;
    String MedicineName_value,MedicineQuantity_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_data);
        MedicineName = (EditText)findViewById(R.id.MedicineName);
        MedicineQuantity = (EditText) findViewById(R.id.MedicineQuantity);
        addButton = (Button) findViewById(R.id.addButtonId);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicineName_value = MedicineName.getText().toString();
                MedicineQuantity_value = MedicineQuantity.getText().toString();
                MedicineDetail detail = new MedicineDetail(MedicineName_value,Integer.parseInt(MedicineQuantity_value));
                medicineDetailList.add(detail);
                mAdapter.notifyDataSetChanged();
            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MedicineAdapter(medicineDetailList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // set the adapter
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MedicineDetail medicineDetail = medicineDetailList.get(position);


                CustomDialogClass cdd=new CustomDialogClass(MedicineData.this);
                cdd.show();






                Toast.makeText(getApplicationContext(), medicineDetail.getMName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
              //Toast.makeText(getApplicationContext(), medicineDetail.getMName() + " is selected!", Toast.LENGTH_SHORT).show();

            }
        }));

        /*fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                MedicineDetail medicineDetail;
                medicineDetail = mAdapter.getData();
                medicineDetailList.add(medicineDetail);
                Log.d("tag",medicineDetailList.get(medicineDetailList.size()-1).getMName());
                mAdapter.notifyDataSetChanged();
            }

        });*/
    }


    public JSONObject getJsonFromMyFormObject(ArrayList<MedicineDetail> medicineDetailList) throws JSONException {
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < medicineDetailList.size(); i++) {
                JSONObject formDetailsJson = new JSONObject();
                formDetailsJson.put("medicine_"+(i+1), medicineDetailList.get(i).getMName());
                formDetailsJson.put("days_"+(i+1), String.valueOf(medicineDetailList.get(i).getMQuantity()));

                jsonArray.put(formDetailsJson);


            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        responseDetailsJson.put("medicineDetailList", jsonArray);

        Log.d("mytag",responseDetailsJson+"");
        return responseDetailsJson;


    }

    public void sendR() {
        String url = "https://pharmcrm.herokuapp.com/api/save/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int result;
                        //String msg = null;
                        try {
                            JSONObject object = new JSONObject(response);
                            //msg = object.getString("msg");
                            result= object.getInt("result");
                            if(result == 1)
                            {




                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MedicineData.this,response,Toast.LENGTH_LONG).show();
                        //Toast.makeText(MedicineData.this,msg+""+ res +"",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MedicineData.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                try {
                params.put("Cname","manya");
                params.put("Cnumber","9898876545");
                params.put("Cadd","delhi");
                params.put("counter",String.valueOf(medicineDetailList.size()));
                params.put("data",getJsonFromMyFormObject(medicineDetailList)+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.medicinedetailactionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.saveIcon) {

            Log.d("mtag","hello");
            sendR();
            /*JSONObject json = null;
            try {
                json = getJsonFromMyFormObject(medicineDetailList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("json",json+"");
            new View.OnClickListener() {
                public void onClick(View view) {

                }
            };*/

            return true;
        }

        return super.onOptionsItemSelected(item);
    }






}




