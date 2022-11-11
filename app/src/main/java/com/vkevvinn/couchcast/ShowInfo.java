package com.vkevvinn.couchcast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//tmdb imports needed for methods
import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.model.tv.Network;
import info.movito.themoviedbapi.model.tv.TvSeries;


public class ShowInfo extends AppCompatActivity {

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
                QueryAsyncTask queryAsyncTask = new QueryAsyncTask();
                String toQuery = showQueryText.getText().toString();
                queryAsyncTask.execute(toQuery);
            }
        });

//        try {
//            // showInfoExecutor.execute(1396);
//            queryAsyncTask.execute("Strange");
//        }
//        catch (Exception e) {
//            System.out.println(e);
//        }
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
        }
    }

    private class QueryAsyncTask extends AsyncTask<String, Void, List<TvSeries>> {

        String platforms = "Streaming on: ";

        @Override
        protected List<TvSeries> doInBackground(String... query) {
            TmdbApi tmdbApi = new TmdbApi(apiKey);
            List<TvSeries> showQuery = tmdbApi.getSearch().searchTv(query[0],"en",0).getResults();
            return showQuery;
        }

        @Override
        protected void onPostExecute(List<TvSeries> showQuery) {
            try {
                Integer showId = showQuery.get(0).getId();
                ShowInfoExecutor showInfoExecutor = new ShowInfoExecutor();
                showInfoExecutor.execute(showId);
//                for( TvSeries tvSeries : showQuery) {
//                    Integer showId = tvSeries.getId();
//                    ShowInfoExecutor showInfoExecutor = new ShowInfoExecutor();
//                    showInfoExecutor.execute(showId);
//                }
            }

            catch (Exception e) {
                Toast.makeText(ShowInfo.this, "Sorry, no shows found!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
