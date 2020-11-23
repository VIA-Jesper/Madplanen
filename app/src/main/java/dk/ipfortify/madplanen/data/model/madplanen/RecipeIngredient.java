package dk.ipfortify.madplanen.data.model.madplanen;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "recipe_ingredient")
public class RecipeIngredient implements Serializable {


    @PrimaryKey(autoGenerate = true)
    public long ingredientId;

    @ColumnInfo(name = "fkRecipeId")
    public long fkRecipeId;

    @ColumnInfo(name = "name")
    public String nameOfIngredient;

    @ColumnInfo(name = "amount")
    public float amountNumber; // 2

    @ColumnInfo(name = "type")
    public int amountType; // stk. (stored as int value to represent the value)

    @ColumnInfo(name = "inGrams")
    public float amountInGrams; // 800

    @ColumnInfo(name = "inMililiters")
    public double amountInMililiter; //0.02


}
