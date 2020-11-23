package dk.ipfortify.madplanen.data.repository.remote.api;

import dk.ipfortify.madplanen.data.listeners.iRecipeRepositoryListener;

public interface iRecipeRemoteDataSourceNemlig {
    void getRecipeByUrlId(String recipeUrlId, iRecipeRepositoryListener listener);

    void getMealPlan(iRecipeRepositoryListener listener, String mealTypeName);
}
