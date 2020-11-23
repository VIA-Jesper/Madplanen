
package dk.ipfortify.madplanen.data.model.nemlig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressWarnings("ALL")
public class NemligRecipe {

    @SerializedName("PrimaryImage")
    @Expose
    public String primaryImage;
    @SerializedName("Id")
    @Expose
    public String id;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Url")
    @Expose
    public String url;

    @SerializedName("TotalTime")
    @Expose
    public String totalTime;

    @SerializedName("NumberOfPersons")
    @Expose
    public Integer numberOfPersons;

    public NemligRecipe() {
    }
}
