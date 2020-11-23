package dk.ipfortify.madplanen.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import dk.ipfortify.madplanen.data.model.madplanen.Recipe;

public interface iMealPlan {

    LiveData<List<Recipe>> mealPlan_get(String planName);
    void mealPlan_update(String planName);

}
