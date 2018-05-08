package com.strive.maway.maway.Admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strive.maway.maway.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteLocationRequests extends Fragment {


    public DeleteLocationRequests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_location_requests, container, false);
    }

}
