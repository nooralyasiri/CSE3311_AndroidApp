package com.vkevvinn.couchcast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.vkevvinn.couchcast.backend.FirestoreWrapper;
import com.vkevvinn.couchcast.backend.GetShowWrapper;

import java.util.List;

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

    TextView showTitle, showSummary, seasons, genre, episodes;
    String apiKey = "";
    ImageView showcard;
    RatingBar ratingBar;
    Button deleteEntry, enterReview;
    EditText showReview;
    ImageButton heartButton;
    String posterUrl;

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
        showcard = view.findViewById(R.id.showcard);
        showSummary = view.findViewById(R.id.showSummary);
        showSummary.setMovementMethod(new ScrollingMovementMethod());
        ratingBar = view.findViewById(R.id.ratingBar);
        deleteEntry = view.findViewById(R.id.deleteEntry);
        showReview = view.findViewById(R.id.review);
        enterReview = view.findViewById(R.id.enterReview);
        heartButton = view.findViewById(R.id.heartButton);
        episodes = view.findViewById(R.id.episodes);

        String userName = ((BotNavActivity) getActivity()).getUserName();
        FirestoreWrapper firestoreWrapper = new FirestoreWrapper();


        int showId = getArguments().getInt("showId");
        if (showId != 0) {
            GetShowAsyncTask getShowAsyncTask = new GetShowAsyncTask();
            getShowAsyncTask.execute(showId);
        }

        else {
            Toast.makeText(ShowViewFragment.this.getContext(), "Something went wrong!  Please try again.", Toast.LENGTH_SHORT).show();
        }

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!heartButton.isSelected()) {
                    firestoreWrapper.addFavorite(userName, showId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            heartButton.setSelected(true);
                            heartButton.setImageResource(R.drawable.hhh);
                        }
                    });
                }

                else {
                    firestoreWrapper.removeFavorite(userName, showId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            heartButton.setSelected(false);
                            heartButton.setImageResource(R.drawable.hhh_off);
                        }
                    });
                }
            }
        });

        firestoreWrapper.getUserInfo(userName).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String showReviewText = firestoreWrapper.getReview(task.getResult(), showId);
                    showReview.setText(showReviewText);

                    float showRating = firestoreWrapper.getRating(task.getResult(), showId);
                    ratingBar.setRating(showRating);

                    if (firestoreWrapper.getFavorites(task.getResult()).contains(String.valueOf(showId))) {
                        heartButton.setSelected(true);
                        heartButton.setImageResource(R.drawable.hhh);
                    }

                    else {
                        heartButton.setSelected(false);
                        heartButton.setImageResource(R.drawable.hhh_off);
                    }
                }
            }
        });

        enterReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreWrapper.addReview(userName, showId, showReview.getText().toString()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ShowViewFragment.this.getContext(), "Review "+ "\""+showReview.getText().toString()+"\"" +" successfully added!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                firestoreWrapper.addRating(userName, showId, ratingBar.getRating()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ShowViewFragment.this.getContext(), "Rating successfully added!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        deleteEntry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showReview.setText("");
                ratingBar.setRating(0);
                firestoreWrapper.addReview(userName, showId, showReview.getText().toString()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ShowViewFragment.this.getContext(), "Review successfully removed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                firestoreWrapper.addRating(userName, showId, ratingBar.getRating()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ShowViewFragment.this.getContext(), "Rating successfully removed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }

    private class GetShowAsyncTask extends AsyncTask<Integer, Void, TvSeries> {
        String genreList = " ";
        @Override
        protected TvSeries doInBackground(Integer... showId) {
            GetShowWrapper getShowWrapper = new GetShowWrapper();

            TvSeries tvSeries = getShowWrapper.getTvSeriesById(showId[0]);
            posterUrl = getShowWrapper.getPosterUrl(showId[0]);
            return tvSeries;
        }

        @Override
        protected void onPostExecute(TvSeries tvSeries) {
            try {
//                    Set UI fields here
                showTitle.setText(tvSeries.getName());
                showSummary.setText(tvSeries.getOverview());
                genre.setText(tvSeries.getGenres().get(0).getName());
                if (tvSeries.getNumberOfSeasons() == 1)
                {
                    seasons.setText(tvSeries.getNumberOfSeasons() + " Season, ");
                }
                else
                {
                    seasons.setText(tvSeries.getNumberOfSeasons() + " Seasons, ");
                }
                if (tvSeries.getNumberOfEpisodes() == 1)
                {
                    episodes.setText(tvSeries.getNumberOfEpisodes() + " Episode");
                }
                else
                {
                    episodes.setText(tvSeries.getNumberOfEpisodes() + " Episodes");
                }
                Log.e("imageUrl: ", posterUrl);
                try {
                    Picasso.get().load(posterUrl).error(R.mipmap.ic_launcher).into(showcard);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            catch (Exception e) {
                Toast.makeText(ShowViewFragment.this.getContext(), "Sorry, no shows found!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}