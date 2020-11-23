package dk.ipfortify.madplanen.ui.shopping;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.HashMap;

import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;
import dk.ipfortify.madplanen.data.repository.iShopping;
import dk.ipfortify.madplanen.data.repository.IngredientRepository;

public class ShoppingFragmentViewModel extends AndroidViewModel {


    private final LiveData<HashMap<String, RecipeIngredient>> ingredients;

    public ShoppingFragmentViewModel(Application app) {
        super(app);

        iShopping repository = IngredientRepository.getInstance(app);
        ingredients = repository.get();

        repository.calculate();
    }

    public LiveData<HashMap<String, RecipeIngredient>> getIngredients() {
        return ingredients;
    }
}