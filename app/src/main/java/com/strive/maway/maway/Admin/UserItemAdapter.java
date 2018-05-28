package com.strive.maway.maway.Admin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strive.maway.maway.LocationInformations;
import com.strive.maway.maway.R;

import java.util.List;

public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.MyViewBolder> {
    List<LocationInformations> items;
    Context context;

    public UserItemAdapter(List<LocationInformations> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public UserItemAdapter.MyViewBolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new UserItemAdapter.MyViewBolder(view);
    }



    @Override
    public void onBindViewHolder(UserItemAdapter.MyViewBolder holder, int position) {
        LocationInformations item = items.get(position);
        holder.display(item,context);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public  class MyViewBolder extends RecyclerView.ViewHolder {
        LocationInformations itemerino;
        Context context;
        TextView objet,solde,date,time;
        public MyViewBolder(final View itemView){
            super(itemView);

            objet = (TextView) itemView.findViewById(R.id.objectif);

            date=(TextView) itemView.findViewById(R.id.date);
            time=(TextView) itemView.findViewById(R.id.time);

        }

        public  void display (LocationInformations item , Context context){
            this.itemerino =item;
            this.context=context;
            objet.setText("test");

            date.setText("test");
            time.setText("test");




        }



    }

}