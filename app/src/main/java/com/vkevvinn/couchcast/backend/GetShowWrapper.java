package com.vkevvinn.couchcast.backend;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
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

}