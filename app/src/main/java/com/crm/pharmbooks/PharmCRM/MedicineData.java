package com.crm.pharmbooks.PharmCRM;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
//import android.support.v7.app.ActionBar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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



import Adapters.MedicineAdapter;
import Model.MedicineDetailModel;

import static com.crm.pharmbooks.PharmCRM.Login.MyPREFERENCES;


public class MedicineData extends AppCompatActivity {
    private ArrayList<MedicineDetailModel> medicineDetailList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MedicineAdapter mAdapter;
    private EditText MedicineName, MedicineQuantity;
    private EditText MedicineNameDialogBox,MedicineQuantityDialogBox;
    private Button addButton;
    public static int pos=0;
    public static int LONG_CLICK_FLAG=0;
    ImageButton deletebtn;
    ImageButton back;
    String MedicineName_value,MedicineQuantity_value;
    String MedicineName_valuedialogbox,MedicineQuantity_valuedialogbox;
    String username;
    String Name,MobileNo,Address;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("mytag","In Medicine");

        setContentView(R.layout.activity_medicine_data);
        LONG_CLICK_FLAG=0;
        MedicineName = (EditText) findViewById(R.id.MedicineName);
        MedicineQuantity = (EditText) findViewById(R.id.MedicineQuantity);
        addButton = (Button) findViewById(R.id.addButton);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        deletebtn=(ImageButton)findViewById(R.id.delete);
        back=(ImageButton)findViewById(R.id.back);
        deletebtn.setVisibility(View.GONE);
        TextView title = (TextView)
                toolbar.findViewById(R.id.title);
        toolbar.setTitle("Existing");
        getSupportActionBar().setTitle("");
        SharedPreferences sharedpreferences = this.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        String restoredText = sharedpreferences.getString("username", null);
        if (restoredText != null) {
            username = sharedpreferences.getString("username", "No username defined");//"No name defined" is the default value.

            Log.d("usernamecheck",username);


        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medicineDetailList.size()==0) {
                    goUp();

                }
                else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MedicineData.this);
                    alertDialog.setTitle("Alert!...");
                    alertDialog.setMessage("The data you've entered will be lost if not saved!\nDo you want to save the data?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sendR();
                            goUp();
                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            goUp();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

        Bundle extra = getIntent().getExtras();
        Name = extra.getString("Name");
        MobileNo= extra.getString("MobileNo");
        Address= extra.getString("Address");
        Log.d("Name",Name);
        Log.d("MobileNo",MobileNo);
        Log.d("Address",Address);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(TextUtils.isEmpty(MedicineName.getText()) && TextUtils.isEmpty(MedicineQuantity.getText().toString().trim()))) {
                    MedicineName_value = MedicineName.getText().toString();
                    MedicineQuantity_value = MedicineQuantity.getText().toString();
                    MedicineDetailModel detail = new MedicineDetailModel(MedicineName_value, Integer.parseInt(MedicineQuantity_value));
                    medicineDetailList.add(detail);
                    mAdapter.notifyDataSetChanged();
                    MedicineName.setText("");
                    MedicineQuantity.setText("");
                    MedicineName.requestFocus();
                }
                else{
                    Toast.makeText(MedicineData.this, "Please Enter Some Values!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MedicineAdapter(medicineDetailList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // set the adapter
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                pos=position;

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MedicineData.this);
                // Get the layout inflater
                LayoutInflater linf = LayoutInflater.from(getApplicationContext());
                final View inflator = linf.inflate(R.layout.custom_dialogbox, null);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Save...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to update these details?");
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.saveicon);
                alertDialog.setView(inflator);



                MedicineNameDialogBox = (EditText) inflator.findViewById(R.id.MedicineNameDialogBox);
                MedicineQuantityDialogBox = (EditText) inflator.findViewById(R.id.MedicineQuantityDialogBox);

                final String name =   medicineDetailList.get(position).getMName();
                final String number = medicineDetailList.get(position).getMQuantity()+"";
                MedicineNameDialogBox.setText(name);
                MedicineQuantityDialogBox.setText(number);
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {


                        if (!(TextUtils.isEmpty(MedicineNameDialogBox.getText()) && TextUtils.isEmpty(MedicineQuantityDialogBox.getText().toString().trim()))) {
                            MedicineName_valuedialogbox = MedicineNameDialogBox.getText().toString();
                            MedicineQuantity_valuedialogbox = MedicineQuantityDialogBox.getText().toString();
                            medicineDetailList.remove(pos);
                            medicineDetailList.add(pos, new MedicineDetailModel(MedicineName_valuedialogbox, Integer.parseInt(MedicineQuantity_valuedialogbox)));
                        /*MedicineDetailModel detail = new MedicineDetailModel(MedicineName_value,Integer.parseInt(MedicineQuantity_value));
                        String name = detail.getMName();
                        Integer number = detail.getMQuantity();
                        //medicineDetailList.add(detail);
                        */


                            mAdapter.notifyDataSetChanged();


                            // Write your code here to invoke YES event
                            Toast.makeText(MedicineData.this, "You clicked on YES and added  " + MedicineName_valuedialogbox, Toast.LENGTH_SHORT).show();
                        }



                        else{
                            Toast.makeText(MedicineData.this, "Please Enter Some Values!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(MedicineData.this, "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                //return alertDialog.create();
                alertDialog.show();






                /*CustomDialogClass cdd=new CustomDialogClass(position,MedicineData.this);
                cdd.show();
                if(cdd.isShowing()) {
                    Log.d("mytag","medicine data dialog box call working");

                }*/

                MedicineDetailModel medicineDetailModel = new MedicineDetailModel(MedicineName_value,Integer.parseInt(MedicineQuantity_value));
                Toast.makeText(getApplicationContext(), medicineDetailModel.getMName() + " is selected!", Toast.LENGTH_SHORT).show();




            }



            // Toast.makeText(getApplicationContext(), medicineDetail.getMName() + " is selected!", Toast.LENGTH_SHORT).show();


            @Override
            public void onLongClick(View view, int position) {
                if (LONG_CLICK_FLAG == 0){
                    LONG_CLICK_FLAG = 1;
                pos = position;
                mAdapter.notifyDataSetChanged();
                deletebtn.setVisibility(View.VISIBLE);
                deletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LONG_CLICK_FLAG = 0;
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(MedicineData.this, medicineDetailList.get(pos).getMName() + " is deleted", Toast.LENGTH_LONG).show();
                        medicineDetailList.remove(pos);
                        mAdapter.notifyDataSetChanged();
                        deletebtn.setVisibility(View.GONE);

                    }
                });
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MedicineData.this);
                alertDialog.setTitle("Remove Entry...");
                alertDialog.setMessage("Do You Really Want To Remove This Entry?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(MedicineData.this, medicineDetailList.get(pos).getMName() + " is deleted", Toast.LENGTH_LONG).show();
                        medicineDetailList.remove(pos);
                        mAdapter.notifyDataSetChanged();

                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                //alertDialog.show();
                //Toast.makeText(getApplicationContext(), medicineDetail.getMName() + " is selected!", Toast.LENGTH_SHORT).show();
            }else {
                    Toast.makeText(MedicineData.this, "Delete or Unselect the previously selected value first!", Toast.LENGTH_SHORT).show();
                }
            }
        }));

    }

    public void goUp(){
        finish();
        startActivity(new Intent(MedicineData.this,CustomerDetail.class));
    }
    @Override
    public void onBackPressed() {
        if(LONG_CLICK_FLAG==1){
            LONG_CLICK_FLAG=0;
            mAdapter.notifyDataSetChanged();
            deletebtn.setVisibility(View.GONE);
        }else if (medicineDetailList.size()==0) {
            super.onBackPressed();

        }
        else{

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Alert!...");
            alertDialog.setMessage("The data you've saved will be lost if you leave!\nDo you want to save the data?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sendR();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MedicineData.super.onBackPressed();
                }
            });
            alertDialog.show();
        }

    }




    public JSONObject getJsonFromMyFormObject(ArrayList<MedicineDetailModel> medicineDetailModelList) throws JSONException {
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < medicineDetailModelList.size(); i++) {
                JSONObject formDetailsJson = new JSONObject();
                formDetailsJson.put("medicine_"+(i+1), medicineDetailModelList.get(i).getMName());
                formDetailsJson.put("days_"+(i+1), String.valueOf(medicineDetailModelList.get(i).getMQuantity()));

                jsonArray.put(formDetailsJson);


            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        responseDetailsJson.put("medicineDetailList", jsonArray);

        Log.d("mytag",responseDetailsJson+"");

        return responseDetailsJson;


    }










    public void sendR() {
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
                        Toast.makeText(MedicineData.this,response,Toast.LENGTH_LONG).show();
                        Log.d("notify","I'm here bro");
//                        for(int i=0;i<medicineDetailList.size();i++)
//                            medicineDetailList.remove(i);
                        medicineDetailList.clear();
                        mAdapter.notifyDataSetChanged();
                        //Toast.makeText(MedicineData.this,msg+""+ result +"",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MedicineData.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                try {
                    params.put("Cname",Name);
                    params.put("Cnumber",MobileNo);
                    params.put("Cadd",Address);
                    params.put("chemist",username);
                    params.put("counter",String.valueOf(medicineDetailList.size()));
                    params.put("data",getJsonFromMyFormObject(medicineDetailList)+"");

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

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        //ActionBar actionBar = getSupportActionBar();;
        //actionBar.setDisplayHomeAsUpEnabled(true);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.medicinedetailactionbar, menu);
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

            Log.d("mtag", "save icon call working");
            if (medicineDetailList.size() != 0) {
                sendR();

            } else
                Toast.makeText(MedicineData.this, "Please Enter Some Values!!", Toast.LENGTH_LONG).show();

            /*JSONObject json = null;
            try {
                json = getJsonFromMyFormObject(medicineDetailList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("json",json+"");
            new View.OnClickListener() {
                public void onClick(View view) {

                }
            };*/

        }
        else if(id==android.R.id.home){


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {

                super.onPause();
            }


    public void navigateUp(){
        Log.d("parent","Navigated to right path");
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
    }
}