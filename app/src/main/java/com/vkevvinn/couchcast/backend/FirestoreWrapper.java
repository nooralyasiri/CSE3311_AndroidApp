package com.vkevvinn.couchcast.backend;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FirestoreWrapper {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Task<Void> addNewUser(Map<String, Object> userInfo) {
        return db.collection("users")
            .document(userInfo.get("userName").toString())
            .set(userInfo);
    }

    public Task<DocumentSnapshot> getUserInfo(String username) {
        return db.collection("users")
            .document(username)
            .get();
    }

    public Task<DocumentSnapshot> addFavorite(String username, Integer showId) {
        return getUserInfo(username).addOnSuccessListener(documentSnapshot -> {
            if( documentSnapshot.getData().containsKey("favorites")) {
                List<Integer> currentFavorites = (List<Integer>)documentSnapshot.get("favorites");
                if( !currentFavorites.contains(showId) ) {
                    currentFavorites.add(showId);
                }
                documentSnapshot.getReference().update("favorites", currentFavorites);
            }

            documentSnapshot.getReference().update("favorites", Arrays.asList(showId));
        });
    }

    public Task<DocumentSnapshot> removeFavorite(String username, Integer showId) {
        return getUserInfo(username).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.getData().containsKey("favorites")) {
                List<Integer> currentFavorites = (List<Integer>) documentSnapshot.get("favorites");
                if (!currentFavorites.contains(showId)) {
                    currentFavorites.remove(showId);
                }
                documentSnapshot.getReference().update("favorites", currentFavorites);
            }

            documentSnapshot.getReference().update("favorites", Arrays.asList());
        });
    }

}