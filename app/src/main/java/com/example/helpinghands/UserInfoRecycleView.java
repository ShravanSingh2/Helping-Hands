package com.example.helpinghands;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserInfoRecycleView extends RecyclerView.Adapter<UserInfoRecycleView.ViewHolder> {

    private List<String> name_list;
    private List<String> userNeed_list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    UserInfoRecycleView(Context context, List<String> nameList, List<String> userNeedList) {
        this.mInflater = LayoutInflater.from(context);
        this.name_list = nameList;
        this.userNeed_list = userNeedList;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.user_info_recycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = name_list.get(position);
        String userneed = userNeed_list.get(position);
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
        TextView name_display;
        TextView userNeedDisplay;

        ViewHolder(View itemView) {
            super(itemView);
            name_display = itemView.findViewById(R.id.ID3);
            userNeedDisplay = itemView.findViewById(R.id.ID2);
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