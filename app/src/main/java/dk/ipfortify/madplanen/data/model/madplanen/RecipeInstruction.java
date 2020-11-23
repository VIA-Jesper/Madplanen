package dk.ipfortify.madplanen.data.model.madplanen;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "recipe_instruction")
public class RecipeInstruction {


    @PrimaryKey(autoGenerate = true)
    public long instructionId;

    @ColumnInfo(name = "step")
    public String step;

    @ColumnInfo(name = "fkRecipeId")
    public long fkRecipeId;
}
