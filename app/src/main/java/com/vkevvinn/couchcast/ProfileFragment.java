package com.vkevvinn.couchcast;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vkevvinn.couchcast.backend.FirestoreWrapper;
import com.vkevvinn.couchcast.backend.GetShowWrapper;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.Utils;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements ShowlistRecyclerViewAdapter.ItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView favorites_rv;
    private ShowlistRecyclerViewAdapter adapter;
    String apiKey = "4bb376189becc0b82f734fd11af958a0";
    private ArrayList<Integer> showIds = new ArrayList<>();
    private ArrayList<String> showNames = new ArrayList<>();
    private ArrayList<String> posterUrls = new ArrayList<>();

    private TextView username_display;
    private TextView realname_display;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        username_display = view.findViewById(R.id.profile_username_display);
        realname_display = view.findViewById(R.id.profile_realname_display);

        String userName = ((BotNavActivity) getActivity()).getUserName();
        FirestoreWrapper firestoreWrapper = new FirestoreWrapper();
        showIds.add(71912);
        showIds.add(1396);
        showIds.add(28136);

        PopulateFavoritesList populateFavoritesList = new PopulateFavoritesList();
        populateFavoritesList.execute(showIds);

        firestoreWrapper.getUserInfo(userName).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if( task.getResult().exists() ) {
                        DocumentSnapshot docSnapshot = task.getResult();
                        String prettyUsername = "@"+docSnapshot.get("userName").toString();
                        username_display.setText(prettyUsername);

                        String prettyRealname = docSnapshot.get("firstName").toString()+" "+docSnapshot.get("lastName").toString();
                        realname_display.setText(prettyRealname);
                    }
                }
            }
        });

        favorites_rv = view.findViewById(R.id.favorites_rv);
        favorites_rv.setHasFixedSize(true);
        LinearLayoutManager favoritesLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        favorites_rv.setLayoutManager(favoritesLayoutManager);
        adapter = new ShowlistRecyclerViewAdapter(getActivity(), showIds, showNames, posterUrls);
        adapter.setClickListener(this);
        favorites_rv.setAdapter(adapter);

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

    private class PopulateFavoritesList extends AsyncTask<List<Integer>, Void, List<TvSeries>> {
        @Override
        protected List<TvSeries> doInBackground(List<Integer>... showIds) {
            GetShowWrapper getShowWrapper = new GetShowWrapper();
            List<TvSeries> favoriteShows = new ArrayList<TvSeries>();
            for (int showId : showIds[0]) {
                favoriteShows.add(getShowWrapper.getTvSeriesById(showId));
            }
            return favoriteShows;
        }

        @Override
        protected void onPostExecute(List<TvSeries> favoriteShows) {
            try {
                for(TvSeries tvSeries : favoriteShows) {

                    showNames.add(tvSeries.getName());
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