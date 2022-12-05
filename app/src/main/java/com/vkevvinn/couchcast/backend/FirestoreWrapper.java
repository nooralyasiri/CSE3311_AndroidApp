package com.vkevvinn.couchcast.backend;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirestoreWrapper {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String favoritesKey = "favorites";
    public static final String ratingValKey = "ratingVal";
    public static final String reviewValKey = "reviewVal";
    public static final String favoritedKey = "favorited";

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
        String showString = showId.toString();

        return getUserInfo(username).addOnSuccessListener(documentSnapshot -> {
            documentSnapshot.getReference().update(favoritesKey + "." + showString + "." + favoritedKey, "true");
        });
    }

    public Task<DocumentSnapshot> removeFavorite(String username, Integer showId) {
        String showString = showId.toString();

        return getUserInfo(username).addOnSuccessListener(documentSnapshot -> {
            documentSnapshot.getReference().update(favoritesKey + "." + showString + "." + favoritedKey, "false");
        });
    }

    public Task<DocumentSnapshot> addRating(String username, Integer showId, float ratingVal) {
        String showString = showId.toString();

        return getUserInfo(username).addOnSuccessListener(documentSnapshot -> {
            documentSnapshot.getReference().update(favoritesKey + "." + showString + "." + ratingValKey, ratingVal);
        });
    }

    public Task<DocumentSnapshot> addReview(String username, Integer showId, String reviewVal) {
        String showString = showId.toString();

        return getUserInfo(username).addOnSuccessListener(documentSnapshot -> {
            documentSnapshot.getReference().update(favoritesKey + "." + showString + "." + reviewValKey, reviewVal);
        });
    }

    public List<String> getFavorites(DocumentSnapshot documentSnapshot) {
        List<String> favoritedList = new ArrayList<>();
        Map<String, Map<String, String>> favoritesMap = (Map<String, Map<String, String>>) documentSnapshot.get(favoritesKey);

        if (favoritesMap != null) {
            favoritesMap.keySet().forEach(show -> {
                if (favoritesMap.get(show).containsKey(favoritedKey) && favoritesMap.get(show).get(favoritedKey).contains("true")) {
                    favoritedList.add(show);
                }
            });
        }

        return favoritedList;
    }

}