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

/**
 * Created by Walid on 09/05/2018.
 */

public class  RequestsItemAdapter  extends RecyclerView.Adapter<RequestsItemAdapter.MyViewBolder> {
    List<LocationInformations> items;
    Context context;

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
        public MyViewBolder(final View itemView){
            super(itemView);


            vicinity= (TextView) itemView.findViewById(R.id.vicinity);


            placename=(TextView) itemView.findViewById(R.id.placename);

        }

        public  void display (LocationInformations item , Context context){
            this.itemerino =item;
            this.context=context;


            vicinity.setText(item.getVicinity());
            placename.setText(item.getPlaceName());




        }



    }

}