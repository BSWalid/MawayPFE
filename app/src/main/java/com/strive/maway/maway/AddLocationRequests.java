package com.strive.maway.maway;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.strive.maway.maway.Admin.RequestsItemAdapter;
import com.strive.maway.maway.Location;
import com.strive.maway.maway.LocationInformations;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddLocationRequests extends Fragment {
    View mView;
    Context C;
    private  Firebase mRef;
    FragmentManager fm ;


    public AddLocationRequests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
               mView = inflater.inflate(R.layout.fragment_add_location_requests, container, false);
        C=getActivity().getApplicationContext();

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fm = getActivity().getSupportFragmentManager();
        RequestUpdate();
    }

    public void RequestUpdate(){


        mRef= new Firebase("https://maway-1520842395181.firebaseio.com/Requests");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //we need a list where we will store our locations that we injected
                ArrayList<LocationInformations> locationInformationsList = new ArrayList<>();

                ArrayList<Location> typeList=new ArrayList<>();

                for(DataSnapshot innerData : dataSnapshot.getChildren())
                {   String lat,lng, placeName,source, vicinity,sender,requestID;
                    String id = mRef.getKey();
                    String type;


                    // initialise our LocationInformation that we will add to our list if it's stored only in firebase

                    LocationInformations Linfo = new LocationInformations(innerData.getKey(),"","","","","","","");


                    for ( DataSnapshot innerInnerData : innerData.getChildren()) {

                        String key = innerInnerData.getKey();

                        switch (key)
                        {
                            case "type" :

                                type = innerInnerData.getValue(String.class);
                                Location L  = new Location(innerData.getKey(),type);
                                typeList.add(L);
                                Linfo.setType(type);
                        }

                        if(key.equals("latitude")){
                            lat = innerInnerData.getValue(String.class);
                            Linfo.setLatitude(lat);
                        }
                        if(key.equals("longitude")){
                            lng = innerInnerData.getValue(String.class);
                            Linfo.setLongitude(lng);
                        }
                        if(key.equals("placeName")){
                            placeName = innerInnerData.getValue(String.class);
                            Linfo.setPlaceName(placeName);
                        }
                        if(key.equals("vicinity")){
                            vicinity =innerInnerData.getValue(String.class);
                            Linfo.setVicinity(vicinity);
                        }
                        if(key.equals("type")){
                            source=innerInnerData.getValue(String.class);
                            Linfo.setType(source);
                        }
                        if(key.equals("RequestSender")){
                            sender=innerInnerData.getValue(String.class);
                            Linfo.setSender(sender);
                        }
                        if(key.equals("requestID")){
                            requestID=innerInnerData.getValue(String.class);
                            Linfo.setRequestID(requestID);
                        }
                    }
                    // If that location is injected then add to our list

                        locationInformationsList.add(Linfo);

                }

                RequestsItemAdapter adapter = new RequestsItemAdapter(locationInformationsList,C,fm);

                RecyclerView rv = ( RecyclerView) mView.findViewById(R.id.LocationAddRequests);
                LinearLayoutManager layoutManager = new LinearLayoutManager(C,LinearLayoutManager.VERTICAL,false);
               layoutManager.setStackFromEnd(true);
                rv.setLayoutManager(layoutManager);
                rv.setAdapter(adapter);







            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
