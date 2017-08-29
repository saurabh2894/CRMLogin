package Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crm.pharmbooks.crmlogin.R;

import java.util.ArrayList;

import Model.CustomerDetailModel;

/**
 * Created by Dell on 28-Aug-17.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {

    private ArrayList<CustomerDetailModel> customerList;


    public CustomerAdapter(ArrayList<CustomerDetailModel> customerList) {
        this.customerList = customerList;
        Log.d("mytag",customerList.size()+"");
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView CustomerName;

        public MyViewHolder(View view) {
            super(view);
            CustomerName = (TextView) view.findViewById(R.id.CustomerName);


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
        holder.CustomerName.setText(customerDetail.getCustomerName());


    }

    @Override
    public int getItemCount()
    {
        return customerList.size();
    }



}