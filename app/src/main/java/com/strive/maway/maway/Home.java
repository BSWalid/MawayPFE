package com.strive.maway.maway;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.strive.maway.maway.AccountSettings.UserSettings;
import com.strive.maway.maway.Admin.AdminPael;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    TextView  name ;
    Firebase mRef;
    String username,password,email;
    LinearLayout Logout;
    LinearLayout adminButton;
    LinearLayout appbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        password = getIntent().getExtras().getString("password");
        email = getIntent().getExtras().getString("email");



        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://maway-1520842395181.firebaseio.com/");


        Logout = findViewById(R.id.logout);


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(Home.this,Login.class));
                finish();
            }
        });
        SetUsername();


        //Fragment
        Map mainFragment = new Map();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.content,mainFragment, "main").commit();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appbar= findViewById(R.id.app_bar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        name =(TextView) navigationView.getHeaderView(0).findViewById(R.id.nameS);
        adminButton = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.AdminBtn);
        if(email.equals("walid@walid.com")){
            adminButton.setVisibility(View.VISIBLE);
            adminButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),AdminPael.class);
                    startActivity(i);


                }
            });


        }




        if(getIntent().getExtras().getString("username")==null&& name==null){


        }else
        {



            name.setText("Welcome "+getIntent().getExtras().getString("username"));
        }
        themeTextColorChange(navigationView,getResources().getColor(R.color.White),getResources().getColor(R.color.greyTextHospital));

        navigationView.setItemBackgroundResource(R.drawable.text_state);


    }
    public void themeTextColorChange(NavigationView navigationView,int colWhite,int colDark){

        int state[][] = new int[][]{
                new int[]{android.R.attr.state_checked},  //checked
                new int[] {-android.R.attr.state_checked}, // unchecked
        };
        int[] color =  new int[]{
                colWhite,colDark
        };
        ColorStateList colorStateList = new ColorStateList(state,color);
        navigationView.setItemTextColor(colorStateList);
        navigationView.setItemIconTintList(colorStateList);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        getMenuInflater().inflate(R.menu.menu2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically   clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setChecked(true);

        if (id == R.id.nav_addlocation) {

            addLocationRequest mainFragment = new addLocationRequest();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            appbar.setVisibility(View.VISIBLE);
            ft.replace(R.id.content,mainFragment, "main").commit();

        }
        else if (id == R.id.nav_settings) {
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            bundle.putString("password",password);
            UserSettings userSettingFragment = new UserSettings();
            userSettingFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            appbar.setVisibility(View.GONE);
            fragmentManager.beginTransaction().replace(R.id.content, userSettingFragment).commit();


        }
        else if (id == R.id.nav_home) {
            Map mainFragment = new Map();
            FragmentManager fragmentManager = getSupportFragmentManager();
            appbar.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction().replace(R.id.content, mainFragment).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }
    private void SetUsername(){
        String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Firebase mRef2= mRef.child("Users").child(mUid);

        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = findViewById(R.id.nameS);

                for(DataSnapshot innerData : dataSnapshot.getChildren())
                {

                    String key = innerData.getKey();

                    switch (key)
                    {
                        case "username":
                            //code here
                            username = innerData.getValue(String.class);
                            if(name!=null && username==null) {
                                name.setText("Welcome back");
                            }
                            if(name == null){}else{name.setText("Welcome "+username);}




                            break;




                }
                    }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }


}
