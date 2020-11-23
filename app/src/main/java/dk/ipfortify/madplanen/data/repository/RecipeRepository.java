package dk.ipfortify.madplanen.data.repository;

import android.app.Application;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import dk.ipfortify.madplanen.data.listeners.iRecipeRepositoryListener;
import dk.ipfortify.madplanen.data.repository.local.dao.RecipeLocalDataSource;
import dk.ipfortify.madplanen.data.repository.local.database.RecipeDatabase;
import dk.ipfortify.madplanen.data.model.madplanen.Recipe;
import dk.ipfortify.madplanen.data.repository.remote.api.RecipeRemoteDataSourceNemlig;

public class RecipeRepository implements iRecipeRepositoryListener, iRecipe, iMealPlan, iFavorite {

    private static RecipeRepository instance;

    private final ExecutorService mDatabaseWriteExecutor;
    private final MutableLiveData<Recipe> mRemoteRecipe;
    private final Map<String, MutableLiveData<List<Recipe>>> mRemoteRecipeMap;
    private final MutableLiveData<List<Recipe>> mLocalRecipes;

    private final RecipeLocalDataSource mRecipeLocalDataSource;
    private final RecipeRemoteDataSourceNemlig mRecipeRemoteDataSource;


    private RecipeRepository(Application application) {

        mRemoteRecipe = new MutableLiveData<>();
        mRemoteRecipeMap = new HashMap<>();
        mLocalRecipes = new MutableLiveData<>();
        mDatabaseWriteExecutor = (ExecutorService) RecipeDatabase.databaseWriteExecutor;
        mRecipeLocalDataSource = RecipeDatabase.getInstance(application).dao();
        mRecipeRemoteDataSource = new RecipeRemoteDataSourceNemlig();

    }

    public static synchronized RecipeRepository getInstance(Application application) {
        if(instance == null)
            instance = new RecipeRepository(application);
        return instance;
    }

    @VisibleForTesting
    public RecipeRepository(RecipeLocalDataSource mRecipeLocalDataSource,
                            RecipeRemoteDataSourceNemlig mRecipeRemoteDataSource,
                            ExecutorService executorService) {
        mRemoteRecipe = new MutableLiveData<>();
        mRemoteRecipeMap = new HashMap<>();
        mLocalRecipes = new MutableLiveData<>();

        this.mRecipeLocalDataSource = mRecipeLocalDataSource;
        this.mRecipeRemoteDataSource = mRecipeRemoteDataSource;

        this.mDatabaseWriteExecutor = executorService;
    }



    @Override
    public LiveData<Recipe> recipe_get() {
        return mRemoteRecipe;
    }


    @Override
    public void listener_recipe_add(Recipe recipe) {
        save(recipe);
        mRemoteRecipe.postValue(recipe);
    }

    @Override
    public void listener_recipe_add(String key, List<Recipe> recipes) {
        // list is newly pulled
        // check if they are in db and return full object if they are
        mDatabaseWriteExecutor.execute(() -> {
            for (int i = 0; i < recipes.size(); i++) {

                Recipe recipe = recipes.get(i);

                Recipe r = mRecipeLocalDataSource.getRecipeByUrlId(recipe.url);
                if (r != null) {
                    recipes.set(i, r);
                }
            }

            MutableLiveData<List<Recipe>> recipeList = mRemoteRecipeMap.get(key);
            if (recipeList != null) { // the key exists
                recipeList.postValue(recipes);
            } else { // key does not exits
                mRemoteRecipeMap.put(key, new MutableLiveData<>(recipes));
            }
        });
    }

    @Override
    public void recipe_update(String recipeUrlId) {
        mDatabaseWriteExecutor.execute(() -> {
            Recipe recipe = mRecipeLocalDataSource.getRecipeByUrlId(recipeUrlId);
            if (recipe == null) {
                mRecipeRemoteDataSource.getRecipeByUrlId(recipeUrlId, this);
            }
            mRemoteRecipe.postValue(recipe);
        });
    }

    @Override
    public LiveData<List<Recipe>> mealPlan_get(String planName) {
        // get from cache
        MutableLiveData<List<Recipe>> recipeList = mRemoteRecipeMap.get(planName);
        if (recipeList != null) {
            return recipeList;
        } else { // means it has not been created, and fragment wants to observe
            mRemoteRecipeMap.put(planName, new MutableLiveData<>());
            return mRemoteRecipeMap.get(planName);
        }
    }

    @Override
    public void mealPlan_update(String planName) {
        // no reason to update if content already is located in list
        if (mRemoteRecipeMap.get(planName) == null || mRemoteRecipeMap.size() == 0)
            mRecipeRemoteDataSource.getMealPlan(this, planName);
    }

    @Override
    public void fav_update() {
        mDatabaseWriteExecutor.execute(() -> mLocalRecipes.postValue(mRecipeLocalDataSource.getRecipesWithFavoriteFlagSetTo(true)));
    }

    @Override
    public LiveData<List<Recipe>> fav_getAll() {
        return mLocalRecipes;
    }
    @Override
    public void fav_addToFavoriteList(Recipe recipe) {
        // check if exists in db
        mDatabaseWriteExecutor.execute(() -> {
            Recipe r = mRecipeLocalDataSource.getRecipeByUrlId(recipe.url);
            if (r != null) {
                mRecipeLocalDataSource.updateRecipeFavorite(r.recipeId, recipe.isFavorite);
            } else {
                // if not in db, download the recipe and add the flag
                ((RecipeRemoteDataSourceNemlig) mRecipeRemoteDataSource).getRecipeByUrlId(recipe.url, this, recipe.isFavorite, false);
            }
        });
    }

    @Override
    public void fav_addToShoppingList(Recipe recipe) {
        // check if exists in db
        mDatabaseWriteExecutor.execute(() -> {
            Recipe r = mRecipeLocalDataSource.getRecipeByUrlId(recipe.url);
            if (r != null) {
                mRecipeLocalDataSource.updateRecipeShoppingList(r.recipeId, recipe.isShoppingList);
            } else {
                // if not in db, download the recipe and add the flag
                ((RecipeRemoteDataSourceNemlig) mRecipeRemoteDataSource).getRecipeByUrlId(recipe.url, this, false, recipe.isShoppingList);
            }
        });
    }

    // database recipe
    private void save(Recipe recipe) {
        mDatabaseWriteExecutor.execute(() -> {
            mRecipeLocalDataSource.insert(recipe);
        });
    }

}
