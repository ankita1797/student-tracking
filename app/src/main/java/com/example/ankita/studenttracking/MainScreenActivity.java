package com.example.ankita.studenttracking;

/**
 * Created by Ankita on 10-04-2018.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import Adapter.StudentAdapter;
import Bean.LocationBean;
import Bean.OutpassBean;
import fragments.OutpassDetailsFragment;
import fragments.FragmentDrawer;
import fragments.HomeWardenFragment;
import fragments.HomeStudentFragment;
import fragments.ManageOutpassFragment;
import fragments.SignOutpassFragment;

public class MainScreenActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainScreenActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private String type,username;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseLocation;
    private DatabaseReference databaseOutpass;
    private FirebaseDatabase firebaseDatabase;
    private TextView tv_hellouser;
    private Date date1, date2;
    String e1, e2;
    private ArrayList<LocationBean> studentLocation=new ArrayList<>();
    public  ArrayList<OutpassBean> liststudents = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_hellouser = (TextView) findViewById(R.id.tv_hellouser);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences prefs = getSharedPreferences("user_login", MODE_PRIVATE);
        type=prefs.getString("type",null);
        username=prefs.getString("username",null);
        tv_hellouser.setText(username);



        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        new GetStudent().execute();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_log_out){
            SharedPreferences.Editor editor = getSharedPreferences("user_login", MODE_PRIVATE).edit();
            editor.putBoolean("isLogin", false);
            editor.putString("username",null);
            editor.putString("type",null);
            editor.putString("name",null);
            editor.commit();
            startActivity(new Intent(MainScreenActivity.this,LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        if(type.equals("warden"))
            displayViewWarden(position);
        else if(type.equals("student"))
            displayViewStudent(position);
    }

    private void displayViewWarden(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeWardenFragment(this,studentLocation);
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new ManageOutpassFragment(this, liststudents);
                title = getString(R.string.title_manageOutpass);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
    private void displayViewStudent(int position){
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeStudentFragment(this);
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new SignOutpassFragment(this);
                title = getString(R.string.title_signOutpass);
                break;
            case 2:
                fragment = new OutpassDetailsFragment(this,liststudents);
                title = getString(R.string.title_editprofile);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }
    }

    private class GetStudent extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(MainScreenActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();


        }

        @Override
        protected Void doInBackground(Void... voids) {

            firebaseDatabase= FirebaseDatabase.getInstance();

            databaseLocation=firebaseDatabase.getReference("Location");
            databaseLocation.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    studentLocation.clear();
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        LocationBean locationBean = postSnapshot.getValue(LocationBean.class);
                        studentLocation.add(locationBean);

                    }
                    String studentLocations = "";
                    for(int i=0;i<studentLocation.size();i++)
                    {
                       studentLocations +="Enrollment :"+studentLocation.get(i).getEnrollment()+"Name :"+studentLocation.get(i).getName()+"\nLatitude : "+studentLocation.get(i).getLat()+"\nLongitude : "
                               +studentLocation.get(i).getLongi()+"\n Locality : "+studentLocation.get(i).getLocality()+"\n Place:"+ studentLocation.get(i).getPlace()+"\n\n\n";
                    }
                    SharedPreferences.Editor editor = getSharedPreferences("user_login", MODE_PRIVATE).edit();
                    editor.putString("studentLocations",studentLocations);
                    editor.commit();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: ");
                }
            });


            databaseOutpass = firebaseDatabase.getReference("Outpass Details");
            databaseOutpass.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    liststudents.clear();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        OutpassBean outpassBean = postSnapshot.getValue(OutpassBean.class);
                        liststudents.add(outpassBean);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: ");
                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            // display the first navigation drawer view on app launch
            if(type.equals("warden"))
            {
                displayViewWarden(0);
            }
            else if(type.equals("student"))
            {
                displayViewStudent(0);
            }

        }
    }
}
