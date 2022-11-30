package com.vkevvinn.couchcast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.vkevvinn.couchcast.backend.GetShowWrapper;

import info.movito.themoviedbapi.model.tv.TvSeries;

public class ShowViewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView username_display;
    private TextView realname_display;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            Integer showId = extras.getInt("showId");
            GetShowAsyncTask getShowAsyncTask = new GetShowAsyncTask();
            getShowAsyncTask.execute(showId);
        }

        else {
            Toast.makeText(ShowViewFragment.this.getContext(), "Something went wrong!  Please try again.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private class GetShowAsyncTask extends AsyncTask<Integer, Void, TvSeries> {

        @Override
        protected TvSeries doInBackground(Integer... showId) {
            GetShowWrapper getShowWrapper = new GetShowWrapper();
            return getShowWrapper.getTvSeriesById(showId[0]);
        }

        @Override
        protected void onPostExecute(TvSeries tvSeries) {
            try {
//                    Set UI fields here
                    return;
            }

            catch (Exception e) {
                Toast.makeText(ShowViewFragment.this.getContext(), "Sorry, no shows found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}