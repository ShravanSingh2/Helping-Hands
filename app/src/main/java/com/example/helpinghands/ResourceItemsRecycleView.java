package com.example.helpinghands;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.widget.ANImageView;

import java.util.List;

public class ResourceItemsRecycleView extends RecyclerView.Adapter<ResourceItemsRecycleView.ViewHolder> {

    private List<String> id_list;
    private List<String> name_list;
    private List<String> location_list;
    private List<String> id_Array;
    private List<String> photo_list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    // data is passed into the constructor
    ResourceItemsRecycleView(Context context, List<String> idList, List<String> nameList, List<String> userNeedList, List<String> idArray, List<String> photoArray) {
        this.mInflater = LayoutInflater.from(context);
        this.id_list = idList;
        this.name_list = nameList;
        this.location_list = userNeedList;
        this.photo_list = photoArray;
        this.id_Array = idArray;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row2, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String id = id_list.get(position);
        String name = name_list.get(position);
        String properID = id_Array.get(position);
        String location = location_list.get(position);
        String photo = photo_list.get(position);
        if(photo.equals("default")) {
            holder.user_icon.setImageUrl("https://lamp.ms.wits.ac.za/home/s2173638/upload_images/default.png");
        }else{
            holder.user_icon.setImageUrl("https://lamp.ms.wits.ac.za/home/s2173638/upload_images/" + properID + ".jpeg");
        }
        holder.id_display.setText(id);
        holder.name_display.setText(name);
        holder.location_display.setText(location);
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
        TextView location_display;
        ANImageView user_icon;

        ViewHolder(View itemView) {
            super(itemView);
             id_display = itemView.findViewById(R.id.ID);
            name_display = itemView.findViewById(R.id.ID2);
            location_display = itemView.findViewById(R.id.ID3);
            user_icon = itemView.findViewById(R.id.userIcon);
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