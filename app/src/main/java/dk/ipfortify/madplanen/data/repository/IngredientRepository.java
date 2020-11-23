package dk.ipfortify.madplanen.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dk.ipfortify.madplanen.data.listeners.iFridgeRepostitoryListener;
import dk.ipfortify.madplanen.data.model.BarcodeModel;
import dk.ipfortify.madplanen.data.model.madplanen.Recipe;
import dk.ipfortify.madplanen.data.repository.local.dao.FridgeLocalDataSource;
import dk.ipfortify.madplanen.data.repository.local.dao.RecipeLocalDataSource;
import dk.ipfortify.madplanen.data.repository.local.database.FridgeDatabase;
import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;
import dk.ipfortify.madplanen.data.repository.local.database.RecipeDatabase;
import dk.ipfortify.madplanen.data.repository.remote.api.firebase.BarcodeRemoteDataSource;
import dk.ipfortify.madplanen.data.repository.remote.api.firebase.iBarcodeRemoteDataSource;

public class IngredientRepository implements iFridgeRepostitoryListener, iBarcode, iIngredient, iShopping {

    private static IngredientRepository instance;

    private final RecipeDatabase mRecipeDatabase;
    private final FridgeLocalDataSource mFridgeLocalDataSource;
    private final RecipeLocalDataSource mRecipeLocalDataSource;
    private final iBarcodeRemoteDataSource mBarcodeRemoteDataSource;

    private final MutableLiveData<HashMap<String, RecipeIngredient>> mIngredientHashMap;
    private final MutableLiveData<BarcodeModel> mBarcodeModel;



    private IngredientRepository(Application application) {
        mFridgeLocalDataSource = FridgeDatabase.getInstance(application).dao();
        mRecipeDatabase = RecipeDatabase.getInstance(application);
        mRecipeLocalDataSource = mRecipeDatabase.dao();
        mIngredientHashMap = new MutableLiveData<>();
        mBarcodeRemoteDataSource = new BarcodeRemoteDataSource(this);
        mBarcodeModel = new MutableLiveData<>();
        mBarcodeModel.setValue(new BarcodeModel());
    }

    public static synchronized IngredientRepository getInstance(Application application) {
        if(instance == null)
            instance = new IngredientRepository(application);
        return instance;
    }


    @Override
    public LiveData<List<RecipeIngredient>> ingredient_getAll() {
        return mFridgeLocalDataSource.getAll();
    }

    @Override
    public void ingredient_add(RecipeIngredient ingredient) {
        FridgeDatabase.databaseWriteExecutor.execute(() -> mFridgeLocalDataSource.insertIngredient(ingredient));
    }

    @Override
    public void ingredient_deleteAll() {
        FridgeDatabase.databaseWriteExecutor.execute(mFridgeLocalDataSource::deleteAll);
    }

    @Override
    public void ingredient_remove(RecipeIngredient ingredient) {
        FridgeDatabase.databaseWriteExecutor.execute(() -> mFridgeLocalDataSource.deleteIngredient(ingredient));
    }

    @Override
    public void ingredient_update(RecipeIngredient ingredient) {
        FridgeDatabase.databaseWriteExecutor.execute(()-> mFridgeLocalDataSource.updateIngredient(ingredient));
    }


    @Override
    public void barcode_init() {
        mBarcodeModel.setValue(new BarcodeModel());
    }

    @Override
    public MutableLiveData<BarcodeModel> barcode_get() {
        return mBarcodeModel;
    }

    @Override
    public void barcode_set(String barcodeText) {
        mBarcodeRemoteDataSource.get(barcodeText);
    }

    private void barcode_add(BarcodeModel barcodeModel) {
        this.mBarcodeModel.postValue(barcodeModel);
    }

    @Override
    public void barcode_save(String name, String amount) {
        Objects.requireNonNull(mBarcodeModel.getValue()).name = name;
        mBarcodeModel.getValue().amount = amount;
        mBarcodeRemoteDataSource.save(mBarcodeModel.getValue());
    }

    @Override
    public void addListener(BarcodeModel barcodeModel) {
        barcode_add(barcodeModel);
    }


    @Override
    public LiveData<HashMap<String, RecipeIngredient>> get() {
        return mIngredientHashMap;
    }

    @Override
    public void calculate() {
        // logic for comparing ingredients
        mRecipeDatabase.databaseWriteExecutor.execute(() -> {
            // get list of ingredient in fridge
            List<RecipeIngredient> fridgeIngredients = mFridgeLocalDataSource.getAllIngredients();

            // get list of needed ingredients
            List<Recipe> recipes = mRecipeLocalDataSource.getRecipesWithCookingFlag(true);
            List<RecipeIngredient> neededIngredients = new ArrayList<>();
            for (Recipe r : recipes) {

                neededIngredients.addAll(r.ingredients);
            }
            // add ingredients together to get total needed
            HashMap<String, RecipeIngredient> totalIngredients = removeDuplicateIngredients(neededIngredients);

            // subtract total needed with owned.
            subtractIngredients(fridgeIngredients, totalIngredients);

            mIngredientHashMap.postValue(totalIngredients);
        });
    }

    @NotNull
    private HashMap<String, RecipeIngredient> removeDuplicateIngredients(List<RecipeIngredient> neededIngredients) {
        HashMap<String, RecipeIngredient> totalIngredients = new HashMap<>();
        for (RecipeIngredient ingredient : neededIngredients) {
            // check if ingredient already is present
            if (totalIngredients.containsKey(ingredient.nameOfIngredient.toLowerCase())) {
                RecipeIngredient storedIngredient = totalIngredients.get(ingredient.nameOfIngredient.toLowerCase());
                // update value
                Objects.requireNonNull(storedIngredient).amountInGrams += ingredient.amountInGrams;
                storedIngredient.amountInMililiter += ingredient.amountInMililiter;

                totalIngredients.put(ingredient.nameOfIngredient.toLowerCase(), storedIngredient);
            } else {
                totalIngredients.put(ingredient.nameOfIngredient.toLowerCase(), ingredient);
            }
        }
        return totalIngredients;
    }

    private void subtractIngredients(List<RecipeIngredient> fridgeIngredients, HashMap<String, RecipeIngredient> totalIngredients) {
        for (RecipeIngredient fridgeIngredient : fridgeIngredients) {
            for (Map.Entry<String, RecipeIngredient> entry : totalIngredients.entrySet()) {
                if (entry.getKey().contains(fridgeIngredient.nameOfIngredient.toLowerCase())) {
                    RecipeIngredient ingredient = entry.getValue();
                    ingredient.amountInGrams -= fridgeIngredient.amountInGrams;
                    ingredient.amountInMililiter -= fridgeIngredient.amountInMililiter;
                }
            }
        }
    }
}
