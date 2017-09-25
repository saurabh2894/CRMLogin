package com.crm.pharmbooks.PharmCRM;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
import java.util.List;
import java.util.Map;

import Adapters.ExpandableListAdapter;

import static android.media.CamcorderProfile.get;
import static com.crm.pharmbooks.PharmCRM.Login.MyPREFERENCES;

public class PrescriptionListActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeaderValue;

    HashMap<String, List<String>> listDataChild_PrescriptionValue;

    HashMap<String, List<String>> listPresId;
    String username;
    ProgressBar pb;
    TextView txt;
    RelativeLayout rl;
    private EditText SearchBoxExistingRefill;
    private ImageButton searchicon;
    int SEARCH_FLAG=0;
    public static int LONG_CLICK_FLAG=0,CHILDLONG_CLICK_FLAG=0;
    ImageButton deletebtn;
    public static int pos;
    Vibrator vb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);

        LONG_CLICK_FLAG=0;

        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setTitle("");
        TextView title = (TextView)toolbar.findViewById(R.id.title);

        deletebtn=(ImageButton)findViewById(R.id.delete);
        deletebtn.setVisibility(View.GONE);

        rl=(RelativeLayout)findViewById(R.id.rel);
        pb= (ProgressBar) findViewById(R.id.pb) ;
        txt=(TextView) findViewById(R.id.loadingtxt);
        SearchBoxExistingRefill=(EditText)findViewById(R.id.SearchBoxExistingRefill);
        SearchBoxExistingRefill.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH){
                    searchFunction();
                    return true;
                }
                else{
                    return false;
                }
            }
        });
        searchicon=(ImageButton) findViewById(R.id.searchicon);
        searchicon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                searchFunction();
            }
        });


        SharedPreferences sharedpreferences = this.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        String restoredText = sharedpreferences.getString("username", null);
        if (restoredText != null) {
            username = sharedpreferences.getString("username", "No name defined");//"No name defined" is the default value.
        }
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setVisibility(View.GONE);
        searchicon.setVisibility(View.GONE);
        SearchBoxExistingRefill.setVisibility(View.GONE);

        //on click for child

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Intent intent = new Intent(PrescriptionListActivity.this,CustomerPrescription.class);
                intent.putExtra("presId",listPresId.get(listDataHeaderValue.get(groupPosition)).get(childPosition));


                String str = listDataHeaderValue.get(groupPosition);
                String[] parts = str.split(" : ");
                String stringreq = parts[1];
                intent.putExtra("customerphone",stringreq);
                startActivity(intent);
                Log.d("mytag",listPresId.get(listDataHeaderValue.get(groupPosition)).get(childPosition));

                return false;
            }
        });
        fetchRequest();


        //long click for group and child
        listAdapter = new ExpandableListAdapter(this, listDataHeaderValue, listDataChild_PrescriptionValue);
        expListView.setAdapter(listAdapter);
        getExpandableListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick( AdapterView<?> parent, View view, int position, long id) {

                long packedPosition = expListView.getExpandableListPosition(position);

                int itemType = expListView.getPackedPositionType(packedPosition);
                int groupPosition = expListView.getPackedPositionGroup(packedPosition);
                int childPosition = expListView.getPackedPositionChild(packedPosition);


                // if group item clicked
                if (itemType == expListView.PACKED_POSITION_TYPE_GROUP) {
                    //  ...
                    onGroupLongClick(groupPosition);
                }
                //if child item clicked
                else if (itemType == expListView.PACKED_POSITION_TYPE_CHILD) {
                    //  ...
                    onChildLongClick(groupPosition, childPosition);
                }
                return false;
            }

            private void onGroupLongClick(final int groupPosition) {

                    if (LONG_CLICK_FLAG == 0){
                        LONG_CLICK_FLAG = 1;
                        if(SEARCH_FLAG==0){
                            pos = groupPosition;
                            listAdapter.notifyDataSetChanged();}
                        else{
                            pos=0;
                            getAdapter().notifyDataSetChanged();
                        }

                        long[] pattern = {0, 1000, 0};
                        vb.vibrate(pattern,0);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Thread.sleep(100);
                                    vb.cancel();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        deletebtn.setVisibility(View.VISIBLE);
                        deletebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LONG_CLICK_FLAG=0;
                                if(SEARCH_FLAG==0) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrescriptionListActivity.this);
                                    alertDialog.setTitle("Remove Entry...");
                                    alertDialog.setMessage("Do You Really Want To Remove This Customer?");
                                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String str = listDataHeaderValue.get(groupPosition);
                                            String[] parts = str.split(" : ");
                                            String stringreq = parts[1];
                                            Log.d("phonenostring",stringreq);
                                            sendDeleteParentRequest(stringreq);

                                            listAdapter.notifyDataSetChanged();
                                            listDataHeaderValue.remove(groupPosition);
                                            listAdapter.notifyDataSetChanged();

                                        }
                                    });
                                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            deletebtn.setVisibility(View.GONE);

                                        }

                                    });
                                    alertDialog.show();
                                    deletebtn.setVisibility(View.GONE);

                                }
                                else{
                                    expListView.setAdapter(listAdapter);
                                    SEARCH_FLAG=0;
                                    SearchBoxExistingRefill.setText("");
                                    listDataHeaderValue.remove(getIndex());
                                    listAdapter.notifyDataSetChanged();
                                    deletebtn.setVisibility(View.GONE);

                                }

                                deletebtn.setVisibility(View.GONE);

                            }

                        });

                    }else {
                        Toast.makeText(PrescriptionListActivity.this, "Delete or Unselect the previously selected value first!", Toast.LENGTH_SHORT).show();
                    }


            }
            private void onChildLongClick(final int groupPosition, final int childPosition) {

                String str = listDataHeaderValue.get(childPosition);
                Log.d("child click working",str);

                int index = expListView.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                expListView.setItemChecked(index,true);

                if (CHILDLONG_CLICK_FLAG == 0){
                    CHILDLONG_CLICK_FLAG = 1;
                    if(SEARCH_FLAG==0){
                        pos = groupPosition;
                        listAdapter.notifyDataSetChanged();}
                    else{
                        pos=0;
                        getAdapter().notifyDataSetChanged();
                    }

                    long[] pattern = {0, 1000, 0};
                    vb.vibrate(pattern,0);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Thread.sleep(100);
                                vb.cancel();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    deletebtn.setVisibility(View.VISIBLE);
                    deletebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CHILDLONG_CLICK_FLAG=0;
                            if(SEARCH_FLAG==0) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrescriptionListActivity.this);
                                alertDialog.setTitle("Remove Entry...");
                                alertDialog.setMessage("Do You Really Want To Remove This Prescription?");
                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String presID=listPresId.get(listDataHeaderValue.get(groupPosition)).get(childPosition);
                                        Log.d("presID check",presID);
                                        sendDeleteChildRequest(presID);


                                        String a=listDataChild_PrescriptionValue.get(listDataHeaderValue.get(groupPosition)).get(childPosition);

                                        Log.d("listDataChild",a);
                                        listAdapter.notifyDataSetChanged();
                                        listDataChild_PrescriptionValue.get(listDataHeaderValue.get(groupPosition)).remove(childPosition);
                                        //listDataChild_PrescriptionValue.remove(childPosition);
                                        listAdapter.notifyDataSetChanged();
                                        deletebtn.setVisibility(View.GONE);

                                    }
                                });
                                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deletebtn.setVisibility(View.GONE);

                                    }

                                });
                                alertDialog.show();
                                deletebtn.setVisibility(View.GONE);

                            }
                            else{
                                expListView.setAdapter(listAdapter);
                                SEARCH_FLAG=0;
                                SearchBoxExistingRefill.setText("");
                                listDataHeaderValue.remove(getIndex());
                                listAdapter.notifyDataSetChanged();
                                deletebtn.setVisibility(View.GONE);

                            }

                            deletebtn.setVisibility(View.GONE);

                        }

                    });

                }else {
                    Toast.makeText(PrescriptionListActivity.this, "Delete or Unselect the previously selected value first!", Toast.LENGTH_SHORT).show();
                }




            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);

        MenuInflater inflater = getMenuInflater();
       // inflater.inflate(R.menu.prescriptionlistactivityactionbar, menu);
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


    @Override
    public void onBackPressed() {
        if(SEARCH_FLAG==1){
            expListView.setAdapter(listAdapter);
            SEARCH_FLAG=0;
            SearchBoxExistingRefill.setText("");
        }
        else if(LONG_CLICK_FLAG==1){
            LONG_CLICK_FLAG=0;
            listAdapter.notifyDataSetChanged();
        }
        else if(CHILDLONG_CLICK_FLAG==1){
            CHILDLONG_CLICK_FLAG=0;
            listAdapter.notifyDataSetChanged();
        }
        else {
            super.onBackPressed();
        }
    }
//////////////for parent delete /////////////
int res;

    public void sendDeleteParentRequest(final String number) {
    String url = "https://pharmcrm.herokuapp.com/api/deleteuser/";
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    String msg = null;
                    try {
                        JSONObject object = new JSONObject(response);
                        msg = object.getString("msg");
                        res = object.getInt("res");
                        if(res == 1)
                        {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(PrescriptionListActivity.this,response,Toast.LENGTH_LONG).show();
                    Toast.makeText(PrescriptionListActivity.this,msg+""+ res +"",Toast.LENGTH_LONG).show();

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PrescriptionListActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                }
            })
    {
        @Override
        protected Map<String,String> getParams(){
            Map<String, String> params = new HashMap<>();
            params.put("number",number);

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

//////////////for child delete /////////////
    int result;

    public void sendDeleteChildRequest(final String prescid) {
        String url = "https://pharmcrm.herokuapp.com/api/deletepres/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String msg = null;
                        try {
                            JSONObject object = new JSONObject(response);
                            msg = object.getString("msg");
                            result = object.getInt("res");
                            if(result == 1)
                            {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(PrescriptionListActivity.this,response,Toast.LENGTH_LONG).show();
                        Toast.makeText(PrescriptionListActivity.this,msg+""+ result +"",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PrescriptionListActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("presid",prescid);

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


////fetch Request
    public void fetchRequest() {
        deletebtn.setVisibility(View.GONE);
        String url = "https://pharmcrm.herokuapp.com/api/namedata/";
        listDataHeaderValue = new ArrayList<String>();
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
                            for(int groupPosition =0; groupPosition<jsonArray.length();groupPosition++) {
                                JSONObject object = jsonArray.getJSONObject(groupPosition);
                                String name = object.getString("custmorname");
                                String phone = object.getString("custmornumber");
                                listDataHeaderValue.add(name + " : "+ phone);
                                JSONArray array = object.getJSONArray("prescriptionid");
                                List<String>  prescriptionlist = new ArrayList<String>();
                                List<String>  prescriptionlistshow = new ArrayList<String>();
                                for(int j = 0; j<array.length();j++)
                                {
                                    prescriptionlist.add(array.getString(j));
                                    prescriptionlistshow.add("Prescription"+" "+(j+1));
                                }
                                listDataChild_PrescriptionValue.put(listDataHeaderValue.get(groupPosition),prescriptionlistshow );
                                listPresId.put(listDataHeaderValue.get(groupPosition),prescriptionlist );

                                //Log.d("mytag",prescriptionlistshow+"");
                            }
                            Log.d("mytag",listDataChild_PrescriptionValue+"");

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



    int index;
    public void setIndex(int i){
        index=i;
    }

    public int getIndex(){
        return index;
    }
    ExpandableListAdapter listAdaptersearchfordelete;
    public void setAdapter(ExpandableListAdapter listAdaptersearch){
        listAdaptersearchfordelete=listAdaptersearch;
    }
    public ExpandableListAdapter getAdapter(){
        return listAdaptersearchfordelete;
    }
    public void searchFunction(){

        ArrayList<String> numbers = new ArrayList<String>();
        List<String> listDataHeaderSpaceSearch =  new ArrayList<String>();
        HashMap<String, List<String>> listDataChild_PrescriptionValueSearch;
        ExpandableListAdapter listAdaptersearch;

        if(SEARCH_FLAG==0){

            String number=SearchBoxExistingRefill.getText().toString().trim();
            for(int i = 0; i< listDataHeaderValue.size(); i++){
                Log.d("list value", listDataHeaderValue.get(i));
                Log.d("index",i+"");
                String str = listDataHeaderValue.get(i);
                String[] parts = str.split(" : ");
                if(parts[1].length()!=0&&parts.length!=0)
                { numbers.add(parts[1]);
                Log.d("num list",numbers.get(i));}
            }
            int count=0;
            if(numbers.contains(number)){

                for(int i=0;i<numbers.size();i++){
                    if(number.equals(numbers.get(i))&&count==0){
                        SEARCH_FLAG=1;
                        count=1;
                        Log.d("evil index",i+"");
                        setIndex(i);
                        listDataHeaderSpaceSearch.add(listDataHeaderValue.get(i));
                        listDataChild_PrescriptionValueSearch = new HashMap<String, List<String>>();
                        listDataChild_PrescriptionValueSearch.put(listDataHeaderValue.get(i),listDataChild_PrescriptionValue.get(listDataHeaderValue.get(i)));
                        Log.d("index",i+"");
                        Log.d("Value", listDataHeaderValue.get(i));
                        listAdaptersearch = new ExpandableListAdapter(PrescriptionListActivity.this, listDataHeaderSpaceSearch, listDataChild_PrescriptionValueSearch);
                        setAdapter(listAdaptersearch);
                        expListView.setAdapter(listAdaptersearch);
                    }
                }

            }else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrescriptionListActivity.this);
                alertDialog.setTitle("Oops...");
                alertDialog.setMessage("This customer does not exist!\nDo you want to add this customer?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(new Intent(PrescriptionListActivity.this,CustomerDetail.class));
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
            }

        }
    }


    public AdapterView getExpandableListView() {
        return expListView;
    }
}

