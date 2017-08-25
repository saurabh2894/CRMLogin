package com.crm.pharmbooks.crmlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class DashBoard extends AppCompatActivity {

    public Button NoOfCustomersToBeRefilled,NoOFCustomersOnboard,NoOfRepeatedCustomersNoOfMedicinesRefilled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
    }
}
