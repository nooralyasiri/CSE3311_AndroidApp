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

import java.util.List;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> {

    private List<Integer> showIds;
    private List<String> showNames;
    private List<String> showGenre;
    private List<String> showSeasons;
    private List<String> posterUrls;
    private LayoutInflater inflater;
    private FavoritesListAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    FavoritesListAdapter(Context context, List<Integer> showIds, List<String> showNames, List<String> showGenre, List<String> showSeasons, List<String> posterUrls) {
        this.inflater = LayoutInflater.from(context);
        this.posterUrls = posterUrls;
        this.showIds = showIds;
        this.showNames = showNames;
        this.showGenre = showGenre;
        this.showSeasons = showSeasons;

    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public FavoritesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_favorites, parent, false);
        return new FavoritesListAdapter.ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull FavoritesListAdapter.ViewHolder holder, int position) {
        String showName = showNames.get(position);
        holder.myTextView.setText(showName);
        String showGenre = this.showGenre.get(position);
        holder.myTextView.setText(showGenre);
        String showSeasons = this.showSeasons.get(position);
        holder.myTextView.setText(showSeasons);
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
            myTextView = itemView.findViewById(R.id.showcardgenre);
            myTextView = itemView.findViewById(R.id.showcardseasons);
            myImageView = itemView.findViewById(R.id.showcardimage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // convenience method for getting data at click position
    public String getName(int i) {
        return showNames.get(i);
    }
    public String getGenre(int i) {
        return showGenre.get(i);
    }
    public String getSeasons(int i) {
        return showSeasons.get(i);
    }
    public Integer getId(int i) {
        return showIds.get(i);
    }

    // allows clicks events to be caught
    public void setClickListener(FavoritesListAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
