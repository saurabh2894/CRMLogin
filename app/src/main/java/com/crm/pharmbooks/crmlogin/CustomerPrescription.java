package com.crm.pharmbooks.crmlogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.CustomerAdapter;
import Adapters.PrescriptionAdapter;
import Model.CustomerDetailModel;
import Model.MedicineDetailModel;
import Model.PresciptionModel;

public class CustomerPrescription extends AppCompatActivity {

    private ArrayList<PresciptionModel> presciptionModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PrescriptionAdapter pAdapter;
    EditText dboxMedName,dboxMedDose,dboxMedStart,dboxMedEnd;
    ProgressBar pb;
    TextView txt;
    String presId;
    int pos;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        presId = extra.getString("presId");
        setContentView(R.layout.activity_customer_prescription);
        recyclerView = (RecyclerView) findViewById(R.id.re);
//        fab = new FloatingActionButton(getBaseContext());
        fab = (FloatingActionButton)findViewById(R.id.fab1);
        recyclerView.setVisibility(View.GONE);
        pb= (ProgressBar) findViewById(R.id.pb) ;
        txt=(TextView) findViewById(R.id.loadingtxt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        pAdapter = new PrescriptionAdapter(presciptionModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // set the adapter
        recyclerView.setAdapter(pAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                pos=position;
                showCustomDialog(pos);

            }



            // Toast.makeText(getApplicationContext(), medicineDetail.getMName() + " is selected!", Toast.LENGTH_SHORT).show();


            @Override
            public void onLongClick(View view, int position) {
                //Toast.makeText(getApplicationContext(), medicineDetail.getMName() + " is selected!", Toast.LENGTH_SHORT).show();
                pos=position;

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerPrescription.this);
                alertDialog.setTitle("Remove Entry...");
                alertDialog.setMessage("Do You Really Want To Remove This Entry?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presciptionModelList.remove(pos);
                        pAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        }));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(-1);
            }
        });

                getSupportActionBar().setTitle("Prescriptions");
        String url = "https://pharmcrm.herokuapp.com/api/medicine/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                recyclerView.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);
                try {
                    JSONArray jarr = new JSONArray(response);
                    for(int i=0;i<jarr.length();i++){
                        JSONObject ob1 = jarr.getJSONObject(i);
                        String endDate= ob1.getString("dosageend");
                        String dose = ob1.getString("days");
                        String refill = ob1.getString("lastrefillon");
                        String medName = ob1.getString("medicinename");
                        PresciptionModel detail = new PresciptionModel(medName,dose,refill,endDate);
                        presciptionModelList.add(detail);
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                pAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("prescid",presId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerPrescription.this);
        requestQueue.add(stringRequest);

    }

    public void showCustomDialog(final int pos){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerPrescription.this);
        // Get the layout inflater
        LayoutInflater linf = LayoutInflater.from(getApplicationContext());
        final View inflator = linf.inflate(R.layout.prescription_edit_dialogbox, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        alertDialog.setView(inflator);

        String btn = "";

        dboxMedName = (EditText) inflator.findViewById(R.id.dboxMedName);
        dboxMedDose = (EditText) inflator.findViewById(R.id.dboxMedDose);
        dboxMedStart = (EditText) inflator.findViewById(R.id.dboxMedStart);
        dboxMedEnd = (EditText) inflator.findViewById(R.id.dboxMedEnd);


        dboxMedStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    Toast.makeText(CustomerPrescription.this,"Date Format Is (YYYY-MM-DD)",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dboxMedEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    Toast.makeText(CustomerPrescription.this,"Date Format Is (YYYY-MM-DD)",Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(pos!=-1) {
            // Setting Dialog Title
            alertDialog.setTitle("Edit Details...");
            btn="Edit";
            String medName =   presciptionModelList.get(pos).getMedName();
            String medDose =   presciptionModelList.get(pos).getDosage();
            String medStart =   presciptionModelList.get(pos).getRefillDate();
            String medEnd =   presciptionModelList.get(pos).getEndDate();


            dboxMedName.setText(medName);
            dboxMedDose.setText(medDose);
            dboxMedStart.setText(medStart);
            dboxMedEnd.setText(medEnd);
        }
        else{
            alertDialog.setTitle("Add New...");
            btn="Add";
            dboxMedName.setText("");
            dboxMedDose.setText("");
            dboxMedStart.setText("");
            dboxMedEnd.setText("");

        }
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {



                String medname = dboxMedName.getText().toString().trim();
                String meddose = dboxMedDose.getText().toString().trim();
                String medstart = dboxMedStart.getText().toString().trim();
                String medend = dboxMedEnd.getText().toString().trim();
                if(pos!=-1) {
                    presciptionModelList.remove(pos);

                    presciptionModelList.add(pos, new PresciptionModel(medname, meddose, medstart, medend));
                }
                else{
                    presciptionModelList.add(new PresciptionModel(medname, meddose, medstart, medend));
                }

                pAdapter.notifyDataSetChanged();


                // Write your code here to invoke YES event
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();



    }

}
