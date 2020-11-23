package dk.ipfortify.madplanen.data.repository.remote.api;

import androidx.annotation.VisibleForTesting;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dk.ipfortify.madplanen.data.model.madplanen.Recipe;
import dk.ipfortify.madplanen.data.model.nemlig.NemligRoot;
import dk.ipfortify.madplanen.data.listeners.iRecipeRepositoryListener;
import dk.ipfortify.madplanen.data.repository.remote.ServiceGenerator;
import dk.ipfortify.madplanen.data.repository.remote.api.nemlig.NemligApi;
import dk.ipfortify.madplanen.util.converters.NemligConverter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRemoteDataSourceNemlig implements iRecipeRemoteDataSourceNemlig {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Map<String, List<Recipe>> mealplanCache;


    public RecipeRemoteDataSourceNemlig() {
        this.mealplanCache = new HashMap<>();
    }


    @Override
    public void getRecipeByUrlId(String recipeUrlId, iRecipeRepositoryListener listener) {

        NemligApi nemligApi = ServiceGenerator.getApi();
        Call<NemligRoot> call = nemligApi.getRecipe(recipeUrlId);
        call.enqueue(new Callback<NemligRoot>() {
            @Override
            public void onResponse(@NotNull Call<NemligRoot> call, @NotNull Response<NemligRoot> response) {
                if (response.code() == 200) {

                    Recipe r = NemligConverter.convertToDetailedRecipe(Objects.requireNonNull(response.body()).content);
                    r.url = recipeUrlId;
                    listener.listener_recipe_add(r);
                }
            }

            @Override
            public void onFailure(@NotNull Call<NemligRoot> call, @NotNull Throwable t) {

            }
        });

    }

    public void getRecipeByUrlId(String recipeUrlId, iRecipeRepositoryListener listener, boolean favorite, boolean shoppingList) {

        NemligApi nemligApi = ServiceGenerator.getApi();
        Call<NemligRoot> call = nemligApi.getRecipe(recipeUrlId);
        call.enqueue(new Callback<NemligRoot>() {
            @Override
            public void onResponse(@NotNull Call<NemligRoot> call, @NotNull Response<NemligRoot> response) {
                if (response.code() == 200) {

                    Recipe r = NemligConverter.convertToDetailedRecipe(Objects.requireNonNull(response.body()).content);
                    r.url = recipeUrlId;
                    if (favorite)
                        r.isFavorite = true;
                    if (shoppingList)
                        r.isShoppingList = true;

                    listener.listener_recipe_add(r);
                }
            }

            @Override
            public void onFailure(@NotNull Call<NemligRoot> call, @NotNull Throwable t) {

            }
        });

    }

    @Override
    public void getMealPlan(iRecipeRepositoryListener listener, String mealTypeName) {
        if (mealplanCache.containsKey(mealTypeName)) {
            listener.listener_recipe_add(mealTypeName, mealplanCache.get(mealTypeName));
        } else {
            NemligApi nemligApi = ServiceGenerator.getApi();
            Call<NemligRoot> call = nemligApi.getFoodPlan(mealTypeName);
            call.enqueue(new Callback<NemligRoot>() {
                @Override
                public void onResponse(@NotNull Call<NemligRoot> call, @NotNull Response<NemligRoot> response) {
                    if (response.code() == 200) {
                        List<Recipe> recipes = NemligConverter.convertToRecipe(Objects.requireNonNull(response.body()).content);
                        listener.listener_recipe_add(mealTypeName, recipes);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<NemligRoot> call, @NotNull Throwable t) {

                }
            });
        }


    }


}
