package dk.ipfortify.madplanen.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import dk.ipfortify.madplanen.data.model.madplanen.Recipe;

public interface iFavorite {

    void fav_update();
    LiveData<List<Recipe>> fav_getAll();
    void fav_addToFavoriteList(Recipe recipe);
    void fav_addToShoppingList(Recipe recipe);
}
