package com.crm.pharmbooks.PharmCRM;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

    ArrayList<String> med = new ArrayList<String>();
    private RecyclerView recyclerView;
    private PrescriptionAdapter pAdapter,pEditAdapter,pAddAdapter,pDeleteAdapter;
    EditText dboxMedName,dboxMedDose,dboxMedStart,dboxMedEnd;
    String medNameEditbox="",medDoseEditbox="",medStartEditbox="",medEndEditbox="",medIdEditbox="";

    FloatingActionButton fab;
    ProgressBar pb;
    TextView txt;
    RelativeLayout rl;
    String presId,customerphone;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        presId = extra.getString("presId");
        customerphone=extra.getString("customerphone");
        Log.d("customerphone",customerphone);
        setContentView(R.layout.activity_customer_prescription);
        rl=(RelativeLayout)findViewById(R.id.rel);
        recyclerView = (RecyclerView) findViewById(R.id.re);
        recyclerView.setVisibility(View.GONE);
        pb= (ProgressBar) findViewById(R.id.pb) ;
        txt=(TextView) findViewById(R.id.loadingtxt);
        fab=(FloatingActionButton)findViewById(R.id.fab1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = (TextView)toolbar.findViewById(R.id.title);

        pAdapter = new PrescriptionAdapter(presciptionModelList);


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
                final ArrayList<PresciptionModel> presciptionDeleteModelList = new ArrayList<>();
                pDeleteAdapter = new PrescriptionAdapter(presciptionDeleteModelList);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerPrescription.this);
                alertDialog.setTitle("Remove Entry...");
                alertDialog.setMessage("Do You Really Want To Remove This Entry?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PresciptionModel deletedValue=presciptionModelList.get(pos);
                        PresciptionModel detail = new PresciptionModel(deletedValue.getMedName(),deletedValue.getDosage(),deletedValue.getRefillDate(),deletedValue.getEndDate(),deletedValue.getMedicineid());
                        presciptionDeleteModelList.add(detail);
                        pDeleteAdapter.notifyDataSetChanged();
                        sendDeleteDataList(presciptionDeleteModelList);

                        presciptionModelList.remove(pos);
                        pAdapter.notifyDataSetChanged();
                        Toast.makeText(CustomerPrescription.this,deletedValue.getMedName()+" is deleted",Toast.LENGTH_LONG).show();


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

        getSupportActionBar().setTitle("");
        String url = "https://pharmcrm.herokuapp.com/api/medicine/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                recyclerView.setVisibility(View.VISIBLE);
                rl.setVisibility(View.GONE);
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


    public void sendEditDataList(final ArrayList<PresciptionModel> presciptionEditModelList) {
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
                    //params.put("prescid",presId);
                    //params.put("counter",String.valueOf(presciptionEditModelList.size()));\
                    //params.put("customerphoneedit",customerphone);
                    params.put("dataedit",getJsonFromMyFormObjectEdit(presciptionEditModelList)+"");

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

    public JSONObject getJsonFromMyFormObjectAdd(ArrayList<PresciptionModel> presciptionAddModelList) throws JSONException {
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < presciptionAddModelList.size(); i++) {
                JSONObject formDetailsJson = new JSONObject();
                formDetailsJson.put("medicineName_"+(i+1), presciptionAddModelList.get(i).getMedName());
                formDetailsJson.put("dosage_"+(i+1), String.valueOf(presciptionAddModelList.get(i).getDosage()));
                //formDetailsJson.put("refilldate_"+(i+1), String.valueOf(presciptionAddModelList.get(i).getRefillDate()));
                //formDetailsJson.put("enddate_"+(i+1), String.valueOf(presciptionAddModelList.get(i).getEndDate()));

                jsonArray.put(formDetailsJson);


            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        responseDetailsJson.put("presciptionAddModelList", jsonArray);

        Log.d("mytag",responseDetailsJson+"");
        Log.d("mytag",presId+"");

        return responseDetailsJson;


    }


    public void sendAddDataList(final ArrayList<PresciptionModel> presciptionAddModelList) {
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
                    params.put("counter",String.valueOf(presciptionAddModelList.size()));
                    params.put("dataadd",getJsonFromMyFormObjectAdd(presciptionAddModelList)+"");
                    params.put("cnumber",customerphone);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    //For Deleted prescription values

    public JSONObject getJsonFromMyFormObjectDelete(ArrayList<PresciptionModel> presciptionDeleteModelList) throws JSONException {
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {

            for (int i = 0; i < presciptionDeleteModelList.size(); i++) {
                JSONObject formDetailsJson = new JSONObject();
                //formDetailsJson.put("medicineName", presciptionDeleteModelList.get(i).getMedName());
                //formDetailsJson.put("dosage", String.valueOf(presciptionDeleteModelList.get(i).getDosage()));
                //formDetailsJson.put("refilldate", String.valueOf(presciptionDeleteModelList.get(i).getRefillDate()));
                //formDetailsJson.put("enddate"+(i+1), String.valueOf(presciptionDeleteModelList.get(i).getEndDate()));
                formDetailsJson.put("medicineId", String.valueOf(presciptionDeleteModelList.get(i).getMedicineid()));
                jsonArray.put(formDetailsJson);
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        responseDetailsJson.put("presciptionDeleteModelList", jsonArray);

        Log.d("mytag",responseDetailsJson+"");
        Log.d("mytag",presId+"");
        //Log.d("mytag",presciptionEditModelList.size()+"");


        return responseDetailsJson;


    }


    public void sendDeleteDataList(final ArrayList<PresciptionModel> presciptionDeleteModelList) {
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
                    //params.put("prescid",presId);
                    //params.put("counter",String.valueOf(presciptionEditModelList.size()));\
                    //params.put("customerphoneedit",customerphone);
                    params.put("datadelete",getJsonFromMyFormObjectDelete(presciptionDeleteModelList)+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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

        final TextView dboxMedId,ds,de,dm,m;

        dboxMedName = (EditText) inflator.findViewById(R.id.dboxMedName);
        dboxMedDose = (EditText) inflator.findViewById(R.id.dboxMedDose);
        dboxMedStart = (EditText) inflator.findViewById(R.id.dboxMedStart);
        dboxMedEnd = (EditText) inflator.findViewById(R.id.dboxMedEnd);
        dboxMedId = (TextView) inflator.findViewById(R.id.dboxMedId);
        ds=(TextView)inflator.findViewById(R.id.toRemove1);
        de=(TextView)inflator.findViewById(R.id.toRemove2);
        dm=(TextView)inflator.findViewById(R.id.medId);
        m=(TextView)inflator.findViewById(R.id.medIdColon);

        String btn="";
        if(pos!=-1) {
            alertDialog.setTitle("Edit Details...");
            medNameEditbox = presciptionModelList.get(pos).getMedName();
            medDoseEditbox = presciptionModelList.get(pos).getDosage();
            medStartEditbox = presciptionModelList.get(pos).getRefillDate();
            medEndEditbox = presciptionModelList.get(pos).getEndDate();
            medIdEditbox = presciptionModelList.get(pos).getMedicineid();
            btn="EDIT";

            dboxMedName.setText(medNameEditbox);
            dboxMedDose.setText(medDoseEditbox);
            ds.setVisibility(View.GONE);
            de.setVisibility(View.GONE);
            dboxMedStart.setVisibility(View.GONE);
            dboxMedEnd.setVisibility(View.GONE);
            dboxMedId.setText(medIdEditbox);

        }
        else{
            alertDialog.setTitle("Add New...");
            btn="Add";
            dboxMedName.setText("");
            dboxMedDose.setText("");
            dboxMedId.setVisibility(View.GONE);
            dboxMedStart.setVisibility(View.GONE);
            dboxMedEnd.setVisibility(View.GONE);
            ds.setVisibility(View.GONE);
            de.setVisibility(View.GONE);
            dm.setVisibility(View.GONE);
            m.setVisibility(View.GONE);
        }


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                ArrayList<PresciptionModel> presciptionEditModelList = new ArrayList<>();
                ArrayList<PresciptionModel> presciptionAddModelList = new ArrayList<>();

                pEditAdapter = new PrescriptionAdapter(presciptionEditModelList);
                pAddAdapter = new PrescriptionAdapter(presciptionAddModelList);

                String medname = dboxMedName.getText().toString().trim();
                String meddose = dboxMedDose.getText().toString().trim();
                String medstart = dboxMedStart.getText().toString().trim();
                String medend = dboxMedEnd.getText().toString().trim();
                String medId = dboxMedId.getText().toString().trim();
                if (!(TextUtils.isEmpty(medname) && TextUtils.isEmpty(meddose)) ) {
                    if (pos != -1) {
                        if (!(medNameEditbox.equals(medname) && medDoseEditbox.equals(meddose))) {
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
                                int pos = 0;
                                for (int i = 0; i < med.size(); i++) {
                                    if (med.get(i).equals(medIdEdit)) {
                                        pos = i;
                                    }
                                }
                                presciptionEditModelList.remove(pos);
                                presciptionEditModelList.add(pos, new PresciptionModel(medNameEdit, meddoseEdit, medStartEditbox, medEndEditbox, medIdEdit));
                                pEditAdapter.notifyDataSetChanged();
                                Log.d("mytag", "You were right1");
                            } else {
                                med.add(medIdEdit);
                                presciptionEditModelList.add(new PresciptionModel(medNameEdit, meddoseEdit, medstartEdit, medendEdit, medIdEdit));
                                pEditAdapter.notifyDataSetChanged();
                                Log.d("mytag", "You were right");
                            }


                            sendEditDataList(presciptionEditModelList);
                        } else {
                            Toast.makeText(CustomerPrescription.this, "Don't add save values again, make some changes bro!", Toast.LENGTH_SHORT).show();

                        }

                    }else {
                        /*
                        String dateStart = presciptionModelList.get(0).getRefillDate();
                        String[] date = dateStart.split("-");
                        int year = Integer.parseInt(date[0]);
                        int month = Integer.parseInt(date[1]);
                        int day = Integer.parseInt(date[2]);
                        int dose = Integer.parseInt(dboxMedDose.getText().toString().trim());
                        int ndays = day + dose;
                        int nmonth = month;
                        int nyear = year;
                        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 10 || month == 12 || month == 8) {
                            if (ndays > 31) {
                                ndays = ndays - 31;
                                nmonth = month + 1;
                            }
                        } else if (month == 2) {
                            if (year % 4 == 0) {
                                if (ndays > 29) {
                                    ndays -= 29;
                                    nmonth = month + 1;
                                }
                            } else {
                                if (ndays > 28) {
                                    ndays = ndays - 28;
                                    nmonth = month + 1;
                                }
                            }
                        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                            if (ndays > 30) {
                                ndays = ndays - 30;
                                nmonth = month + 1;
                            }
                        }

                        if (nmonth > 12) {
                            nmonth -= 12;
                        }
                        String ndate = "";
                        int flag = 0;
                        if (nmonth < 10) {
                            ndate = nyear + "-0" + nmonth + "-" + ndays;
                            flag = 1;
                        }
                        if (ndays < 10) {
                            ndate = nyear + "-" + nmonth + "-0" + ndays;
                            flag = 1;
                        }
                        if (flag == 0) {
                            ndate = nyear + "-" + nmonth + "-" + ndays;
                        }

                        */
                            presciptionAddModelList.add(new PresciptionModel(medname, meddose, "0", "0", "0"));
                            pAddAdapter.notifyDataSetChanged();
                            presciptionModelList.add(new PresciptionModel(medname, meddose, "0", "0", "0"));
                            pAdapter.notifyDataSetChanged();
                            sendAddDataList(presciptionAddModelList);
                            //TODO send a request to server to fetch the lastest entry of prescription list
                    }


                        // Write your code here to invoke YES event

                }



                else{
                    Toast.makeText(CustomerPrescription.this, "Please Enter Some Values!!!", Toast.LENGTH_SHORT).show();
                }
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.customerprescriptionactionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
