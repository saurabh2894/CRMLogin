package com.crm.pharmbooks.PharmCRM;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.crm.pharmbooks.PharmCRM.Login.MyPREFERENCES;

public class CustomerDetail extends AppCompatActivity {

    EditText Name;
    EditText MobileNo;
    EditText Address;
    Button Next;
    String Name_var, MobileNo_var, Address_var;
    int result;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        Name = (EditText)findViewById(R.id.Name);
        MobileNo = (EditText) findViewById(R.id.MobileNo);
        Address = (EditText) findViewById(R.id.Address);
        Next = (Button) findViewById(R.id.Next);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = (TextView)toolbar.findViewById(R.id.title);
        SharedPreferences sharedpreferences = this.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        String restoredText = sharedpreferences.getString("username", null);
        if (restoredText != null) {
            username = sharedpreferences.getString("username", "No name defined");//"No name defined" is the default value.
            Log.d("usernamecheck",username);
        }

       Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((!(TextUtils.isEmpty(Name.getText())) && !(TextUtils.isEmpty(MobileNo.getText().toString().trim())) && !(TextUtils.isEmpty(Address.getText().toString().trim())))) {
                    Name_var = Name.getText().toString();
                    MobileNo_var = MobileNo.getText().toString();
                    Address_var = Address.getText().toString();


                    Pattern p = Pattern.compile("\\d{10}");
                    Matcher m = p.matcher(MobileNo_var);
                    boolean b = m.matches();
                    if (b) {
                        Toast.makeText(CustomerDetail.this, "10 digits bingo", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CustomerDetail.this, MedicineData.class);
                        intent.putExtra("Name", String.valueOf(Name_var));
                        intent.putExtra("MobileNo", String.valueOf(MobileNo_var));
                        intent.putExtra("Address", String.valueOf(Address_var));
                        Log.d("mytag", "b is true");
                        //Intent i = new Intent(getApplicationContext(),MedicineData.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CustomerDetail.this, "Please enter valid number", Toast.LENGTH_SHORT).show();
                        Log.d("mytag", "b is false");
                    }

                    //sendR(Name_var, MobileNo_var, Address_var);
                } else {
                    Toast.makeText(CustomerDetail.this, "Please Enter Some Values!!!", Toast.LENGTH_SHORT).show();
                }
            }
       });

        Address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_GO){
                    NextFunction();
                    return true;
                }
                else{
                    return false;
                }
            }
        });
    }

    public void NextFunction(){
        Name_var = Name.getText().toString();
        MobileNo_var = MobileNo.getText().toString();
        Address_var = Address.getText().toString();

        Pattern p = Pattern.compile("\\d{10}");
        Matcher m = p.matcher(MobileNo_var);
        boolean b = m.matches();
        if(b){
            Toast.makeText(CustomerDetail.this,"10 digits bingo",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CustomerDetail.this,MedicineData.class);
            intent.putExtra("Name", String.valueOf(Name_var));
            intent.putExtra("MobileNo", String.valueOf(MobileNo_var));
            intent.putExtra("Address", String.valueOf(Address_var));
            Log.d("mytag","b is true");
            //Intent i = new Intent(getApplicationContext(),MedicineData.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(CustomerDetail.this, "Please enter valid number", Toast.LENGTH_SHORT).show();
            Log.d("mytag","b is false");
        }
    }



    /*
    public void sendR(final String Name_var, final String MobileNo_var, final String Address_var){


        String url = "https://pharmcrm.herokuapp.com/api/save/";
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

                                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString("Name", String.valueOf(Name));
                                editor.putString("MobileNo", String.valueOf(MobileNo));
                                editor.putString("Address", String.valueOf(Address));
                                editor.commit();

                                Intent i = new Intent(getApplicationContext(),MedicineData.class);
                                startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(CustomerDetail.this,response,Toast.LENGTH_LONG).show();
                        Toast.makeText(CustomerDetail.this,msg+""+ result +"",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerDetail.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                //params.put("Cname",Name_var);
                //params.put("Cnumber",MobileNo_var);
                //params.put("Cadd",Address_var);
                //params.put("chemist",username);

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

    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.customerdetailactionbar, menu);
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