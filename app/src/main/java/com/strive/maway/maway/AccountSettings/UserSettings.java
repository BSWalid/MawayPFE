package com.strive.maway.maway.AccountSettings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.strive.maway.maway.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettings extends Fragment {
    LinearLayout editAccount ;
    String email;
    String password;



    View mView;



    public UserSettings() {
        // Required empty public constructor


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getContext(), email+"   "+password+"", Toast.LENGTH_SHORT).show();
        editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAccount e = new EditAccount();
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                bundle.putString("password",password);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                e.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.content, e).commit();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_user_settings, container, false);
        editAccount = mView.findViewById(R.id.EditAccount);
        email = getArguments().getString("email");
        password = getArguments().getString("password");





        return mView;
        // Inflate the layout for this fragment

    }


}
