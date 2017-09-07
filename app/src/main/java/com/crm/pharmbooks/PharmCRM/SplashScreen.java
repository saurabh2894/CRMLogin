package com.crm.pharmbooks.PharmCRM;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import static com.crm.pharmbooks.PharmCRM.Login.MyPREFERENCES;

public class SplashScreen extends AppCompatActivity {
ProgressBar progressBar;
    public String username,password;
    SharedPreferences sharedpreferences;
    int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(counter);

        sharedpreferences = this.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    while (counter<100){
                        Thread.sleep(10);
                        counter++;
                        progressBar.setProgress(counter);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String restoredTextuser = sharedpreferences.getString("username", null);
                String restoredTextpass = sharedpreferences.getString("password", null);


                if (restoredTextuser != null && restoredTextpass!=null) {
                    loadActivity("yes");
                }else{
                    loadActivity("no");
                }
            }
        }).start();
    }

    public void loadActivity(String n){
        if(n.equals("yes")){
            finish();
            startActivity(new Intent(SplashScreen.this,MainActivity.class));
        }
        else{
            finish();
            startActivity(new Intent(SplashScreen.this,Login.class));
        }
    }
}
