package com.crm.pharmbooks.crmlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.CustomerAdapter;
import Model.CustomerDetailModel;

public class ExistingRefills extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText SearchBoxExistingRefill;
    private ArrayList<CustomerDetailModel> customerDetailList = new ArrayList<>();
    private CustomerAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_refills);
        SearchBoxExistingRefill = (EditText) findViewById(R.id.SearchBoxExistingRefill);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_viewExRefill);
        cAdapter = new CustomerAdapter(customerDetailList);


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // set the adapter
        recyclerView.setAdapter(cAdapter);
        prepareData();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {


                Toast.makeText(getApplicationContext(), "AWWWW YISSS", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void prepareData() {
        CustomerDetailModel c = new CustomerDetailModel("Manya");
        customerDetailList.add(c);

        c = new CustomerDetailModel("Saurabh");
        customerDetailList.add(c);
        cAdapter.notifyDataSetChanged();

    }
}
