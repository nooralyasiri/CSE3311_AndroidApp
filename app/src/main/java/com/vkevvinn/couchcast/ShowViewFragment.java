package com.vkevvinn.couchcast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.vkevvinn.couchcast.backend.GetShowWrapper;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.Utils;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class ShowViewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView username_display;
    private TextView realname_display;

    TextView showTitle, showSummary, seasons, genre;
    String apiKey = "4bb376189becc0b82f734fd11af958a0";
    ImageView showcard;
    RatingBar ratingBar;
    Button deleteEntry, enterEntry;
    EditText showReview;
    ImageButton heart;

    public ShowViewFragment() {
        // Required empty public constructor
    }

    public static ShowViewFragment newInstance(String param1, String param2) {
        ShowViewFragment fragment = new ShowViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showview, container, false);

        showTitle = view.findViewById(R.id.showTitle);
        seasons = view.findViewById(R.id.seasons);
        genre = view.findViewById(R.id.genre);
        heart = view.findViewById(R.id.heartButton);
        showcard = view.findViewById(R.id.showcard);
        showSummary = view.findViewById(R.id.showSummary);
        ratingBar = view.findViewById(R.id.ratingBar);
        showReview = view.findViewById(R.id.review);
        deleteEntry = view.findViewById(R.id.deleteEntry);
        enterEntry = view.findViewById(R.id.enterReview);


        deleteEntry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showReview.setText(String.valueOf(ratingBar.getRating()));
            }
        });

        int showId = getArguments().getInt("showId");
        if (showId != 0) {
            GetShowAsyncTask getShowAsyncTask = new GetShowAsyncTask();
            getShowAsyncTask.execute(showId);
        }

        else {
            Toast.makeText(ShowViewFragment.this.getContext(), "Something went wrong!  Please try again.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private class GetShowAsyncTask extends AsyncTask<Integer, Void, TvSeries> {
        String genreList = " ";
        @Override
        protected TvSeries doInBackground(Integer... showId) {
            GetShowWrapper getShowWrapper = new GetShowWrapper();
            return getShowWrapper.getTvSeriesById(showId[0]);
        }

        @Override
        protected void onPostExecute(TvSeries tvSeries) {
            try {
//                    Set UI fields here
                showTitle.setText(tvSeries.getName());
                showSummary.setText(tvSeries.getOverview());
                genre.setText(tvSeries.getGenres().get(0).getName());
                seasons.setText(tvSeries.getNumberOfSeasons() + " Seasons");
                GetPosterImage getPosterImage = new GetPosterImage();
                getPosterImage.execute(tvSeries.getPosterPath());
            }

            catch (Exception e) {
                Toast.makeText(ShowViewFragment.this.getContext(), "Sorry, no shows found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetPosterImage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... posterPaths) {
            TmdbApi tmdbApi = new TmdbApi(apiKey);
            try{
                return Utils.createImageUrl(tmdbApi, posterPaths[0], "w500").toString();
            } catch (Exception e) {
                return "https://sdbeerfestival.com/wp-content/uploads/2018/10/placeholder.jpg";
            }
        }

        @Override
        protected void onPostExecute(String imageUrl) {
            Log.e("imageUrl: ", imageUrl.replaceAll("http", "https"));
            try {
                Picasso.get().load(imageUrl.replaceAll("http://", "https://")).error(R.mipmap.ic_launcher).into(showcard);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}