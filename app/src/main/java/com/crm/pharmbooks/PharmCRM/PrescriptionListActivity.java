package com.crm.pharmbooks.PharmCRM;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.ExpandableListAdapter;

import static com.crm.pharmbooks.PharmCRM.Login.MyPREFERENCES;

public class PrescriptionListActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeaderCommaValue;
    List<String> listDataHeaderSpaceValue;
    HashMap<String, List<String>> listDataChild_PrescriptionValue;
    HashMap<String, List<String>> listPresId;
    String username;
    ProgressBar pb;
    TextView txt;
    RelativeLayout rl;
    private EditText SearchBoxExistingRefill;
    private ImageView searchicon;




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
        SearchBoxExistingRefill=(EditText)findViewById(R.id.SearchBoxExistingRefill);
        searchicon=(ImageView)findViewById(R.id.searchicon);

        SharedPreferences sharedpreferences = this.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        String restoredText = sharedpreferences.getString("username", null);
        if (restoredText != null) {
            username = sharedpreferences.getString("username", "No name defined");//"No name defined" is the default value.
        }
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setVisibility(View.GONE);
        searchicon.setVisibility(View.GONE);
        SearchBoxExistingRefill.setVisibility(View.GONE);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent = new Intent(PrescriptionListActivity.this,CustomerPrescription.class);
                intent.putExtra("presId",listPresId.get(listDataHeaderSpaceValue.get(i)).get(i1));


                String str = listDataHeaderCommaValue.get(i);
                String[] parts = str.split(",");
                String stringreq = parts[1];
                intent.putExtra("customerphone",stringreq);


                startActivity(intent);
                Log.d("mytag",listPresId.get(listDataHeaderSpaceValue.get(i)).get(i1));

                return false;
            }
        });
     //   prepareListData();
        sendR();

        listAdapter = new ExpandableListAdapter(this, listDataHeaderSpaceValue, listDataChild_PrescriptionValue);


        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrescriptionListActivity.this);
                alertDialog.setTitle("Remove Entry...");
                alertDialog.setMessage("Do You Really Want To Remove This Customer?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listDataHeaderSpaceValue.remove(position);
                        listDataHeaderCommaValue.remove(position);
                        listAdapter.notifyDataSetChanged();
                    }
                });
               alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });
                alertDialog.show();
                return true;
            }
        });


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
        listDataHeaderCommaValue = new ArrayList<String>();
        listDataHeaderSpaceValue = new ArrayList<String>();
        listDataChild_PrescriptionValue = new HashMap<String, List<String>>();
        listPresId = new HashMap<String , List<String>>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        expListView.setVisibility(View.VISIBLE);
                        searchicon.setVisibility(View.VISIBLE);
                        SearchBoxExistingRefill.setVisibility(View.VISIBLE);
                        rl.setVisibility(View.GONE);
                        pb.setVisibility(View.GONE);
                        txt.setVisibility(View.GONE);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i =0; i<jsonArray.length();i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("custmorname");
                                String phone = object.getString("custmornumber");
                                listDataHeaderCommaValue.add(name + ","+ phone);
                                listDataHeaderSpaceValue.add(name + " : "+ phone);
                                JSONArray array = object.getJSONArray("prescriptionid");
                                List<String>  prescriptionlist = new ArrayList<String>();
                                List<String>  prescriptionlistshow = new ArrayList<String>();
                                for(int j = 0; j<array.length();j++)
                                {
                                    prescriptionlist.add(array.getString(j));
                                    prescriptionlistshow.add("Prescription"+" "+(j+1));
                                }


                                listDataChild_PrescriptionValue.put(listDataHeaderSpaceValue.get(i),prescriptionlistshow );
                                listPresId.put(listDataHeaderSpaceValue.get(i),prescriptionlist );

                                //Log.d("mytag",listDataChild_PrescriptionValue+"");
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

