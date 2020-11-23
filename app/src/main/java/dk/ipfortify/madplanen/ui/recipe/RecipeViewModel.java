package dk.ipfortify.madplanen.ui.recipe;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import dk.ipfortify.madplanen.data.repository.iRecipe;
import dk.ipfortify.madplanen.data.repository.RecipeRepository;
import dk.ipfortify.madplanen.data.model.madplanen.Recipe;

public class RecipeViewModel extends AndroidViewModel {

    private final iRecipe repository;


    public RecipeViewModel(Application app) {
        super(app);
        repository = RecipeRepository.getInstance(app);
    }

    public void update(String linkUrl) {
        repository.recipe_update(linkUrl);
    }

    public LiveData<Recipe> getRecipe() {
        return repository.recipe_get();
    }

}
