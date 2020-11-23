package dk.ipfortify.madplanen.data.repository.remote.api.nemlig;

import dk.ipfortify.madplanen.data.model.nemlig.NemligRoot;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NemligApi {

    @GET("opskrifter/madplaner/{foodplanType}?GetAsJson=1&d=1")
    Call<NemligRoot> getFoodPlan(@Path("foodplanType") String foodplanType);

    @GET("{recipeId}?GetAsJson=1")
    Call<NemligRoot> getRecipe(@Path("recipeId") String recipeId);
}
