package com.crm.pharmbooks.crmlogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.crm.pharmbooks.crmlogin.Login.MyPREFERENCES;

public class TransactionalMessage extends AppCompatActivity {

    EditText Name;
    EditText MobileNo;
    EditText BillAmount;
    Button Next;
    String Name_var, MobileNo_var, BillAmount_var;
    int result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        Name = (EditText)findViewById(R.id.Name);
        MobileNo = (EditText) findViewById(R.id.MobileNo);
        BillAmount = (EditText) findViewById(R.id.BillAmount);
        Next = (Button) findViewById(R.id.Next);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name_var = Name.getText().toString();
                MobileNo_var = MobileNo.getText().toString();
                BillAmount_var = BillAmount.getText().toString();
                sendR(Name_var, MobileNo_var, BillAmount_var);

            }
        });


    }



    public void sendR(final String Name_var, final String MobileNo_var, final String BillAmount_var){


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

                                //SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                //SharedPreferences.Editor editor = sharedpreferences.edit();

                                //editor.putString("Name", String.valueOf(Name));
                                //editor.commit();

                                //Intent i = new Intent(getApplicationContext(),MedicineData.class);
                                //startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(TransactionalMessage.this,response,Toast.LENGTH_LONG).show();
                        Toast.makeText(TransactionalMessage.this,msg+""+ result +"",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TransactionalMessage.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("Cname",Name_var);
                params.put("Cnumber",MobileNo_var);
                params.put("Camt",BillAmount_var);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}