
package dk.ipfortify.madplanen.data.model.nemlig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@SuppressWarnings("ALL")
public class NemligIngredientGroup {

    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Ingredients")
    @Expose
    public List<NemligIngredient> ingredients = null;

    public NemligIngredientGroup() {
    }
}
