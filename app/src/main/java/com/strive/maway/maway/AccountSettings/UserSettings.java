package com.strive.maway.maway.AccountSettings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.strive.maway.maway.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettings extends Fragment {
    LinearLayout editAccount,deleteAccount ;
    String email;
    String password;



    View mView;



    public UserSettings() {
        // Required empty public constructor


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showSnackbar();
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
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteAccount deleteAccount = new DeleteAccount();
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("password",password);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                deleteAccount.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.content,deleteAccount).commit();

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_user_settings, container, false);
        editAccount = mView.findViewById(R.id.EditAccount);
        deleteAccount = mView.findViewById(R.id.delete_accountBtn);
        email = getArguments().getString("email");
        password = getArguments().getString("password");





        return mView;
        // Inflate the layout for this fragment

    }
    private void showSnackbar(){

        final Snackbar snackbar = Snackbar.make(mView, "Slide your finger from the left end to the right to display the menu", Snackbar.LENGTH_INDEFINITE).setDuration(1000*15);

        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(getContext(),R.color.White));
        textView.setTextSize(14);
        textView.setTypeface(ResourcesCompat.getFont(getContext(),R.font.raleway_semibold));
        textView.setMaxLines(8);
        snackbarView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.bluBtn));

        snackbar .setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }).setActionTextColor(ContextCompat.getColor(getContext(),R.color.White));
        snackbar.show();
    }

}
