package com.example.eldhopjames.recyclercardviewsample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * needs Apdater and View Holder
 *      View Holder : used to caches the data and no need to write findViewById for each items
 *      Adapter : populates the item  and data about it , data comes from n/w or from DB
 * We create an interface to pass the values of the item clicked into activity
 *
 */
public class Adapter  extends RecyclerView.Adapter<Adapter.ViewHolder> { //Adapter

    //ModelClass and its constructor
    List<ModelClass> listItems; // List
    Context context;
    private OnItemClickListener mListener; // Listener for the OnItemClickListener interface

    public Adapter(List<ModelClass> listItems, Context context) { // constructor
        this.listItems = listItems;
        this.context = context;
    }

    /**
     * interface will forward our click from adapter to our main activity
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {// this method calls when ever our view method is created , ie; the instance of ViewHolder class is created
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false); /**list_item-> is the Card view which holds the data in the recycler view*/
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//populate the data into the list_item (View Holder), as we scroll
        /**Binding data to the list_item*/
        ModelClass listitem = listItems.get(position);
        holder.headTv.setText(listitem.getHead());
        holder.descTv.setText(listitem.getDesc());
    }

    @Override
    public int getItemCount() { // return the size of the list view , NOTE : this must be a fast process
        return listItems.size();
    }

    //View Holder class caches these references that gonna modify in the adapter
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**Define viewHolder views (list_item) here*/
        public TextView headTv;
        public TextView descTv;

        //create a constructor with itemView as a params
        public ViewHolder(View itemView) { // with the help of "itemView" we ge the views from xml
            super(itemView);
            /**bind views*/
            headTv = itemView.findViewById(R.id.heading);
            descTv = itemView.findViewById(R.id.description);

            /**Assigning on click listener on the item*/
            itemView.setOnClickListener(new View.OnClickListener() { // we can handle the click as like we do in normal
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition(); // Get the index of the view holder
                        if (position != RecyclerView.NO_POSITION) { // Makes sure this position is still valid
                            mListener.onItemClick(position); // we catch the click on the item view then pass it over the interface and then to our activity
                        }
                    }

                }
            });
        }
    }
}
