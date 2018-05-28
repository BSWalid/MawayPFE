package com.strive.maway.maway.Admin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.strive.maway.maway.LocationInformations;
import com.strive.maway.maway.R;
import com.strive.maway.maway.RequestDeleteDialog;
import com.strive.maway.maway.addLocationAdminDialog;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Walid on 09/05/2018.
 */

public class  RequestsItemAdapter  extends RecyclerView.Adapter<RequestsItemAdapter.MyViewBolder> {
    List<LocationInformations> items;
    Context context;
    FragmentManager fm ;
    DecimalFormat df = new DecimalFormat("#.##");


    public RequestsItemAdapter(List<LocationInformations> items, Context context,FragmentManager fm) {
        this.items = items;
        this.context = context;
        this.fm = fm;
    }

    @Override
    public RequestsItemAdapter.MyViewBolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_requests_item,parent,false);
        return new RequestsItemAdapter.MyViewBolder(view);
    }



    @Override
    public void onBindViewHolder(RequestsItemAdapter.MyViewBolder holder, int position) {
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
        TextView lat,lang,placename,vicinity;
        ImageView itemclick;
        public MyViewBolder(final View itemView){
            super(itemView);
            lat = (TextView)itemView.findViewById(R.id.lat) ;
            lang = ( TextView) itemView.findViewById(R.id.lang);
            vicinity= (TextView) itemView.findViewById(R.id.vicinity);
            itemclick=(ImageView) itemView.findViewById(R.id.itemclick);


            placename=(TextView) itemView.findViewById(R.id.placename);

        }

        public  void display (final LocationInformations item , final Context context){
            this.itemerino =item;
            this.context=context;

            lat.setText("Lat: "+item.getLatitude().substring(0, Math.min(item.getLatitude().length(), 5)));
            lang.setText("Lang: "+item.getLongitude().substring(0, Math.min(item.getLongitude().length(), 5)));
            vicinity.setText(item.getVicinity());
            placename.setText(item.getPlaceName());
            itemclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //HERE the DG should be created

                    Bundle args = new Bundle();

                    args.putString("latitude",item.getLatitude());
                    args.putString("longitude",item.getLongitude());
                    args.putString("placename",item.getPlaceName());
                    args.putString("placetype",item.getType());
                    args.putString("vicinity",item.getVicinity());
                    args.putString("RequestID",item.getRequestID());


                    addLocationAdminDialog addDialog= new addLocationAdminDialog();
                    addDialog.setArguments(args);



                    addDialog.show(fm,"addLocationAdminDialog");


                }
            });




        }



    }
    public void openDialog(){


    }

}