package dk.ipfortify.madplanen.ui.fridge;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Objects;

import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;
import dk.ipfortify.madplanen.data.repository.IngredientRepository;
import dk.ipfortify.madplanen.data.repository.iIngredient;
import dk.ipfortify.madplanen.util.converters.WeightConverter;

public class FridgeAddContentViewModel extends AndroidViewModel {

    private final iIngredient repository;

    private final MutableLiveData<String> name;
    private final MutableLiveData<Float> amountNumber;
    private final MutableLiveData<Integer> measureIn;


    public FridgeAddContentViewModel(Application app) {
        super(app);
        repository = IngredientRepository.getInstance(app);
        name = new MutableLiveData<>();
        amountNumber = new MutableLiveData<>();
        amountNumber.setValue(0f);
        measureIn = new MutableLiveData<>();
        measureIn.setValue(0);

    }



    public void saveIngredient() {

        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.nameOfIngredient = name.getValue();
        ingredient.amountType = Objects.requireNonNull(measureIn.getValue());

        insertIngredientValues(ingredient);
        repository.ingredient_add(ingredient);

    }

    private void insertIngredientValues(RecipeIngredient ingredient) {
        if (Objects.requireNonNull(measureIn.getValue()) == 0) { // kg
            ingredient.amountInGrams = WeightConverter.kilogramToGram(Objects.requireNonNull(amountNumber.getValue()));
        } else if (measureIn.getValue() == 1) { // g
            ingredient.amountInGrams = Objects.requireNonNull(amountNumber.getValue());
        } else if (measureIn.getValue() == 2) { // l
            ingredient.amountInMililiter = WeightConverter.literToMililiter(Objects.requireNonNull(amountNumber.getValue()));
        } else if (measureIn.getValue() == 3) { // ml
            ingredient.amountInMililiter = Objects.requireNonNull(amountNumber.getValue());
        }
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<Float> getAmountNumber() {
        return amountNumber;
    }

    public MutableLiveData<Integer> getMeasureIn() {
        return measureIn;
    }


    public void updateIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredient.nameOfIngredient = name.getValue();
        insertIngredientValues(recipeIngredient);
        repository.ingredient_update(recipeIngredient);
    }
}