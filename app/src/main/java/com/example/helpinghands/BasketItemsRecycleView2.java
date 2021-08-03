package com.example.helpinghands;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BasketItemsRecycleView2 extends RecyclerView.Adapter<BasketItemsRecycleView2.ViewHolder> {

    private List<String> id_list;
    private List<String> name_list;
    private List<String> userNeed_list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    BasketItemsRecycleView2(Context context, List<String> idList, List<String> nameList, List<String> userNeedList) {
        this.mInflater = LayoutInflater.from(context);
        this.id_list = idList;
        this.name_list = nameList;
        this.userNeed_list = userNeedList;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row3, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String id = id_list.get(position);
        String name = name_list.get(position);
        String userneed = userNeed_list.get(position);
        holder.id_display.setText(id);
        holder.name_display.setText(name);
        holder.userNeedDisplay.setText(userneed);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return name_list.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id_display;
        TextView name_display;
        TextView userNeedDisplay;

        ViewHolder(View itemView) {
            super(itemView);
             id_display = itemView.findViewById(R.id.ID);
            name_display = itemView.findViewById(R.id.NAME);
            userNeedDisplay = itemView.findViewById(R.id.USERS_NEEDED);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return name_list.get(id);
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