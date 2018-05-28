package com.strive.maway.maway.Admin;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
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

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Walid on 09/05/2018.
 */

public class  RequestsItemAdapter  extends RecyclerView.Adapter<RequestsItemAdapter.MyViewBolder> {
    List<LocationInformations> items;
    Context context;
    DecimalFormat df = new DecimalFormat("#.##");


    public RequestsItemAdapter(List<LocationInformations> items, Context context) {
        this.items = items;
        this.context = context;
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

        public  void display (LocationInformations item , final Context context){
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

                }
            });




        }



    }

}