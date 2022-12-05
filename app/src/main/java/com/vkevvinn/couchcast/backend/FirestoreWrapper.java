package com.vkevvinn.couchcast.backend;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreWrapper {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    static final String favoritesKey = "favorites";
    static final String ratingValKey = "ratingVal";
    static final String reviewValKey = "reviewVal";
    static final String favoritedKey = "favorited";

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
            Map<Integer, Map<String, String>> currentFavorites = (Map<Integer, Map<String, String>>)documentSnapshot.get("favorites");

            if (currentFavorites == null) {
                currentFavorites = new HashMap<Integer, Map<String,String>>();
            }

            if (!currentFavorites.containsKey(showId)) {
                Map<String, String> favoriteMap = new HashMap<>();
                favoriteMap.put(favoritedKey, "true");
                currentFavorites.put(showId, favoriteMap);
            }

            else {
                if (currentFavorites.get(showId).containsKey(favoritedKey)) {
                    currentFavorites.get(showId).replace(favoritedKey, "true");
                }

                else {
                    currentFavorites.get(showId).putIfAbsent(favoritedKey, "true");
                }
            }

            documentSnapshot.getReference().update(favoritesKey, currentFavorites);
        });
    }

    public Task<DocumentSnapshot> removeFavorite(String username, Integer showId) {
        return getUserInfo(username).addOnSuccessListener(documentSnapshot -> {
            Map<Integer, Map<String, String>> currentFavorites = (Map<Integer, Map<String, String>>) documentSnapshot.get("favorites");
            if (currentFavorites != null) {
                currentFavorites.remove(showId);
                documentSnapshot.getReference().update(favoritesKey, currentFavorites);
            }
        });
    }

    public Task<DocumentSnapshot> addRating(String username, Integer showId, float ratingVal) {
        return getUserInfo(username).addOnSuccessListener(documentSnapshot -> {
            Map<Integer, Map<String, String>> currentFavorites = (Map<Integer, Map<String, String>>) documentSnapshot.get("favorites");

            if (currentFavorites == null) {
                currentFavorites = new HashMap<Integer, Map<String,String>>();
            }

            if (currentFavorites.containsKey(showId)) {
                if (currentFavorites.get(showId).containsKey(ratingValKey)) {
                    currentFavorites.get(showId).replace(ratingValKey, Float.toString(ratingVal));
                }

                else {
                    currentFavorites.get(showId).putIfAbsent(ratingValKey, Float.toString(ratingVal));
                }

            }

            else {
                Map<Integer, Map<String, String>> newFavoritesMap = new HashMap<>();
                Map<String, String> ratingMap = new HashMap<>();
                ratingMap.put(ratingValKey, Float.toString(ratingVal));
                newFavoritesMap.put(showId, ratingMap);
                currentFavorites = newFavoritesMap;
            }

            documentSnapshot.getReference().update(favoritesKey, currentFavorites);
        });
    }

    public Task<DocumentSnapshot> addReview(String username, Integer showId, String reviewVal) {
        return getUserInfo(username).addOnSuccessListener(documentSnapshot -> {
            Map<Integer, Map<String, String>> currentFavorites = (Map<Integer, Map<String, String>>) documentSnapshot.get("favorites");

            if (currentFavorites == null) {
                currentFavorites = new HashMap<Integer, Map<String,String>>();
            }

            if (currentFavorites.containsKey(showId)) {
                if (currentFavorites.get(showId).containsKey(reviewValKey)) {
                    currentFavorites.get(showId).replace(reviewValKey, reviewVal);
                }

                else {
                    currentFavorites.get(showId).putIfAbsent(reviewValKey, reviewVal);
                }
            }

            else {
                Map<Integer, Map<String, String>> newFavoritesMap = new HashMap<>();
                Map<String, String> reviewMap = new HashMap<>();
                reviewMap.put(reviewValKey, reviewVal);
                newFavoritesMap.put(showId, reviewMap);
                currentFavorites = newFavoritesMap;
            }

            documentSnapshot.getReference().update(favoritesKey, currentFavorites);
        });
    }

    public Map<Integer, Map<String, String>> getFavorites(DocumentSnapshot documentSnapshot) {
        return (Map<Integer, Map<String, String>>) documentSnapshot.get(favoritesKey);
    }

}