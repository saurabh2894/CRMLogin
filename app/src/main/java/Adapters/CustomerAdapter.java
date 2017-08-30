package Adapters;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crm.pharmbooks.crmlogin.R;

import java.util.ArrayList;

import Model.CustomerDetails;
import Model.MedicineDetail;


public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {

        private ArrayList<CustomerDetails> customerList;


        public CustomerAdapter(ArrayList<CustomerDetails> customerList) {
                   this.customerList = customerList;

        }






        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView CName,CPhone;

            public MyViewHolder(View view) {
                super(view);
                CName = (TextView) view.findViewById(R.id.cus_name);
                CPhone = (TextView) view.findViewById(R.id.cus_phone);

            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.customer_name_entry, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {


            CustomerDetails customerDetails = customerList.get(position);
            holder.CName.setText(customerDetails.getCName());
            holder.CPhone.setText(customerDetails.getCPhone());

        }

        @Override
        public int getItemCount()
        {
            return customerList.size();
        }



    }

