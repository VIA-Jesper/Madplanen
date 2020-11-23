
package dk.ipfortify.madplanen.data.model.nemlig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("ALL")
public class NemligSubElement {

    @SerializedName("Id")
    @Expose
    public String id;

    @SerializedName("Name")
    @Expose
    public String name;

    @SerializedName("Recipe")
    @Expose
    public NemligRecipe recipe;
    @SerializedName("Text")
    @Expose
    public String text;

    public NemligSubElement() {

    }

}
