package dk.ipfortify.madplanen.data.repository.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;

@Dao
public abstract class FridgeLocalDataSource {

    @Query("SELECT * FROM recipe_ingredient")
    public abstract LiveData<List<RecipeIngredient>> getAll();

    @Query("SELECT * FROM recipe_ingredient")
    public abstract List<RecipeIngredient> getAllIngredients();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertIngredient(RecipeIngredient ingredient);

    @Delete
    public abstract void deleteIngredient(RecipeIngredient ingredient);

    @Query("DELETE FROM recipe_ingredient")
    public abstract void deleteAll();

    @Update
    public abstract void updateIngredient(RecipeIngredient recipeIngredient);
}
