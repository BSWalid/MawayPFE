package com.strive.maway.maway.Admin;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.strive.maway.maway.R;

public class AdminPael extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pael);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewpager);

        TabLayout tl= ( TabLayout) findViewById(R.id.tab);
        tl.setupWithViewPager(viewpager);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.




    }
    public void setupViewPager (ViewPager vp)
    {
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        Bundle args = new Bundle();



        AddLocationRequests addLocationRequests = new AddLocationRequests();
        Users users = new Users();
        DeleteLocationRequests deleteLocationRequests = new DeleteLocationRequests();

        addLocationRequests.setArguments(args);
        users.setArguments(args);
        deleteLocationRequests.setArguments(args);
        adapter.addfragments(addLocationRequests,"Location Requests");
        adapter.addfragments(users,"Users");
        adapter.addfragments(deleteLocationRequests,"Delete Requests");
        vp.setAdapter(adapter);



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_pael, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
