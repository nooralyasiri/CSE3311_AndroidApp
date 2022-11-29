package com.vkevvinn.couchcast;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vkevvinn.couchcast.backend.FirestoreWrapper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

        firestoreWrapper.getUserInfo(userName).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if( task.getResult().exists() ) {
                        DocumentSnapshot docSnapshot = task.getResult();
                        if( docSnapshot.contains("profilePicUri") && !docSnapshot.get("profilePicUri").toString().isEmpty() ) {
                            profilePic_display.setImageURI(Uri.parse(docSnapshot.get("profilePicUri").toString()));
                        }
                        String prettyUsername = "@"+docSnapshot.get("userName").toString();
                        username_display.setText(prettyUsername);

                        String prettyRealname = docSnapshot.get("firstName").toString()+" "+docSnapshot.get("lastName").toString();
                        realname_display.setText(prettyRealname);
                    }
                }
            }
        });


        // Inflate the layout for this fragment
        return view;
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