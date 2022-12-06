package com.vkevvinn.couchcast.backend;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.Utils;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class GetShowWrapper {

    String apiKey = "4bb376189becc0b82f734fd11af958a0";

    public TvSeries getTvSeriesByName(String... showNames) {
        TmdbApi tmdbApi = new TmdbApi(apiKey);
        Integer showId = tmdbApi.getSearch().searchTv(showNames[0],"en",0).getResults().get(0).getId();
        return getTvSeriesById(showId);
    }

    public TvSeries getTvSeriesById(int showId) {
        TmdbApi tmdbApi = new TmdbApi(apiKey);
        TmdbTV tv = tmdbApi.getTvSeries();
        TvSeries tvSeries = tv.getSeries(showId, "en");
        return tvSeries;
    }

    public String getPosterUrl(int showId) {
        TmdbApi tmdbApi = new TmdbApi(apiKey);
        String posterPath = tmdbApi.getTvSeries().getSeries(showId, "en").getPosterPath();
        String posterUrl;
        try {
            posterUrl = Utils.createImageUrl(tmdbApi, posterPath, "w500").toString().replaceFirst("http://", "https://");
        } catch (Exception e) {
            posterUrl = "https://sdbeerfestival.com/wp-content/uploads/2018/10/placeholder.jpg";
        }
        return posterUrl;
    }

}