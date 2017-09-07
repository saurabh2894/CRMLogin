package com.crm.pharmbooks.PharmCRM;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class Login extends AppCompatActivity {

    EditText username;
    EditText password;
    Button Signin;
    String user_var, pass_var;
    int res ;
    public static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        Signin = (Button) findViewById(R.id.Signin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView title = (TextView)toolbar.findViewById(R.id.title);



        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_var = username.getText().toString();
                pass_var = password.getText().toString();
                sendR(user_var, pass_var);

            }
        });
    }

    public void sendR(final String username_var, final String password_var) {



        String url = "https://pharmcrm.herokuapp.com/api/login/";
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
                                /*
                                Changing the intent to start the MainActivity after Successful login
                                */
                               Intent i = new Intent(getApplicationContext(),MainActivity.class);
                               startActivity(i);
                                //SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                //SharedPreferences.Editor editor = sharedpreferences.edit();
                                SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                                editor.putString("username", String.valueOf(user_var));
                                editor.putString("password", String.valueOf(pass_var));

                                editor.commit();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                        Toast.makeText(Login.this,msg+""+ res +"",Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                })




        {
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("pharmname",username_var);
                params.put("password",password_var);

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



