package dk.ipfortify.madplanen.ui.mealplan;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dk.ipfortify.madplanen.data.repository.iFavorite;
import dk.ipfortify.madplanen.data.repository.iMealPlan;
import dk.ipfortify.madplanen.data.repository.RecipeRepository;
import dk.ipfortify.madplanen.data.model.madplanen.Recipe;


public class MealPlanViewModel extends AndroidViewModel {


    private final iMealPlan mealPlan_repository;
    private final iFavorite fav_repository;

    public MealPlanViewModel(Application app) {
        super(app);
        mealPlan_repository = RecipeRepository.getInstance(app);
        fav_repository = RecipeRepository.getInstance(app);
    }


    public LiveData<List<Recipe>> getRecipes(String mealTypeName) {
        return mealPlan_repository.mealPlan_get(mealTypeName);
    }

    public void updateRecipes(String mealTypeName) {
        mealPlan_repository.mealPlan_update(mealTypeName);
    }


    public void updateFavorite(Recipe recipe) {
        recipe.isFavorite = !recipe.isFavorite;
        fav_repository.fav_addToFavoriteList(recipe);

    }

    public void updateShoppingList(Recipe recipe) {
        recipe.isShoppingList = !recipe.isShoppingList;
        fav_repository.fav_addToShoppingList(recipe);
    }
}
