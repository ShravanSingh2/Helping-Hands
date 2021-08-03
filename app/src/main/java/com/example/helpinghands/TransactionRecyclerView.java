package com.example.helpinghands;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionRecyclerView extends RecyclerView.Adapter<TransactionRecyclerView.ViewHolder> {

    private List<String> donatee_list;
    private List<String> item_list;
    private List<String> quantity_list;
    private List<String> date_list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    TransactionRecyclerView(Context context,  List<String> donateeList, List<String> itemList,List<String> quantityList,List<String> dateList) {
        this.mInflater = LayoutInflater.from(context);
        this.donatee_list = donateeList;
        this.item_list = itemList;
        this.quantity_list = quantityList;
        this.date_list = dateList;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.transaction_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = donatee_list.get(position);
        String item = item_list.get(position);
        String quantity = quantity_list.get(position);
        String date = date_list.get(position);
        holder.name_display.setText(name);
        holder.item_display.setText(item);
        holder.quantity_display.setText(quantity);
        holder.date_display.setText(date);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return quantity_list.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_display;
        TextView item_display;
        TextView quantity_display;
        TextView date_display;

        ViewHolder(View itemView) {
            super(itemView);

            name_display = itemView.findViewById(R.id.ID3);
            item_display = itemView.findViewById(R.id.ID4);
            quantity_display = itemView.findViewById(R.id.ID5);
            date_display = itemView.findViewById(R.id.ID2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return quantity_list.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}