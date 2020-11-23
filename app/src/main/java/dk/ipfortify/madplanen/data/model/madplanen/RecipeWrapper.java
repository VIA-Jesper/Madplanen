package dk.ipfortify.madplanen.data.model.madplanen;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RecipeWrapper {

    @Embedded
    public Recipe recipe;

    @Relation(
            parentColumn = "recipeId",
            entityColumn = "fkRecipeId",
            entity = RecipeIngredient.class
    )
    public List<RecipeIngredient> ingredients;


    @Relation(
            parentColumn = "recipeId",
            entityColumn = "fkRecipeId",
            entity = RecipeInstruction.class
    )
    public List<RecipeInstruction> instructions;
}
