package com.vkevvinn.couchcast;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vkevvinn.couchcast.backend.FirestoreWrapper;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowDetailsFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    TextView showTitle, seasons, genre, showSummary;
//    String apiKey = "4bb376189becc0b82f734fd11af958a0";
//    Button delete, enter;
//    EditText review;
//    RatingBar stars;
//    ImageButton heart;
//
//    public ShowDetailsFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ShowDetails.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ShowDetailsFragment newInstance(String param1, String param2) {
//        ShowDetailsFragment fragment = new ShowDetailsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_show_details, container, false);
//        showTitle = view.findViewById(R.id.showTitle);
//        genre = view.findViewById(R.id.genre);
//        seasons = view.findViewById(R.id.seasons);
//        heart = view.findViewById(R.id.heartButton);
//        showSummary = view.findViewById(R.id.showSummary);
//        stars = view.findViewById(R.id.ratingBar);
//        review = view.findViewById(R.id.review);
//        delete = view.findViewById(R.id.delete);
//        enter = view.findViewById(R.id.enterReview);
//
//        //using review as my search bar & enter as my search button just for trial purposes rn !
//
//        enter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ShowDetailsFragment.QueryAsyncTask queryAsyncTask = new ShowDetailsFragment().QueryAsyncTask();
//                String toQuery = showSummary.getText().toString();
//                queryAsyncTask.execute(toQuery);
//            }
//
//        });
//
//        // Inflate the layout for this fragment
//        return view;
//    }
//
//    private class ShowDetailsExecutor extends AsyncTask<Integer, String, TvSeries> {
//
//        String genreList = " ";
//
//        @Override
//        protected TvSeries doInBackground(Integer... integers) {
//            TmdbApi tmdbApi = new TmdbApi(apiKey);
//            TmdbTV tv = tmdbApi.getTvSeries();
//            return tv.getSeries(integers[0], "en");
//        }
//
//        @Override
//        protected void onPostExecute(TvSeries tvSeries) {
//            showTitle.setText(tvSeries.getOriginalName());
//            for(Genre genre : tvSeries.getGenres()){
//                genreList += (genre.getName()+ " ");
//            }
//            genre.setText(genreList);
//            seasons.setText(tvSeries.getNumberOfSeasons() + " Seasons");
//            showSummary.setText(tvSeries.getOverview());
//        }
//
//        @Override
//        protected void onProgressUpdate(String... strings) {
//        }
//    }
//
//    private class QueryAsyncTask extends AsyncTask<String, Void, List<TvSeries>> {
//
//        @Override
//        protected List<TvSeries> doInBackground(String... query) {
//            TmdbApi tmdbApi = new TmdbApi(apiKey);
//            List<TvSeries> showQuery = tmdbApi.getSearch().searchTv(query[0], "en", 0).getResults();
//            return showQuery;
//        }
//
//        @Override
//        protected void onPostExecute(List<TvSeries> showQuery) {
//            try {
//                Integer showId = showQuery.get(0).getId();
//                ShowDetailsFragment.ShowDetailsExecutor showInfoExecutor = new ShowDetailsFragment().ShowDetailsExecutor();
//                showInfoExecutor.execute(showId);
////                for( TvSeries tvSeries : showQuery) {
////                    Integer showId = tvSeries.getId();
////                    ShowInfoExecutor showInfoExecutor = new ShowInfoExecutor();
////                    showInfoExecutor.execute(showId);
////                }
//            } catch (Exception e) {
//                Toast.makeText(ShowDetailsFragment.this, "Sorry, no shows found!", LENGTH_SHORT).show();
//            }
//        }
//    }

}