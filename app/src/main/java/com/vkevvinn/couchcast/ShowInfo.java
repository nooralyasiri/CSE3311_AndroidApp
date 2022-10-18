package com.vkevvinn.couchcast;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

//tmdb imports needed for methods
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.model.tv.Network;
import info.movito.themoviedbapi.model.tv.TvSeries;


public class ShowInfo extends AppCompatActivity {

    TextView showName, showSeasons, showStreamingProviders;
    String apiKey = "4bb376189becc0b82f734fd11af958a0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ShowInfoExecutor showInfoExecutor = new ShowInfoExecutor();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

        showName = findViewById(R.id.showName);
        showSeasons = findViewById(R.id.showSeasons);
        showStreamingProviders = findViewById(R.id.showStreamingProviders);

        try {
            showInfoExecutor.execute(1396);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private void setShowInfo(String apiKey) {
        String show;
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                TmdbApi tmdbApi = new TmdbApi(apiKey);
                TmdbTV tv = tmdbApi.getTvSeries();

                showName.setText(tv.getSeries(1396, "en").getOriginalName());
            }
        });
    }

    private class ShowInfoExecutor extends AsyncTask<Integer, String, TvSeries> {

        String platforms = "Streaming on: ";

        @Override
        protected TvSeries doInBackground(Integer... integers) {
            TmdbApi tmdbApi = new TmdbApi(apiKey);
            TmdbTV tv = tmdbApi.getTvSeries();
            return tv.getSeries(integers[0], "en");
        }

        @Override
        protected void onPostExecute(TvSeries tvSeries) {
            showName.setText(tvSeries.getOriginalName());
            showSeasons.setText(tvSeries.getNumberOfSeasons() + " Seasons");
            for( Network network : tvSeries.getNetworks()) {
                platforms += (network.getName() + " ");
            }
            showStreamingProviders.setText(platforms);
        }

        @Override
        protected void onProgressUpdate(String... strings) {
            return;
        }
    }

}
