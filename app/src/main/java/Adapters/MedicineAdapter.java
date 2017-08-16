package Adapters;

/**
 * Created by Dell on 14-Aug-17.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.crm.pharmbooks.crmlogin.R;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import java.util.Scanner;

import Model.MedicineDetail;


public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

        private ArrayList<MedicineDetail> medicineList;

        public MedicineAdapter(ArrayList<MedicineDetail> medicineList) {
            //medicineList= new ArrayList<RetrieveItem>(medicineList.length);
        this.medicineList = medicineList;
        Log.d("mytag",medicineList.size()+"");
        }






        public class MyViewHolder extends RecyclerView.ViewHolder {
            public EditText MName, MQuantity;
            public String MName_value,MQuantity_value;

            public MyViewHolder(View view) {
                super(view);
                MName = (EditText) view.findViewById(R.id.MName);
                MQuantity = (EditText) view.findViewById(R.id.MQuantity);

                MName_value= MName.getText().toString();
                MQuantity_value=MQuantity.getText().toString();

            }
        }




        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.medicine_list_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {


            //MedicineDetail medicineDetail = medicineList.get(position);
            //holder.MName.setText(medicineDetail.getMName());
            //holder.MQuantity.setText(medicineDetail.getMQuantity()+"");

        }


        @Override
        public int getItemCount()
        {
            return medicineList.size();
        }



    }

