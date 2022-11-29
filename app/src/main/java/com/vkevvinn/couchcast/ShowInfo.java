package com.vkevvinn.couchcast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//tmdb imports needed for methods
import com.vkevvinn.couchcast.backend.BaseActivity;
import com.vkevvinn.couchcast.backend.FirestoreWrapper;


import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.model.tv.Network;
import info.movito.themoviedbapi.model.tv.TvSeries;


public class ShowInfo extends BaseActivity {

    TextView showName, showSeasons, showStreamingProviders;
    String apiKey = "4bb376189becc0b82f734fd11af958a0";
    Button searchButton;
    EditText showQueryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

        showName = findViewById(R.id.showName);
        showSeasons = findViewById(R.id.showSeasons);
        showStreamingProviders = findViewById(R.id.showStreamingProviders);

        searchButton = findViewById(R.id.searchButton);
        showQueryText = findViewById(R.id.searchQueryText);

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                GetShowAsyncTask queryAsyncTask = new GetShowAsyncTask();
                String showName = showQueryText.getText().toString();
                queryAsyncTask.execute(showName);
            }
        });
    }

    private class GetShowAsyncTask extends AsyncTask<String, Void, TvSeries> {

        String platforms = "Streaming on: ";

        @Override
        protected TvSeries doInBackground(String... query) {
            TmdbApi tmdbApi = new TmdbApi(apiKey);
            Integer showId = tmdbApi.getSearch().searchTv(query[0],"en",0).getResults().get(0).getId();
            TmdbTV tv = tmdbApi.getTvSeries();
            TvSeries tvSeries = tv.getSeries(showId, "en");
            return tvSeries;
        }

        @Override
        protected void onPostExecute(TvSeries tvSeries) {
            try {
                showName.setText(tvSeries.getOriginalName());
                showSeasons.setText(tvSeries.getNumberOfSeasons() + " Seasons");
                for( Network network : tvSeries.getNetworks()) {
                    platforms += (network.getName() + " ");
                }
                showStreamingProviders.setText(platforms);
            }

            catch (Exception e) {
                Toast.makeText(ShowInfo.this, "Sorry, no shows found!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
