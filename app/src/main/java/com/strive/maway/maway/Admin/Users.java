package com.strive.maway.maway.Admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strive.maway.maway.LocationInformations;
import com.strive.maway.maway.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Users extends Fragment {

    View mView;


    public Users() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_users, container, false);
        userUpadter();
        return mView;
    }


    private void userUpadter(){

        List<LocationInformations> items =  new ArrayList<LocationInformations>();

        for ( int i= 0 ; i<20 ;i++){
            LocationInformations  L = new LocationInformations("5",
                    "5"
                    ,"5"
                    ,"5"
                    ,"5","5"
                    ,"5","5");
            items.add(L);

            UserItemAdapter adapter = new UserItemAdapter(items,getActivity().getApplicationContext());
            RecyclerView rv = ( RecyclerView) mView.findViewById(R.id.usersList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(adapter);




        }



    }

}
