package dk.ipfortify.madplanen.data.repository.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.List;

import dk.ipfortify.madplanen.data.model.madplanen.Recipe;
import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;
import dk.ipfortify.madplanen.data.model.madplanen.RecipeInstruction;
import dk.ipfortify.madplanen.data.model.madplanen.RecipeWrapper;

@Dao
public abstract class RecipeLocalDataSource {

    @Insert
    abstract long insertRecipe(Recipe recipe);

    public void insert(Recipe recipe){
        if (recipe == null )
            return;
        if (getRecipeWrapperByName(recipe.name) != null)
            return;

        long recipeId = insertRecipe(recipe); // get the autogen id
        recipe.recipeId = recipeId; // set the id

        recipe.ingredients.forEach(i -> i.fkRecipeId = recipeId);
        insertIngredientList(recipe.ingredients);

        recipe.instructions.forEach(i -> i.fkRecipeId = recipeId);
        insertInstructionList(recipe.instructions);

    }

    @Transaction
    @Insert
    abstract void insertIngredientList(List<RecipeIngredient> ingredients);

    @Transaction
    @Insert
    abstract void insertInstructionList(List<RecipeInstruction> instructions);





    @Transaction
    @Query("SELECT * FROM recipe WHERE name =:name LIMIT 1")
    abstract RecipeWrapper getRecipeWrapperByName(String name);

    @Transaction
    @Query("SELECT * FROM recipe WHERE url =:url LIMIT 1")
    abstract RecipeWrapper getRecipeWrapperByUrlId(String url);

    public Recipe getRecipeByUrlId(String urlId) {
        RecipeWrapper rw = getRecipeWrapperByUrlId(urlId);
        if (rw != null)
        {
            Recipe r = rw.recipe;
            r.ingredients = rw.ingredients;
            r.instructions = rw.instructions;
            return r;
        }
        return null;
    }


    @Query("UPDATE recipe SET isFavorite = :isFavorite WHERE recipeId = :recipeId")
    public abstract void updateRecipeFavorite(long recipeId, boolean isFavorite);

    @Query("UPDATE recipe SET isShoppingList = :isShoppingList WHERE recipeId = :recipeId")
    public abstract void updateRecipeShoppingList(long recipeId, boolean isShoppingList);

    @Transaction
    @Query("SELECT * FROM recipe WHERE isShoppingList = :isShoppingList")
    abstract List<RecipeWrapper> getRecipeWrapperListWithCookingFlag(boolean isShoppingList);

    public List<Recipe> getRecipesWithCookingFlag(boolean inCookingList) {
        List<RecipeWrapper> recipeWrappers = getRecipeWrapperListWithCookingFlag(inCookingList);
        List<Recipe> recipes = new ArrayList<>();
        recipeWrappers.forEach(r -> {
            Recipe recipe = r.recipe;
            recipe.ingredients = r.ingredients;
            recipe.instructions = r.instructions;
            recipes.add(recipe);
        });
        return recipes;
    }

    @Transaction
    @Query("SELECT * FROM recipe WHERE isFavorite = :isFavorite")
    abstract List<RecipeWrapper> getRecipeWrapperListWithFavoriteFlag(boolean isFavorite);


    public List<Recipe> getRecipesWithFavoriteFlagSetTo(boolean favorite) {
        List<RecipeWrapper> recipeWrappers = getRecipeWrapperListWithFavoriteFlag(favorite);
        List<Recipe> recipes = new ArrayList<>();
        recipeWrappers.forEach(r -> {
            Recipe recipe = r.recipe;
            recipe.ingredients = r.ingredients;
            recipe.instructions = r.instructions;
            recipes.add(recipe);
        });
        return recipes;
    }


}









