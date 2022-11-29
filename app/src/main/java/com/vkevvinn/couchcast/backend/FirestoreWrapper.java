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

    public Task<Void> addFavorite(DocumentSnapshot documentSnapshot, Integer showId) {
        if( documentSnapshot.getData().containsKey("favorites")) {
            List<Integer> currentFavorites = (List<Integer>)documentSnapshot.get("favorites");
            if( !currentFavorites.contains(showId) ) {
                currentFavorites.add(showId);
            }
            return documentSnapshot.getReference().update("favorites", currentFavorites);
        }

        return documentSnapshot.getReference().update("favorites", Arrays.asList(showId));
    }

    public Task<Void> removeFavorite(DocumentSnapshot documentSnapshot, Integer showId) {
        if( documentSnapshot.getData().containsKey("favorites")) {
            List<Integer> currentFavorites = (List<Integer>)documentSnapshot.get("favorites");
            if( !currentFavorites.contains(showId) ) {
                currentFavorites.remove(showId);
            }
            return documentSnapshot.getReference().update("favorites", currentFavorites);
        }

        return documentSnapshot.getReference().update("favorites", Arrays.asList());
    }

    public List<Integer> getFavorites(DocumentSnapshot documentSnapshot) {
        if( documentSnapshot.getData().containsKey("favorites")) {
            return (List<Integer>)documentSnapshot.get("favorites");
        }

        return Arrays.asList();
    }

}
