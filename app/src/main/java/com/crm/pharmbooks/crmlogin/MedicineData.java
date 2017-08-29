package com.crm.pharmbooks.crmlogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import Adapters.MedicineAdapter;
import Model.MedicineDetailModel;


public class MedicineData extends AppCompatActivity {
    private ArrayList<MedicineDetailModel> medicineDetailModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MedicineAdapter mAdapter;
    //private FloatingActionButton fab;
    private EditText MedicineName, MedicineQuantity;
    private EditText MedicineNameDialogBox,MedicineQuantityDialogBox;
    private Button addButton;
    String MedicineName_value,MedicineQuantity_value;
    String MedicineName_valuedialogbox,MedicineQuantity_valuedialogbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_data);
        MedicineName = (EditText)findViewById(R.id.MedicineName);
        MedicineQuantity = (EditText) findViewById(R.id.MedicineQuantity);
        addButton = (Button) findViewById(R.id.addButton);





        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicineName_value = MedicineName.getText().toString();
                MedicineQuantity_value = MedicineQuantity.getText().toString();
                MedicineDetailModel detail = new MedicineDetailModel(MedicineName_value,Integer.parseInt(MedicineQuantity_value));
                medicineDetailModelList.add(detail);
                mAdapter.notifyDataSetChanged();
            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MedicineAdapter(medicineDetailModelList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // set the adapter
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {





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

                String name =   medicineDetailModelList.get(position).getMName();
                String number = medicineDetailModelList.get(position).getMQuantity()+"";
                MedicineNameDialogBox.setText(name);
                MedicineQuantityDialogBox.setText(number);



                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {



                        MedicineName_valuedialogbox = MedicineNameDialogBox.getText().toString();
                        MedicineQuantity_valuedialogbox = MedicineQuantityDialogBox.getText().toString();
                        medicineDetailModelList.remove(position);

                        medicineDetailModelList.add(position,new MedicineDetailModel(MedicineName_valuedialogbox,Integer.parseInt(MedicineQuantity_valuedialogbox)));
                        /*MedicineDetailModel detail = new MedicineDetailModel(MedicineName_value,Integer.parseInt(MedicineQuantity_value));
                        String name = detail.getMName();
                        Integer number = detail.getMQuantity();
                        //medicineDetailModelList.add(detail);
                        */


                        mAdapter.notifyDataSetChanged();


                        // Write your code here to invoke YES event
                        Toast.makeText(getApplicationContext(), "You clicked on YES and added  "+ MedicineName_valuedialogbox, Toast.LENGTH_SHORT).show();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
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

            @Override
            public void onLongClick(View view, int position) {
              //Toast.makeText(getApplicationContext(), medicineDetail.getMName() + " is selected!", Toast.LENGTH_SHORT).show();

            }
        }));

        /*fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                MedicineDetailModel medicineDetail;
                medicineDetail = mAdapter.getData();
                medicineDetailModelList.add(medicineDetail);
                Log.d("tag",medicineDetailModelList.get(medicineDetailModelList.size()-1).getMName());
                mAdapter.notifyDataSetChanged();
            }

        });*/
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
        responseDetailsJson.put("medicineDetailModelList", jsonArray);

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
                        //Toast.makeText(MedicineData.this,msg+""+ res +"",Toast.LENGTH_LONG).show();

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
                params.put("Cname","manya");
                params.put("Cnumber","9898876545");
                params.put("Cadd","delhi");
                params.put("counter",String.valueOf(medicineDetailModelList.size()));
                params.put("data",getJsonFromMyFormObject(medicineDetailModelList)+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);

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

            Log.d("mtag","save icon call working");
            sendR();
            /*JSONObject json = null;
            try {
                json = getJsonFromMyFormObject(medicineDetailModelList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("json",json+"");
            new View.OnClickListener() {
                public void onClick(View view) {

                }
            };*/

            return true;
        }

        return super.onOptionsItemSelected(item);
    }






}




