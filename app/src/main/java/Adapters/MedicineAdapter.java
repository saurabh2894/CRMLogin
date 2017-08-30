package Adapters;

/**
 * Created by Dell on 14-Aug-17.
 */


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crm.pharmbooks.crmlogin.R;

import java.util.ArrayList;

import Model.MedicineDetailModel;


public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

        private ArrayList<MedicineDetailModel> medicineList;


        public MedicineAdapter(ArrayList<MedicineDetailModel> medicineList) {
                   this.medicineList = medicineList;
                   Log.d("mytag",medicineList.size()+"");
        }






        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView MName, MQuantity;

            public MyViewHolder(View view) {
                super(view);
                MName = (TextView) view.findViewById(R.id.MName);
                MQuantity = (TextView) view.findViewById(R.id.MQuantity);

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


            MedicineDetailModel medicineDetailModel = medicineList.get(position);
            holder.MName.setText(medicineDetailModel.getMName());
            holder.MQuantity.setText(medicineDetailModel.getMQuantity()+"");

        }

        @Override
        public int getItemCount()
        {
            return medicineList.size();
        }



    }

