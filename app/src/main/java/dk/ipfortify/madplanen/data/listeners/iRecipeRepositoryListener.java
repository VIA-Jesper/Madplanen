package dk.ipfortify.madplanen.data.listeners;

import java.util.List;

import dk.ipfortify.madplanen.data.model.madplanen.Recipe;

public interface iRecipeRepositoryListener {

    void listener_recipe_add(Recipe recipe);

    void listener_recipe_add(String key, List<Recipe> recipes);
}
