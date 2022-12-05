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
            Map<String, Map<String, String>> currentFavorites = (Map<String, Map<String, String>>)documentSnapshot.get("favorites");

            if (currentFavorites == null) {
                currentFavorites = new HashMap<String, Map<String,String>>();
            }

            if (!currentFavorites.containsKey(showString)) {
                Map<String, String> favoriteMap = new HashMap<>();
                favoriteMap.put(favoritedKey, "true");
                currentFavorites.put(showString, favoriteMap);
            }

            else {
                if (currentFavorites.get(showString).containsKey(favoritedKey)) {
                    currentFavorites.get(showString).replace(favoritedKey, "true");
                }

                else {
                    currentFavorites.get(showString).putIfAbsent(favoritedKey, "true");
                }
            }

            documentSnapshot.getReference().update(favoritesKey, currentFavorites);
        });
    }

    public Task<DocumentSnapshot> removeFavorite(String username, Integer showId) {
        String showString = showId.toString();

        return getUserInfo(username).addOnSuccessListener(documentSnapshot -> {
            Map<String, Map<String, String>> currentFavorites = (Map<String, Map<String, String>>) documentSnapshot.get("favorites");
            if (currentFavorites != null) {
                currentFavorites.remove(showString);
                documentSnapshot.getReference().update(favoritesKey, currentFavorites);
            }
        });
    }

    public Task<DocumentSnapshot> addRating(String username, Integer showId, float ratingVal) {
        String showString = showId.toString();

        return getUserInfo(username).addOnSuccessListener(documentSnapshot -> {
            Map<String, Map<String, String>> currentFavorites = (Map<String, Map<String, String>>) documentSnapshot.get("favorites");

            if (currentFavorites == null) {
                currentFavorites = new HashMap<String, Map<String,String>>();
            }

            if (currentFavorites.containsKey(showString)) {
                if (currentFavorites.get(showString).containsKey(ratingValKey)) {
                    currentFavorites.get(showString).replace(ratingValKey, Float.toString(ratingVal));
                }

                else {
                    currentFavorites.get(showString).putIfAbsent(ratingValKey, Float.toString(ratingVal));
                }

            }

            else {
                Map<String, Map<String, String>> newFavoritesMap = new HashMap<>();
                Map<String, String> ratingMap = new HashMap<>();
                ratingMap.put(ratingValKey, Float.toString(ratingVal));
                newFavoritesMap.put(showString, ratingMap);
                currentFavorites = newFavoritesMap;
            }

            documentSnapshot.getReference().update(favoritesKey, currentFavorites);
        });
    }

    public Task<DocumentSnapshot> addReview(String username, Integer showId, String reviewVal) {
        String showString = showId.toString();

        return getUserInfo(username).addOnSuccessListener(documentSnapshot -> {
            Map<String, Map<String, String>> currentFavorites = (Map<String, Map<String, String>>) documentSnapshot.get("favorites");

            if (currentFavorites == null) {
                currentFavorites = new HashMap<String, Map<String,String>>();
            }

            if (currentFavorites.containsKey(showString)) {
                if (currentFavorites.get(showString).containsKey(reviewValKey)) {
                    currentFavorites.get(showString).replace(reviewValKey, reviewVal);
                }

                else {
                    currentFavorites.get(showString).putIfAbsent(reviewValKey, reviewVal);
                }
            }

            else {
                Map<String, Map<String, String>> newFavoritesMap = new HashMap<>();
                Map<String, String> reviewMap = new HashMap<>();
                reviewMap.put(reviewValKey, reviewVal);
                newFavoritesMap.put(showString, reviewMap);
                currentFavorites = newFavoritesMap;
            }

            documentSnapshot.getReference().update(favoritesKey, currentFavorites);
        });
    }

    public Map<String, Map<String, String>> getFavorites(DocumentSnapshot documentSnapshot) {
        return (Map<String, Map<String, String>>) documentSnapshot.get(favoritesKey);
    }

}