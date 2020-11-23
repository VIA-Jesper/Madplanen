
package dk.ipfortify.madplanen.data.model.nemlig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("ALL")
public class NemligContent {
    @SerializedName("SubElements")
    @Expose
    public List<NemligSubElement> subElements = null;
    @SerializedName("Media")
    @Expose
    public List<NemligMedium> media = null;
    @SerializedName("Id")
    @Expose
    public String id;
    @SerializedName("Header")
    @Expose
    public String header;
    @SerializedName("Instructions")
    @Expose
    public String instructions;
    @SerializedName("WorkTime")
    @Expose
    public String workTime;
    @SerializedName("TotalTime")
    @Expose
    public String totalTime;
    @SerializedName("NumberOfPersons")
    @Expose
    public Integer numberOfPersons;
    @SerializedName("IngredientGroups")
    @Expose
    public List<NemligIngredientGroup> ingredientGroups = null;


    public NemligContent() {
    }
}
