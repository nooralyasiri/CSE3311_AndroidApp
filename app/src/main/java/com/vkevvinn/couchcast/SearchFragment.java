package com.vkevvinn.couchcast;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.Utils;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements ShowlistRecyclerViewAdapter.ItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView search_rv;
    private ShowlistRecyclerViewAdapter adapter;
    String apiKey = "4bb376189becc0b82f734fd11af958a0";
    private ArrayList<Integer> showIds = new ArrayList<>();
    private ArrayList<String> showNames = new ArrayList<>();
    private ArrayList<String> posterUrls = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        search_rv = view.findViewById(R.id.search_rv);
        search_rv.setHasFixedSize(true);
        LinearLayoutManager trendingLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        search_rv.setLayoutManager(trendingLayoutManager);
        adapter = new ShowlistRecyclerViewAdapter(getActivity(), showIds, showNames, posterUrls);
        adapter.setClickListener(this);
        search_rv.setAdapter(adapter);


        Button showInfoButton = view.findViewById(R.id.search_button);
        EditText searchBox = view.findViewById(R.id.searchView);

        showInfoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (searchBox.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "Error: search field is blank!",Toast.LENGTH_SHORT).show();
                }
                else {
                    GetShowFromSearchTask getShowFromSearchTask = new GetShowFromSearchTask();
                    getShowFromSearchTask.execute(searchBox.getText().toString());
                }
//                startActivity(new Intent(getActivity(), ShowInfo.class));
            }
        });
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

    private class GetShowFromSearchTask extends AsyncTask<String, Void, List<TvSeries>> {

        @Override
        protected List<TvSeries> doInBackground(String... query) {
            TmdbApi tmdbApi = new TmdbApi(apiKey);
            List<TvSeries> showQuery = tmdbApi.getSearch().searchTv(query[0],"en",0).getResults();
            return showQuery;
        }

        @Override
        protected void onPostExecute(List<TvSeries> showQuery) {
            try {
                showNames.clear();
                showIds.clear();
                posterUrls.clear();
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