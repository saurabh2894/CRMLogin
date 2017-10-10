package com.crm.pharmbooks.PharmCRM;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

import Adapters.RefillAdapter;
import Model.RefillModel;

public class PrescriptionRefillActivity extends AppCompatActivity {


    private ArrayList<RefillModel> refillModelList = new ArrayList<>();


    private RecyclerView recyclerView;
    Toolbar toolbar;
    public static int LONG_CLICK_FLAG=0;
//    ImageButton deletebtn;
    private RefillAdapter rAdapter;
    EditText dboxMedDose;
    TextView  dboxMedName,dboxMedStart,dboxMedEnd;
    String medNameEditbox="",medDoseEditbox="",medStartEditbox="",medEndEditbox="",medIdEditbox="";

//    FloatingActionButton fab;
    ProgressBar pb;
    TextView txt;
    RelativeLayout rl;
    String presId,customerphone;
    public static int pos;
    Vibrator vb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_refill);



        Bundle extra = getIntent().getExtras();

        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        LONG_CLICK_FLAG=0;
        presId = extra.getString("presId");
        customerphone=extra.getString("customerphone");
        Log.d("customerphone",customerphone);
        rl=(RelativeLayout)findViewById(R.id.rel);
        recyclerView = (RecyclerView) findViewById(R.id.re);
//        deletebtn=(ImageButton)findViewById(R.id.delete);
//        deletebtn.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        pb= (ProgressBar) findViewById(R.id.pb) ;
        txt=(TextView) findViewById(R.id.loadingtxt);
//        fab=(FloatingActionButton)findViewById(R.id.fab1);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = (TextView)toolbar.findViewById(R.id.title);

        rAdapter = new RefillAdapter(refillModelList);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // set the adapter
        recyclerView.setAdapter(rAdapter);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showCustomDialog(-1);
//            }
//        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                pos=position;
                showCustomDialog(pos);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getSupportActionBar().setTitle("");
        fetchData(0);
    }

    @Override
    public void onBackPressed() {
        if(LONG_CLICK_FLAG==1){
            LONG_CLICK_FLAG=0;
            rAdapter.notifyDataSetChanged();
//            deletebtn.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
    }
// Fetch Request for data
    public void fetchData(final int count){
        if(count==1){
            recyclerView.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);
            pb.setVisibility(View.VISIBLE);
            txt.setText("Refreshing Data...");
            txt.setVisibility(View.VISIBLE);
            refillModelList.clear();
            Log.d("My tag 004","I am the list data after clearing!");
            Log.d("My tag004",refillModelList+"");
        }
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
                        Log.d("Mytag 006","Days "+i+" = "+dose);
                        RefillModel detail = new RefillModel(medName,dose,refill,endDate,id);
                        refillModelList.add(detail);
                        Log.d("My tag 005","Refill list   "+refillModelList.get(i).getDosage());
                        rAdapter.notifyDataSetChanged();

                    }
//                    Log.d("responsemodellist",jarr+"");
//                    Log.d("response",response+"");

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


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

        int MY_SOCKET_TIMEOUT_MS = 50000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(PrescriptionRefillActivity.this);
        requestQueue.add(stringRequest);
    }



    //For Refill Prescription

    public JSONObject getJsonFromMyFormObjectRefill(ArrayList<RefillModel> refillModelList) throws JSONException {
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {

            for (int i = 0; i < refillModelList.size(); i++) {
                JSONObject formDetailsJson = new JSONObject();
                //formDetailsJson.put("medicineName", refillModelList.get(i).getMedName());
                formDetailsJson.put("days", String.valueOf(refillModelList.get(i).getDosage()));
                //formDetailsJson.put("refilldate", String.valueOf(refillModelList.get(i).getRefillDate()));
                //formDetailsJson.put("enddate"+(i+1), String.valueOf(refillModelList.get(i).getEndDate()));
                formDetailsJson.put("medicine", String.valueOf(refillModelList.get(i).getMedicineid()));
                jsonArray.put(formDetailsJson);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        responseDetailsJson.put("medicineDetailList",jsonArray);

        Log.d("responseDetailsEdit",responseDetailsJson+"");
        Log.d("mytag",presId+"");
        //Log.d("mytag",refillModelList.size()+"");
        //Log.d("jsonArrayEdit",jsonArray+"");


        return responseDetailsJson;


    }


    public void sendRefillDataList(final ArrayList<RefillModel> refillModelList) {
        String url = "https://pharmcrm.herokuapp.com/api/repeatmed/";
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
                                Log.d("My tag001","I am in the block where result code is 1");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("My tag 002","I was here bro!\nTu galat hai");
                        Toast.makeText(PrescriptionRefillActivity.this,response,Toast.LENGTH_LONG).show();
                        fetchData(1);
                        //Toast.makeText(MedicineData.this,msg+""+ result +"",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PrescriptionRefillActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                try {
                    //params.put("prescid",presId);
                    //params.put("counter",String.valueOf(presciptionEditModelList.size()));\
                    //params.put("customerphoneedit",customerphone);
                    //params.put("prescid",presId);
                    //params.put("counter","0");
                    //params.put("dataadd","{\"presciptionAddModelList\":[{}]}");
                    //params.put("datadelete","{\"presciptionDeleteModelList\":[{}]}");
                    //params.put("cnumber",customerphone);
                    params.put("data",String.valueOf(getJsonFromMyFormObjectRefill(refillModelList)));
                    Log.d("in dataeditresponse",String.valueOf(getJsonFromMyFormObjectRefill(refillModelList)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }

        };

        int MY_SOCKET_TIMEOUT_MS = 50000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Log.d("My tag 003","I am leaving this function bro!");
    }









    public void showCustomDialog(final int pos){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrescriptionRefillActivity.this);
        // Get the layout inflater
        LayoutInflater linf = LayoutInflater.from(getApplicationContext());
        final View inflator = linf.inflate(R.layout.prescription_refill_dialogbox, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        // Setting Dialog Title


        // Setting Dialog Message
        // Setting Icon to Dialog
        alertDialog.setView(inflator);

        final TextView dboxMedId,ds,de,dm,m;

        dboxMedName = (TextView) inflator.findViewById(R.id.dboxMedName);
        dboxMedDose = (EditText) inflator.findViewById(R.id.dboxMedDose);
        dboxMedStart = (TextView) inflator.findViewById(R.id.dboxMedStart);
        dboxMedEnd = (TextView) inflator.findViewById(R.id.dboxMedEnd);
        dboxMedId = (TextView) inflator.findViewById(R.id.dboxMedId);
        ds=(TextView)inflator.findViewById(R.id.toRemove1);
        de=(TextView)inflator.findViewById(R.id.toRemove2);
        dm=(TextView)inflator.findViewById(R.id.medId);
        m=(TextView)inflator.findViewById(R.id.medIdColon);

        String btn="";
        if(pos!=-1) {
            alertDialog.setTitle("Edit Dosage...");
            medNameEditbox = refillModelList.get(pos).getMedName();
            medDoseEditbox = refillModelList.get(pos).getDosage();
            medStartEditbox = refillModelList.get(pos).getRefillDate();
            medEndEditbox = refillModelList.get(pos).getEndDate();
            medIdEditbox = refillModelList.get(pos).getMedicineid();
            btn="EDIT";
            dboxMedName.setText(medNameEditbox);
            dboxMedDose.setText(medDoseEditbox);
            ds.setVisibility(View.GONE);
            de.setVisibility(View.GONE);
            dboxMedStart.setVisibility(View.GONE);
            dboxMedEnd.setVisibility(View.GONE);
            dboxMedId.setText(medIdEditbox);

        }
//        else{
//            alertDialog.setTitle("Add New...");
//            btn="Add";
//            dboxMedName.setText("");
//            dboxMedDose.setText("");
//            dboxMedId.setVisibility(View.GONE);
//            dboxMedStart.setVisibility(View.GONE);
//            dboxMedEnd.setVisibility(View.GONE);
//            ds.setVisibility(View.GONE);
//            de.setVisibility(View.GONE);
//            dm.setVisibility(View.GONE);
//            m.setVisibility(View.GONE);
//        }


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {


                ArrayList<RefillModel> refillModelListEdit = new ArrayList<>();

                ArrayList<String> med = new ArrayList<String>();

                String medname = dboxMedName.getText().toString().trim();
                String meddose = dboxMedDose.getText().toString().trim();
                String medstart = dboxMedStart.getText().toString().trim();
                String medend = dboxMedEnd.getText().toString().trim();
                String medId = dboxMedId.getText().toString().trim();
                if (!(TextUtils.isEmpty(medname) && TextUtils.isEmpty(meddose)) ) {
                    if (pos != -1) {
                        if (!(medNameEditbox.equals(medname) && medDoseEditbox.equals(meddose))) {
                            refillModelList.remove(pos);
                            refillModelList.add(pos, new RefillModel(medname, meddose, medStartEditbox, medEndEditbox, medId));
                            refillModelListEdit.add(new RefillModel(medname, meddose, medStartEditbox, medEndEditbox, medId));
                            rAdapter.notifyDataSetChanged();


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
                                refillModelList.remove(pos);
                                refillModelList.add(pos, new RefillModel(medNameEdit, meddoseEdit, medStartEditbox, medEndEditbox, medIdEdit));
                                rAdapter.notifyDataSetChanged();
                                Log.d("mytag", "You were right1");

                            } else {
                                med.add(medIdEdit);
                                refillModelList.add(new RefillModel(medNameEdit, meddoseEdit, medStartEditbox, medEndEditbox, medIdEdit));
                                rAdapter.notifyDataSetChanged();
                                Log.d("mytag", "You were right");

                            }


                             sendRefillDataList(refillModelListEdit);
                             //fetchData(1);
                        } else {
                            Toast.makeText(PrescriptionRefillActivity.this, "Don't add save values again, make some changes bro!", Toast.LENGTH_SHORT).show();

                        }

                    }
//                    else {
//                        presciptionAddModelList.add(new PresciptionModel(medname, meddose, "0", "0", "0"));
//                        pAddAdapter.notifyDataSetChanged();
//                        // presciptionModelList.add(new PresciptionModel(medname, meddose, "0", "0", "0"));
//                        // pAdapter.notifyDataSetChanged();
//                        sendAddDataList(presciptionAddModelList);
//                        //fetchData(1);
//                    }


                    // Write your code here to invoke YES event

                }



                else{
                    Toast.makeText(PrescriptionRefillActivity.this, "Please Enter Some Values!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(PrescriptionRefillActivity.this, "You clicked on NO", Toast.LENGTH_SHORT).show();
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
        //inflater.inflate(R.menu.toolbar_inflated_menu, menu);
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
