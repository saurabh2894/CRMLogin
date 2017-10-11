package Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crm.pharmbooks.PharmCRM.PrescriptionListActivity;
import com.crm.pharmbooks.PharmCRM.PrescriptionRefillActivity;
import com.crm.pharmbooks.PharmCRM.R;

import java.util.ArrayList;

import Model.RefillModel;

/**
 * Created by Dell on 09-Oct-17.
 */


    public class RefillAdapter extends RecyclerView.Adapter<RefillAdapter.MyViewHolder> {

        private ArrayList<RefillModel> refillList;


        public RefillAdapter(ArrayList<RefillModel> refillList) {
            this.refillList = refillList;
        }




        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView medName;
            public TextView medDose;
            public TextView lastRefill;
            public TextView endDate;
            public LinearLayout base;
            public CheckBox checkBox;
            public MyViewHolder(View view) {
                super(view);
                medName = (TextView) view.findViewById(R.id.med_name);
                medDose= (TextView) view.findViewById(R.id.med_dose);
                lastRefill= (TextView) view.findViewById(R.id.med_last_refill);
                endDate= (TextView) view.findViewById(R.id.med_end);
                base=(LinearLayout)view.findViewById(R.id.refill_list_row_id);
                checkBox=(CheckBox)view.findViewById(R.id.checkboxitem);

            }
        }


        @Override
        public RefillAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.refill_list_row, parent, false);

        return new RefillAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RefillAdapter.MyViewHolder holder, int position) {


            final RefillModel customerDetail = refillList.get(position);
            holder.medName.setText(customerDetail.getMedName());
            holder.medDose.setText(customerDetail.getDosage());
            holder.lastRefill.setText(customerDetail.getRefillDate());
            holder.endDate.setText(customerDetail.getEndDate());
            holder.checkBox.setChecked(customerDetail.getCheck());
//            holder.checkBox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(!(customerDetail.getCheck())){
//                        customerDetail.setCheck(true);
//                    }
//                    else{
//                        customerDetail.setCheck(false);
//                    }
//                }
//            });
            if(PrescriptionRefillActivity.LONG_CLICK_FLAG==0){
                    holder.checkBox.setVisibility(View.GONE);


            }else{
                holder.checkBox.setVisibility(View.VISIBLE);
            }




        }

        @Override
        public int getItemCount()
        {
            return refillList.size();
        }



    }

