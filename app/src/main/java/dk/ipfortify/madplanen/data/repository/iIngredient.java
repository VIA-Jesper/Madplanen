package dk.ipfortify.madplanen.data.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;

public interface iIngredient {

    LiveData<List<RecipeIngredient>> ingredient_getAll();
    void ingredient_add(RecipeIngredient ingredient);
    void ingredient_deleteAll();
    void ingredient_remove(RecipeIngredient ingredient);
    void ingredient_update(RecipeIngredient ingredient);

}
