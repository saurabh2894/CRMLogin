package com.crm.pharmbooks.crmlogin;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import Model.MedicineDetail;

/**
 * Created by Dell on 24-Aug-17.
 */

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public int position;
    public Button yes, no;
    public EditText MedicineName,MedicineQuantity;
    String var_name, var_quantity;

    public CustomDialogClass(int position,Activity a) {
        super(a);
        this.position=position;
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialogbox);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        MedicineName = (EditText) findViewById(R.id.MedicineName);
        MedicineQuantity = (EditText) findViewById(R.id.MedicineQuantity);




        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
            {
                var_name = MedicineName.getText().toString();
                var_quantity = MedicineQuantity.getText().toString();
                MedicineDetail detail = new MedicineDetail(var_name,Integer.parseInt(var_quantity));
                MedicineData.
                //sendData(var_name,Integer.parseInt(var_quantity));

                break;}
            case R.id.btn_no:
                dismiss();
                break;

        }
        dismiss();
    }


}