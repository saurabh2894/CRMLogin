package com.crm.pharmbooks.PharmCRM;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.crm.pharmbooks.PharmCRM.Login.MyPREFERENCES;

public class MainActivity extends AppCompatActivity
        implements DashBoard.OnFragmentInteractionListener,LoyaltyFragment.OnFragmentInteractionListener,
        NewFragment.OnFragmentInteractionListener,AboutUs.OnFragmentInteractionListener,ContactUs.OnFragmentInteractionListener,View.OnClickListener{

    static String LOAD_FRAG_TAG=" ";
    DrawerLayout drawer;
    FrameLayout frame;
    int flag=0;
    static String CURRENT_SELECTION_FLAG="dashboard";
    static String PREVIOUS_SELECTION_FLAG="dashboard";
    TextView title;
    ImageButton dashboard,customer,refill,transaction,loyalty,aboutUs,contactUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);

        title = (TextView)findViewById(R.id.title);

        //defining the Tabs
        dashboard = (ImageButton)findViewById(R.id.dashboard);
        customer = (ImageButton)findViewById(R.id.customer);
        refill = (ImageButton)findViewById(R.id.refill);
        transaction = (ImageButton)findViewById(R.id.transaction);
        loyalty = (ImageButton)findViewById(R.id.loyalty);

        aboutUs = (ImageButton)findViewById(R.id.about_us);
        contactUs = (ImageButton)findViewById(R.id.contact_us);
        dashboard.setOnClickListener(this);
        customer.setOnClickListener(this);
        refill.setOnClickListener(this);
        transaction.setOnClickListener(this);
        loyalty.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        contactUs.setOnClickListener(this);

        aboutUs.setVisibility(View.GONE);
        contactUs.setVisibility(View.GONE);
        frame = (FrameLayout) findViewById(R.id.frame);

        loadFragment();
        setSelected();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_drawer,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case R.id.about_us: onClick(aboutUs);
                break;
            case R.id.contact_us : onClick(contactUs);
                break;
            case R.id.logout : finish();
                SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putString("username", null);
                editor.putString("password", null);
                editor.commit();
                startActivity(new Intent(MainActivity.this,Login.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        }
//        else
            if(flag==0&&LOAD_FRAG_TAG.equals("About us")&&LOAD_FRAG_TAG.equals("Contact Us")){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Confirm Exit!");
            alertDialog.setMessage("Do You Really Want To Exit?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.super.onBackPressed();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();

        }
        else if(LOAD_FRAG_TAG.equals("About us")||LOAD_FRAG_TAG.equals("Contact Us")) {
                if (CURRENT_SELECTION_FLAG.equals("dashboard")) {
                    onClick(dashboard);
                } else if (CURRENT_SELECTION_FLAG.equals("refill")) {
                    onClick(refill);
                } else if (CURRENT_SELECTION_FLAG.equals("customer")) {
                    onClick(customer);
                } else if (CURRENT_SELECTION_FLAG.equals("transaction")) {
                    onClick(transaction);
                } else if (CURRENT_SELECTION_FLAG.equals("loyalty")) {
                    onClick(loyalty);
                }
            }
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void loadFragment(){
        if(LOAD_FRAG_TAG=="DashBoard"){
            title.setText("DashBoard");
            Fragment fragment = DashBoard.newInstance();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, "DashBoard");
            fragmentTransaction.commitAllowingStateLoss();
        }
        else if(LOAD_FRAG_TAG=="Customer Information"){
            title.setText("Customer Information");
            Fragment fragment = NewFragment.newInstance();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, "New Fragment");
            fragmentTransaction.commitAllowingStateLoss();
        }
        else if(LOAD_FRAG_TAG=="Transactional"){
            title.setText("Transactional Messages");
            Fragment fragment = new TransactionalMessage();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, "Transactional Data");
            fragmentTransaction.commitAllowingStateLoss();
        }
        else if(LOAD_FRAG_TAG=="About us"){
            title.setText("About us");
            Fragment fragment = new AboutUs();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, "About Us");
            fragmentTransaction.commitAllowingStateLoss();
        }
        else if(LOAD_FRAG_TAG=="Contact Us"){
            title.setText("Contact Us");
            Fragment fragment = new ContactUs();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, "Contact Us");
            fragmentTransaction.commitAllowingStateLoss();
        }
        else if(LOAD_FRAG_TAG=="Refill"){
            title.setText("Refilling");
            Fragment fragment = new RefillListActivity();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, "Refill");
            fragmentTransaction.commitAllowingStateLoss();
        }
        else if(LOAD_FRAG_TAG=="Loyalty"){
            title.setText("Loyalty");
            Fragment fragment = new LoyaltyFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, "Loyalty");
            fragmentTransaction.commitAllowingStateLoss();
        }
        else {
            title.setText("DashBoard");
            Fragment fragment = DashBoard.newInstance();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, "DashBoard");
        fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == dashboard) {
            if(!PREVIOUS_SELECTION_FLAG.equals("dashboard")) {
                CURRENT_SELECTION_FLAG = "dashboard";
                setUnselected();
                setSelected();
            }
            LOAD_FRAG_TAG="DashBoard";

            loadFragment();

        } else if (view == refill) {
            if(!PREVIOUS_SELECTION_FLAG.equals("refill")) {
                CURRENT_SELECTION_FLAG = "refill";
                setUnselected();
                setSelected();
            }
            LOAD_FRAG_TAG="Refill";

            loadFragment();

        } else if (view == customer) {
            if(!PREVIOUS_SELECTION_FLAG.equals("customer")) {
                CURRENT_SELECTION_FLAG = "customer";
                setUnselected();
                setSelected();
            }
            LOAD_FRAG_TAG="Customer Information";

            loadFragment();
        }
        else if (view == transaction) {
            if(!PREVIOUS_SELECTION_FLAG.equals("transaction")) {
                CURRENT_SELECTION_FLAG = "transaction";
                setUnselected();
                setSelected();
            }
            LOAD_FRAG_TAG="Transactional";

            loadFragment();
        }
        else if (view == loyalty) {
            if(!PREVIOUS_SELECTION_FLAG.equals("loyalty")) {
                CURRENT_SELECTION_FLAG = "loyalty";
                setUnselected();
                setSelected();
            }
            LOAD_FRAG_TAG="Loyalty";

            loadFragment();
        }
        else if (view == aboutUs) {
            LOAD_FRAG_TAG="About us";

            loadFragment();
        }
        else if (view == contactUs) {
            LOAD_FRAG_TAG="Contact Us";

            loadFragment();
        }
    }
    public void setSelected(){
        if(CURRENT_SELECTION_FLAG.equals("dashboard")){
            PREVIOUS_SELECTION_FLAG="dashboard";
            dashboard.setImageResource(R.drawable.ic_dashboard_selected);
        }
        else if(CURRENT_SELECTION_FLAG.equals("refill")){
            PREVIOUS_SELECTION_FLAG="refill";
            refill.setImageResource(R.drawable.ic_refill_selected);
        }
        else if(CURRENT_SELECTION_FLAG.equals("customer")){
            PREVIOUS_SELECTION_FLAG="customer";
            customer.setImageResource(R.drawable.ic_customer_selected);
        }
        else if(CURRENT_SELECTION_FLAG.equals("transaction")){
            PREVIOUS_SELECTION_FLAG="transaction";
            transaction.setImageResource(R.drawable.ic_transaction_selected);
        }
        else if(CURRENT_SELECTION_FLAG.equals("loyalty")){
            PREVIOUS_SELECTION_FLAG="loyalty";
            loyalty.setImageResource(R.drawable.ic_loyalty_selected);
        }
    }
    public void setUnselected(){
        if(PREVIOUS_SELECTION_FLAG.equals("dashboard")){
            dashboard.setImageResource(R.drawable.ic_dashboard);
        }
        else if(PREVIOUS_SELECTION_FLAG.equals("refill")){
            refill.setImageResource(R.drawable.ic_refill);
        }
        else if(PREVIOUS_SELECTION_FLAG.equals("customer")){
            customer.setImageResource(R.drawable.ic_customer);
        }
        else if(PREVIOUS_SELECTION_FLAG.equals("transaction")){
            transaction.setImageResource(R.drawable.ic_transaction);
        }
        else if(PREVIOUS_SELECTION_FLAG.equals("loyalty")){
            loyalty.setImageResource(R.drawable.ic_loyalty);
        }
    }


}

