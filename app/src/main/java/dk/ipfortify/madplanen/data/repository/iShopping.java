package dk.ipfortify.madplanen.data.repository;

import androidx.lifecycle.LiveData;

import java.util.HashMap;

import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;

public interface iShopping {

    LiveData<HashMap<String, RecipeIngredient>> get();
    void calculate();
}
