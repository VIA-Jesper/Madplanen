
package dk.ipfortify.madplanen.data.model.nemlig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("ALL")
public class NemligIngredient {


    @SerializedName("Text")
    @Expose
    public String text;
    @SerializedName("Amount")
    @Expose
    public String amount;
    @SerializedName("Unit")
    @Expose
    public String unit;
    @SerializedName("GroupId")
    @Expose
    public Integer groupId;
    @SerializedName("MesaureWeightInKg")
    @Expose
    public String measureWeightInKg;
    @SerializedName("MesaureWeightInGram")
    @Expose
    public String measureWeightInGram;
    @SerializedName("MeasureMassInLiter")
    @Expose
    public Object measureMassInLiter;
    @SerializedName("MeasurePieces")
    @Expose
    public String measurePieces;
    @SerializedName("Id")
    @Expose
    public Integer id;

    public NemligIngredient() {
    }
}
