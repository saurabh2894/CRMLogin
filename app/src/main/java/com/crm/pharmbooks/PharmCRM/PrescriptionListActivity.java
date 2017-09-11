package com.crm.pharmbooks.PharmCRM;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.ExpandableListAdapter;

import static com.crm.pharmbooks.PharmCRM.Login.MyPREFERENCES;

public class PrescriptionListActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    List<String> listDataHeader1;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> listPresId;
    String username;
    ProgressBar pb;
    TextView txt;
    RelativeLayout rl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = (TextView)toolbar.findViewById(R.id.title);

        rl=(RelativeLayout)findViewById(R.id.rel);
        pb= (ProgressBar) findViewById(R.id.pb) ;
        txt=(TextView) findViewById(R.id.loadingtxt);

        SharedPreferences sharedpreferences = this.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        String restoredText = sharedpreferences.getString("username", null);
        if (restoredText != null) {
            username = sharedpreferences.getString("username", "No name defined");//"No name defined" is the default value.
        }
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setVisibility(View.GONE);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent = new Intent(PrescriptionListActivity.this,CustomerPrescription.class);
                intent.putExtra("presId",listPresId.get(listDataHeader1.get(i)).get(i1));


                String str = listDataHeader.get(i);
                String[] parts = str.split(",");
                String stringreq = parts[1];
                intent.putExtra("customerphone",stringreq);


                startActivity(intent);
                Log.d("mytag",listPresId.get(listDataHeader.get(i)).get(i1));

                return false;
            }
        });
     //   prepareListData();
        sendR();

        listAdapter = new ExpandableListAdapter(this, listDataHeader1, listDataChild);


        // setting list adapter
        expListView.setAdapter(listAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.prescriptionlistactivityactionbar, menu);
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



    public void sendR() {
        String url = "https://pharmcrm.herokuapp.com/api/namedata/";
        listDataHeader = new ArrayList<String>();
        listDataHeader1 = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listPresId = new HashMap<String , List<String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        expListView.setVisibility(View.VISIBLE);
                        rl.setVisibility(View.GONE);
                        pb.setVisibility(View.GONE);
                        txt.setVisibility(View.GONE);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i =0; i<jsonArray.length();i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("custmorname");
                                String phone = object.getString("custmornumber");
                                listDataHeader.add(name + ","+ phone);
                                listDataHeader1.add(name + "  "+ phone);
                                JSONArray array = object.getJSONArray("prescriptionid");
                                List<String>  prescriptionlist = new ArrayList<String>();
                                List<String>  prescriptionlistshow = new ArrayList<String>();
                                for(int j = 0; j<array.length();j++)
                                {
                                    prescriptionlist.add(array.getString(j));
                                    prescriptionlistshow.add("Prescription"+" "+(j+1));
                                }


                                listDataChild.put(listDataHeader1.get(i),prescriptionlistshow );
                                listPresId.put(listDataHeader1.get(i),prescriptionlist );

                                //Log.d("mytag",listDataChild+"");
                                //Log.d("mytag",prescriptionlistshow+"");



                            }
                            listAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finally {


                        }

                        //Toast.makeText(MedicineData.this,msg+""+ res +"",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
               params.put("chemist",username);
                //params.put("chemist","balkeerat");

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

}

