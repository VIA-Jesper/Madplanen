
package dk.ipfortify.madplanen.data.model.nemlig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@SuppressWarnings("ALL")
public class NemligRoot {

    @SerializedName("content")
    @Expose
    public List<NemligContent> content = null;

    public NemligRoot() {
    }
}
