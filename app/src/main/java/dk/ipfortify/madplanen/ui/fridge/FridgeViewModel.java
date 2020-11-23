package dk.ipfortify.madplanen.ui.fridge;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;
import dk.ipfortify.madplanen.data.repository.IngredientRepository;
import dk.ipfortify.madplanen.data.repository.iIngredient;

public class FridgeViewModel extends AndroidViewModel {


    private final iIngredient repository;
    private final LiveData<List<RecipeIngredient>> ingredients;


    public FridgeViewModel(Application app) {
        super(app);
        repository = IngredientRepository.getInstance(app);
        ingredients = repository.ingredient_getAll();

    }

    public LiveData<List<RecipeIngredient>> getAllIngredients() {
        return ingredients;
    }

    public void deleteAll() {
        repository.ingredient_deleteAll();
    }


    public void deleteItem(RecipeIngredient recipeIngredient) {
        repository.ingredient_remove(recipeIngredient);
    }
}