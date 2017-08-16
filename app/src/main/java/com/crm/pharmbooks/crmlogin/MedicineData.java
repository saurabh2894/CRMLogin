package com.crm.pharmbooks.crmlogin;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import Adapters.MedicineAdapter;
import Model.MedicineDetail;


public class MedicineData extends AppCompatActivity {
    private ArrayList<MedicineDetail> medicineDetailList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MedicineAdapter mAdapter;
    private FloatingActionButton fab;

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.saveIcon) {

            Log.d("hello","hello");
            /*new View.OnClickListener() {
                public void onClick(View view) {
                            Log.v("EditText", medicineDetailList.getText().toString());
                }
            };*/


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_data);


        



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);







        mAdapter = new MedicineAdapter(medicineDetailList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //prepareMedicineData();
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                MedicineDetail medicineDetail = new MedicineDetail("",10);
                medicineDetailList.add(medicineDetail);
                mAdapter.notifyDataSetChanged();
                //medicineDetail.getMName().toString();
                //Log.v("EditText",medicineDetail.getMQuantity().toString()+"");

            }

        });

    }
    //

    /*private void prepareMedicineData() {

        MedicineDetail medicineDetail = new MedicineDetail("Crocin",20);
        medicineDetailList.add(medicineDetail);


        mAdapter.notifyDataSetChanged();
    }*/


}




