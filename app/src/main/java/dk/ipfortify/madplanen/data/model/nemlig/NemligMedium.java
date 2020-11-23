
package dk.ipfortify.madplanen.data.model.nemlig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressWarnings("ALL")
public class NemligMedium {

    @SerializedName("Url")
    @Expose
    public String url;

    public NemligMedium() {
    }
}
