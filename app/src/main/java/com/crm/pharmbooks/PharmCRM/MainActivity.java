package com.crm.pharmbooks.PharmCRM;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DashBoard.OnFragmentInteractionListener,
        NewFragment.OnFragmentInteractionListener{

    static String LOAD_FRAG_TAG=" ";
    DrawerLayout drawer;
    FrameLayout frame;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        frame = (FrameLayout) findViewById(R.id.frame);

        loadFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(!drawer.isDrawerOpen(GravityCompat.START)||flag==0){

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

//            Snackbar.make(frame,"Do You Really want to exit",Snackbar.LENGTH_INDEFINITE).
//                    setAction("Exit", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            MainActivity.super.onBackPressed();
//                        }
//                    }).show();
        }
//        if(flag==1){
//            super.onBackPressed();
//        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportActionBar().setTitle("DashBoard");
            LOAD_FRAG_TAG="DashBoard";
            loadFragment();

        } else if (id == R.id.nav_refill) {
            LOAD_FRAG_TAG="Refill";
            getSupportActionBar().setTitle("Refilling");
            loadFragment();

        } else if (id == R.id.nav_promotion) {
            LOAD_FRAG_TAG="Transactional";
            getSupportActionBar().setTitle("Transactional Message");
            loadFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void loadFragment(){
        if(LOAD_FRAG_TAG=="DashBoard"){

            Fragment fragment = DashBoard.newInstance();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, "DashBoard");
            fragmentTransaction.commitAllowingStateLoss();
        }
        else if(LOAD_FRAG_TAG=="Refill"){

            Fragment fragment = NewFragment.newInstance();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, "New Fragment");
            fragmentTransaction.commitAllowingStateLoss();
        }
        else if(LOAD_FRAG_TAG=="Transactional"){

            Fragment fragment = new TransactionalMessage();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, "Transactional Data");
            fragmentTransaction.commitAllowingStateLoss();
        }
        else {
            Fragment fragment = DashBoard.newInstance();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, "DashBoard");
        fragmentTransaction.commitAllowingStateLoss();
        }
    }

}

