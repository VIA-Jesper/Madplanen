package dk.ipfortify.madplanen.ui.favorite;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dk.ipfortify.madplanen.data.repository.iFavorite;
import dk.ipfortify.madplanen.data.repository.RecipeRepository;
import dk.ipfortify.madplanen.data.model.madplanen.Recipe;

public class FavoriteRecipesViewModel extends AndroidViewModel {

    private final iFavorite repository;
    private final LiveData<List<Recipe>> recipes;


    public FavoriteRecipesViewModel(Application app) {
        super(app);
        repository = RecipeRepository.getInstance(app);
        recipes = repository.fav_getAll();
    }


    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }


    public void update() {
        repository.fav_update();
    }

    public void updateFavorite(Recipe recipe) {
        recipe.isFavorite = !recipe.isFavorite;
        repository.fav_addToFavoriteList(recipe);

    }

    public void updateShoppingList(Recipe recipe) {
        recipe.isShoppingList = !recipe.isShoppingList;
        repository.fav_addToShoppingList(recipe);
    }
}