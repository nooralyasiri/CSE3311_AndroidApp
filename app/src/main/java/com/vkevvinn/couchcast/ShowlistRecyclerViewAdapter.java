package com.vkevvinn.couchcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShowlistRecyclerViewAdapter extends RecyclerView.Adapter<ShowlistRecyclerViewAdapter.ViewHolder> {

    private List<String> showNames;
    private List<String> posterUrls;
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    ShowlistRecyclerViewAdapter(Context context, List<String> showNames, List<String> posterUrls) {
        this.inflater = LayoutInflater.from(context);
        this.showNames = showNames;
        this.posterUrls = posterUrls;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_show, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String showName = showNames.get(position);
        holder.myTextView.setText(showName);
        try {
            Picasso.get().load(posterUrls.get(position)).error(R.mipmap.ic_launcher).into(holder.myImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return showNames.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView myImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.showcardtitle);
            myImageView = itemView.findViewById(R.id.showcardimage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // convenience method for getting data at click position
    public String getItem(int id) {
        return showNames.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
