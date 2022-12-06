package com.vkevvinn.couchcast;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.vkevvinn.couchcast.backend.FirestoreWrapper;
import com.vkevvinn.couchcast.backend.GetShowWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.Utils;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ShowlistRecyclerViewAdapter.ItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView trending_rv;
    private ShowlistRecyclerViewAdapter adapter;
    String apiKey = "4bb376189becc0b82f734fd11af958a0";
    private List<TvSeries> trendingShows;
    private ArrayList<Integer> showIds = new ArrayList<>();
    private ArrayList<String> showNames = new ArrayList<>();
    private ArrayList<String> posterUrls = new ArrayList<>();
    private ArrayList<Integer> favoritesIds = new ArrayList<>();
    private String recentlyAddedPosterUrl;
    private int recentlyAddedShowId;
    ImageView smallercard_recent;
    TextView showTitle, synopsis, genre, seasonCount, episodeCount;
    boolean hasRecentShow = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        GetTrendingShows getTrendingShows = new GetTrendingShows();
        getTrendingShows.execute();

        smallercard_recent = view.findViewById(R.id.smallercard_recent);
        synopsis = view.findViewById(R.id.synopsis);
        synopsis.setMovementMethod(new ScrollingMovementMethod());
        genre = view.findViewById(R.id.genre);
        seasonCount = view.findViewById(R.id.seasonCount);
        episodeCount = view.findViewById(R.id.episodeCount);
        showTitle = view.findViewById(R.id.showTitle);

        trending_rv = view.findViewById(R.id.trending_rv);
        trending_rv.setHasFixedSize(true);
        LinearLayoutManager trendingLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        trending_rv.setLayoutManager(trendingLayoutManager);
        adapter = new ShowlistRecyclerViewAdapter(getActivity(), showIds, showNames, posterUrls);
        adapter.setClickListener(this);
        trending_rv.setAdapter(adapter);

        String userName = ((BotNavActivity) getActivity()).getUserName();
        FirestoreWrapper firestoreWrapper = new FirestoreWrapper();
        firestoreWrapper.getUserInfo(userName).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> favoritesList = firestoreWrapper.getFavorites(task.getResult());
                    favoritesList.forEach(show -> {
                        favoritesIds.add(Integer.parseInt(show));
                    });
                    if ( !favoritesIds.isEmpty() ) {
                        hasRecentShow = true;
                        smallercard_recent.setClickable(true);
                        PopulateRecentlyAdded populateRecentlyAdded = new PopulateRecentlyAdded();
                        populateRecentlyAdded.execute(favoritesIds.get(ThreadLocalRandom.current().nextInt(0,favoritesIds.size())));
                    }
                }
            }
        });

        smallercard_recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("showId",recentlyAddedShowId);
                ShowViewFragment showViewFragment = new ShowViewFragment();
                showViewFragment.setArguments(bundle);
                ((BotNavActivity) getActivity()).replaceFragment(showViewFragment);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("showId",adapter.getId(position));
        ShowViewFragment showViewFragment = new ShowViewFragment();
        showViewFragment.setArguments(bundle);
        ((BotNavActivity) getActivity()).replaceFragment(showViewFragment);
//        Toast.makeText(getActivity(), "You clicked " + adapter.getName(position) + " (Show ID " + adapter.getId(position) + ") on item position " + position, Toast.LENGTH_SHORT).show();
    }

    private class GetTrendingShows extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            TmdbApi tmdbApi = new TmdbApi(apiKey);
            List<TvSeries> showQuery = tmdbApi.getTvSeries().getPopular("en-US",0).getResults();
            try {
                trendingShows = showQuery;
                for(TvSeries tvSeries : showQuery) {
                    showNames.add(tvSeries.getName());
                    showIds.add(tvSeries.getId());
                    GetShowWrapper getShowWrapper = new GetShowWrapper();
                    String posterUrl = getShowWrapper.getPosterUrl(tvSeries.getId());
                    posterUrls.add(posterUrl);
                }
            }

            catch (Exception e) {
                Toast.makeText(getActivity(), "Sorry, no popular shows found!", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String unusedString) {
            adapter.notifyDataSetChanged();
        }
    }

    private class PopulateRecentlyAdded extends AsyncTask<Integer, Void, TvSeries> {

        @Override
        protected TvSeries doInBackground(Integer... showId) {
            GetShowWrapper getShowWrapper = new GetShowWrapper();
            recentlyAddedPosterUrl = getShowWrapper.getPosterUrl(showId[0]);
            return getShowWrapper.getTvSeriesById(showId[0]);
        }

        @Override
        protected void onPostExecute(TvSeries tvSeries) {
            recentlyAddedShowId = tvSeries.getId();
            showTitle.setText(tvSeries.getName());
            genre.setText(tvSeries.getGenres().get(0).getName());
            if (tvSeries.getNumberOfSeasons() == 1) {
                seasonCount.setText(tvSeries.getNumberOfSeasons() + " Season, ");
            }
            else {
                seasonCount.setText(tvSeries.getNumberOfSeasons() + " Seasons, ");
            }
            if (tvSeries.getNumberOfEpisodes() == 1) {
                episodeCount.setText(tvSeries.getNumberOfEpisodes() + " Episode");
            }
            else {
                episodeCount.setText(tvSeries.getNumberOfEpisodes() + " Episodes");
            }
            synopsis.setText(tvSeries.getOverview());
            Log.e("imageUrl: ", recentlyAddedPosterUrl);
            try {
                Picasso.get().load(recentlyAddedPosterUrl).error(R.mipmap.ic_launcher).into(smallercard_recent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}