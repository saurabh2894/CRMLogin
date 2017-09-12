package Adapters;

/**
 * Created by Dell on 14-Aug-17.
 */


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crm.pharmbooks.PharmCRM.MedicineData;
import com.crm.pharmbooks.PharmCRM.R;

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
            public LinearLayout base;

            public MyViewHolder(View view) {
                super(view);
                MName = (TextView) view.findViewById(R.id.MName);
                MQuantity = (TextView) view.findViewById(R.id.MQuantity);
                base = (LinearLayout) view.findViewById(R.id.medicine_list_layout);

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
            if(MedicineData.LONG_CLICK_FLAG==1){
                if(MedicineData.pos==position){
                    holder.base.setBackgroundColor(Color.parseColor("#9e9e9e"));
                }
            }else{
                holder.base.setBackgroundColor(Color.TRANSPARENT);
            }

        }

        @Override
        public int getItemCount()
        {
            return medicineList.size();
        }



    }

