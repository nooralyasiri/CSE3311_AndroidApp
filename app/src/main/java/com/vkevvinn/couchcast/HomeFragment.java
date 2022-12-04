package com.vkevvinn.couchcast;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

        trending_rv = view.findViewById(R.id.trending_rv);
        trending_rv.setHasFixedSize(true);
        LinearLayoutManager trendingLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        trending_rv.setLayoutManager(trendingLayoutManager);
        adapter = new ShowlistRecyclerViewAdapter(getActivity(), showIds, showNames, posterUrls);
        adapter.setClickListener(this);
        trending_rv.setAdapter(adapter);

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

    private class GetTrendingShows extends AsyncTask<Void, Void, List<TvSeries>> {

        @Override
        protected List<TvSeries> doInBackground(Void... voids) {
            TmdbApi tmdbApi = new TmdbApi(apiKey);
            List<TvSeries> showQuery = tmdbApi.getTvSeries().getPopular("en-US",0).getResults();
            return showQuery;
        }

        @Override
        protected void onPostExecute(List<TvSeries> showQuery) {
            try {
                trendingShows = showQuery;
                for(TvSeries tvSeries : showQuery) {

                    showNames.add(tvSeries.getName());
                    showIds.add(tvSeries.getId());
                    String posterPath = tvSeries.getPosterPath();
                    GetPosterImage getPosterImage = new GetPosterImage();
                    getPosterImage.execute(posterPath);
                }
            }

            catch (Exception e) {
                Toast.makeText(getActivity(), "Sorry, no popular shows found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetPosterImage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... posterPaths) {
            TmdbApi tmdbApi = new TmdbApi(apiKey);
            Utils utils = new Utils();
            try{
                return Utils.createImageUrl(tmdbApi, posterPaths[0], "w500").toString();
            } catch (Exception e) {
                return "https://sdbeerfestival.com/wp-content/uploads/2018/10/placeholder.jpg";
            }
        }

        @Override
        protected void onPostExecute(String imageUrl) {
            Log.e("imageUrl: ", imageUrl.replaceAll("http", "https"));
            posterUrls.add(imageUrl.replaceAll("http://", "https://"));
            adapter.notifyDataSetChanged();
        }
    }
}