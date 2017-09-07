package Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crm.pharmbooks.PharmCRM.R;

import java.util.ArrayList;

import Model.CustomerDetailModel;

/**
 * Created by hp on 28-Aug-17.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {

    private ArrayList<CustomerDetailModel> customerList;


    public CustomerAdapter(ArrayList<CustomerDetailModel> customerList) {
        this.customerList = customerList;
        Log.d("mytag",customerList.size()+"");
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView CName;
        public TextView CPhone;
        public MyViewHolder(View view) {
            super(view);
            CName = (TextView) view.findViewById(R.id.CName);
            CPhone= (TextView) view.findViewById(R.id.CPhone);


        }
    }


    @Override
    public CustomerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_list_row, parent, false);

        return new CustomerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomerAdapter.MyViewHolder holder, int position) {


        CustomerDetailModel customerDetail = customerList.get(position);
        holder.CName.setText(customerDetail.getCName());
        holder.CPhone.setText(customerDetail.getCPhone());



    }

    @Override
    public int getItemCount()
    {
        return customerList.size();
    }



}