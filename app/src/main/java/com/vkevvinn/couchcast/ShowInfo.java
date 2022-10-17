package com.vkevvinn.couchcast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

//tmdb imports needed for methods
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;


public class ShowInfo extends AppCompatActivity {
    //i can't tell what kind of method to use here i think that is what is causing the problems
    //when i use just onCreate nothing changes

    //i also tried following this issue https://github.com/holgerbrandl/themoviedbapi/issues/69
    //they say to use AsyncTask but I was unsuccessful there too :(

    //i do believe that i am using the correct themoviedbapi method to get the show name tho!

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

        //creating variables
        TextView showName, showSeasons;
        String name;

        String apiKey = "4bb376189becc0b82f734fd11af958a0";

    //start having issues after trying to include TmdbApi stuff

//        TmdbTV tv = new TmdbApi(apiKey).getTvSeries();

//        showName = findViewById(R.id.showName);
//        name = tv.getSeries(1396,"en").getOriginalName();
//        showName.setText(name);

//        Log.e("SHOW NAME", name);
    }


}
