package com.vkevvinn.couchcast;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vkevvinn.couchcast.backend.FirestoreWrapper;
import com.vkevvinn.couchcast.backend.GetShowWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.Utils;
import info.movito.themoviedbapi.model.tv.TvSeries;

import java.io.File;

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
    String apiKey = "";
    private ArrayList<Integer> showIds = new ArrayList<>();
    private ArrayList<String> showNames = new ArrayList<>();
    private ArrayList<String> posterUrls = new ArrayList<>();

    private TextView username_display;
    private TextView realname_display;

    private ImageView profilePic_display;
    private FloatingActionButton changePic;

    private FirestoreWrapper firestoreWrapper = new FirestoreWrapper();

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

        //variable declarations to change picture
        profilePic_display = view.findViewById(R.id.profilePic);
        changePic = view.findViewById(R.id.changePic);

        //on click listener that actually does the change when button is pressed
        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ProfileFragment.this)
                        .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        String userName = ((BotNavActivity) getActivity()).getUserName();
        FirestoreWrapper firestoreWrapper = new FirestoreWrapper();
        firestoreWrapper.getUserInfo(userName).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> favoritesList = firestoreWrapper.getFavorites(task.getResult());
                    favoritesList.forEach(show -> {
                        showIds.add(Integer.parseInt(show));
                    });
                    PopulateFavoritesList populateFavoritesList = new PopulateFavoritesList();
                    populateFavoritesList.execute(showIds);
                }
            }
        });

        firestoreWrapper.getUserInfo(userName).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if( task.getResult().exists() ) {
                        DocumentSnapshot docSnapshot = task.getResult();
                        if( docSnapshot.contains("profilePicUri") && !docSnapshot.get("profilePicUri").toString().isEmpty() ) {
                            Uri imageUri = Uri.parse(docSnapshot.get("profilePicUri").toString());
                            File file = new File(imageUri.getPath());
                            if( file.exists() ) {
                                profilePic_display.setImageURI(Uri.parse(docSnapshot.get("profilePicUri").toString()));
                            }
                        }
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

    private class PopulateFavoritesList extends AsyncTask<List<Integer>, Void, String> {

        @Override
        protected String doInBackground(List<Integer>... showIds) {
            GetShowWrapper getShowWrapper = new GetShowWrapper();
            try {
                for (int showId : showIds[0]) {
                    TvSeries tvSeries = getShowWrapper.getTvSeriesById(showId);
                    showNames.add(tvSeries.getName());
                    String posterUrl = getShowWrapper.getPosterUrl(tvSeries.getId());
                    posterUrls.add(posterUrl);
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error populating favorites!", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String unusedString) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        profilePic_display.setImageURI(uri);

        String userName = ((BotNavActivity) getActivity()).getUserName();
        firestoreWrapper.getUserInfo(userName).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if( task.getResult().exists() ) {
                        DocumentSnapshot docSnapshot = task.getResult();
                        firestoreWrapper.addProfilePic(docSnapshot, uri.toString()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            }
                        });
                    }
                }
            }
        });

    }
}