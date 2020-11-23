package dk.ipfortify.madplanen.data.repository;

import androidx.lifecycle.LiveData;

import dk.ipfortify.madplanen.data.model.madplanen.Recipe;

public interface iRecipe {

    LiveData<Recipe> recipe_get();
    void recipe_update(final String recipeId);


}
