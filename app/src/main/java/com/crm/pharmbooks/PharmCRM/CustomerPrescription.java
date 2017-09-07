package com.crm.pharmbooks.PharmCRM;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import Adapters.PrescriptionAdapter;
import Model.PresciptionModel;

public class CustomerPrescription extends AppCompatActivity {

    private ArrayList<PresciptionModel> presciptionModelList = new ArrayList<>();
    private ArrayList<PresciptionModel> presciptionEditModelList = new ArrayList<>();
    ArrayList<String> med = new ArrayList<String>();
    private RecyclerView recyclerView;
    private PrescriptionAdapter pAdapter,pEditAdapter;
    EditText dboxMedName,dboxMedDose,dboxMedStart,dboxMedEnd;
    FloatingActionButton fab;
    ProgressBar pb;
    TextView txt;
    String presId;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        presId = extra.getString("presId");
        setContentView(R.layout.activity_customer_prescription);
        recyclerView = (RecyclerView) findViewById(R.id.re);
        recyclerView.setVisibility(View.GONE);
        pb= (ProgressBar) findViewById(R.id.pb) ;
        txt=(TextView) findViewById(R.id.loadingtxt);
        fab=(FloatingActionButton)findViewById(R.id.fab1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = (TextView)toolbar.findViewById(R.id.title);

        pAdapter = new PrescriptionAdapter(presciptionModelList);
        pEditAdapter = new PrescriptionAdapter(presciptionEditModelList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // set the adapter
        recyclerView.setAdapter(pAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(-1);
            }
        });
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
                alertDialog.show();
            }
        }));

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
                        String id = ob1.getString("id");
                        PresciptionModel detail = new PresciptionModel(medName,dose,refill,endDate,id);
                        presciptionModelList.add(detail);

                    }
                    Log.d("mytag",jarr+"");

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


    public JSONObject getJsonFromMyFormObjectEdit(ArrayList<PresciptionModel> presciptionEditModelList) throws JSONException {
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {

            for (int i = 0; i < presciptionEditModelList.size(); i++) {
                JSONObject formDetailsJson = new JSONObject();
                formDetailsJson.put("medicineName", presciptionEditModelList.get(i).getMedName());
                formDetailsJson.put("dosage", String.valueOf(presciptionEditModelList.get(i).getDosage()));
                //formDetailsJson.put("refilldate", String.valueOf(presciptionEditModelList.get(i).getRefillDate()));
                //formDetailsJson.put("enddate"+(i+1), String.valueOf(presciptionEditModelList.get(i).getEndDate()));
                formDetailsJson.put("medicineId", String.valueOf(presciptionEditModelList.get(i).getMedicineid()));
                jsonArray.put(formDetailsJson);
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        responseDetailsJson.put("presciptionEditModelList", jsonArray);

        Log.d("mytag",responseDetailsJson+"");
        Log.d("mytag",presId+"");
        //Log.d("mytag",presciptionEditModelList.size()+"");


        return responseDetailsJson;


    }


    public void sendEditDataList() {
        String url = "https://pharmcrm.herokuapp.com/api/save/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int result;
                        //String msg = null;
                        try {
                            JSONObject object = new JSONObject(response);
                            //msg = object.getString("msg");
                            result= object.getInt("result");
                            if(result == 1)
                            {


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(CustomerPrescription.this,response,Toast.LENGTH_LONG).show();
                        //Toast.makeText(MedicineData.this,msg+""+ result +"",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerPrescription.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                try {
                    params.put("prescid",presId);
                    //params.put("counter",String.valueOf(presciptionEditModelList.size()));
                    params.put("data",getJsonFromMyFormObjectEdit(presciptionEditModelList)+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }





    //For Add Prescription

/*
    public JSONObject getJsonFromMyFormObjectAdd(ArrayList<PresciptionModel> presciptionModelList) throws JSONException {
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < presciptionModelList.size(); i++) {
                JSONObject formDetailsJson = new JSONObject();
                formDetailsJson.put("medicineName_"+(i+1), presciptionModelList.get(i).getMedName());
                formDetailsJson.put("dosage"+(i+1), String.valueOf(presciptionModelList.get(i).getDosage()));
                formDetailsJson.put("refilldate"+(i+1), String.valueOf(presciptionModelList.get(i).getRefillDate()));
                formDetailsJson.put("enddate"+(i+1), String.valueOf(presciptionModelList.get(i).getEndDate()));

                jsonArray.put(formDetailsJson);


            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        responseDetailsJson.put("presciptionModelList", jsonArray);

        Log.d("mytag",responseDetailsJson+"");
        Log.d("mytag",presId+"");

        return responseDetailsJson;


    }


    public void sendAddDataList() {
        String url = "https://pharmcrm.herokuapp.com/api/save/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int result;
                        //String msg = null;
                        try {
                            JSONObject object = new JSONObject(response);
                            //msg = object.getString("msg");
                            result= object.getInt("result");
                            if(result == 1)
                            {


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(CustomerPrescription.this,response,Toast.LENGTH_LONG).show();
                        //Toast.makeText(MedicineData.this,msg+""+ result +"",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerPrescription.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                try {
                    params.put("prescid",presId);
                    params.put("counter",String.valueOf(presciptionEditModelList.size()));
                    params.put("data",getJsonFromMyFormObjectAdd(presciptionEditModelList)+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.prescriptiondetailactionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.saveIcon) {

           // Log.d("mtag","save icon call working");
           // sendEditDataList();
            //sendAddDataList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void showCustomDialog(final int pos){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerPrescription.this);
        // Get the layout inflater
        LayoutInflater linf = LayoutInflater.from(getApplicationContext());
        final View inflator = linf.inflate(R.layout.prescription_edit_dialogbox, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        // Setting Dialog Title


        // Setting Dialog Message
        // Setting Icon to Dialog
        alertDialog.setView(inflator);

        final TextView dboxMedId;

        dboxMedName = (EditText) inflator.findViewById(R.id.dboxMedName);
        dboxMedDose = (EditText) inflator.findViewById(R.id.dboxMedDose);
        dboxMedStart = (EditText) inflator.findViewById(R.id.dboxMedStart);
        dboxMedEnd = (EditText) inflator.findViewById(R.id.dboxMedEnd);
        dboxMedId = (TextView) inflator.findViewById(R.id.dboxMedId);
        String btn="";
        if(pos!=-1) {
            alertDialog.setTitle("Edit Details...");
            final String medName = presciptionModelList.get(pos).getMedName();
            String medDose = presciptionModelList.get(pos).getDosage();
            String medStart = presciptionModelList.get(pos).getRefillDate();
            String medEnd = presciptionModelList.get(pos).getEndDate();
            String medId = presciptionModelList.get(pos).getMedicineid();
            btn="EDIT";

            dboxMedName.setText(medName);
            dboxMedDose.setText(medDose);
            dboxMedStart.setText(medStart);
            dboxMedEnd.setText(medEnd);
            dboxMedId.setText(medId);

        }
        else{
            alertDialog.setTitle("Add New...");
            btn="Add";
            dboxMedName.setText("");
            dboxMedDose.setText("");
            dboxMedId.setVisibility(View.GONE);
            dboxMedStart.setEnabled(false);
            dboxMedEnd.setEnabled(false);
        }


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {



                String medname = dboxMedName.getText().toString().trim();
                String meddose = dboxMedDose.getText().toString().trim();
                String medstart = dboxMedStart.getText().toString().trim();
                String medend = dboxMedEnd.getText().toString().trim();
                String medId = dboxMedId.getText().toString().trim();
                if(pos!=-1) {
                    presciptionModelList.remove(pos);
                    presciptionModelList.add(pos, new PresciptionModel(medname, meddose, medstart, medend, medId));

                    pAdapter.notifyDataSetChanged();


                    //code for Prescription Edit List

                    String medNameEdit = dboxMedName.getText().toString().trim();
                    String meddoseEdit = dboxMedDose.getText().toString().trim();
                    String medstartEdit = dboxMedStart.getText().toString().trim();
                    String medendEdit = dboxMedEnd.getText().toString().trim();
                    String medIdEdit = dboxMedId.getText().toString().trim();


                    if (med.contains(medIdEdit)) {
                        presciptionEditModelList.remove(pos);
                            presciptionEditModelList.add(pos, new PresciptionModel(medNameEdit, meddoseEdit, medstartEdit, medendEdit, medIdEdit));
                            pEditAdapter.notifyDataSetChanged();
                        Log.d("mytag","You were right1");
                        }
                        else{
                        med.add(medIdEdit);
                        presciptionEditModelList.add(pos, new PresciptionModel(medNameEdit, meddoseEdit, medstartEdit, medendEdit, medIdEdit));
                        pEditAdapter.notifyDataSetChanged();
                        Log.d("mytag","You were right");
                    }


                    sendEditDataList();
                }
                else{
                    String dateStart = presciptionModelList.get(0).getRefillDate();
                    String[] date= dateStart.split("-");
                    int year=Integer.parseInt(date[0]);
                    int month=Integer.parseInt(date[1]);
                    int day=Integer.parseInt(date[2]);
                    int dose = Integer.parseInt(dboxMedDose.getText().toString().trim());
                    int ndays=day+dose;
                    int nmonth=month;
                    int nyear=year;
                    if(month==1||month==3||month==5||month==7||month==10||month==12||month==8){
                        if(ndays>31){
                            ndays=ndays-31;
                            nmonth=month+1;
                        }
                    }
                    else if(month==2) {
                        if (year % 4 == 0) {
                            if (ndays > 29) {
                                ndays-= 29;
                                nmonth = month + 1;
                            }
                        } else {
                            if (ndays > 28) {
                                ndays = ndays - 28;
                                nmonth = month + 1;
                            }
                        }
                    }
                    else if(month==4||month==6||month==9||month==11){
                        if(ndays>30){
                            ndays=ndays-30;
                            nmonth=month+1;
                        }
                    }

                    if(nmonth>12){
                        nmonth-=12;
                    }
                    String ndate="";
                    int flag=0;
                    if(nmonth<10){
                        ndate=nyear+"-0"+nmonth+"-"+ndays;
                        flag=1;
                    }
                    if(ndays<10){
                        ndate=nyear+"-"+nmonth+"-0"+ndays;
                        flag=1;
                    }
                    if(flag==0){
                        ndate=nyear+"-"+nmonth+"-"+ndays;
                    }
                    presciptionModelList.add(new PresciptionModel(medname, meddose, dateStart, ndate,"0"));
                    pAdapter.notifyDataSetChanged();
                }



                // Write your code here to invoke YES event

            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(CustomerPrescription.this, "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        alertDialog.show();






                /*CustomDialogClass cdd=new CustomDialogClass(position,MedicineData.this);
                cdd.show();
                if(cdd.isShowing()) {
                    Log.d("mytag","medicine data dialog box call working");

                }*/
    }

}
