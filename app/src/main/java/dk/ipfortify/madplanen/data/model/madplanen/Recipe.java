package dk.ipfortify.madplanen.data.model.madplanen;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "recipe")
public class Recipe {


    @PrimaryKey(autoGenerate = true)
    public long recipeId;
    public String name;
    public String url;
    public String imageUrl;
    public int numberOfPersons;
    public String totalPrepareTime;
    public String totalWorkTime;
    @Ignore
    public List<RecipeIngredient> ingredients;
    @Ignore
    public List<RecipeInstruction> instructions;

    public boolean isFavorite;
    public boolean isShoppingList;

    public Recipe() {
    }

    @Ignore
    public Recipe(String name, String url, String imageUrl, int numberOfPersons, String totalPrepareTime, String totalWorkTime, List<RecipeIngredient> ingredients, List<RecipeInstruction> instructions) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.numberOfPersons = numberOfPersons;
        this.totalPrepareTime = totalPrepareTime;
        this.totalWorkTime = totalWorkTime;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }
    @Ignore
    public Recipe(int recipeId, String name, String url, String imageUrl, int numberOfPersons, String totalPrepareTime, String totalWorkTime, List<RecipeIngredient> ingredients, List<RecipeInstruction> instructions) {
        this.recipeId = recipeId;
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.numberOfPersons = numberOfPersons;
        this.totalPrepareTime = totalPrepareTime;
        this.totalWorkTime = totalWorkTime;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }



}
