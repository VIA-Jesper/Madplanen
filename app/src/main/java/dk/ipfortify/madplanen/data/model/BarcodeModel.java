package dk.ipfortify.madplanen.data.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BarcodeModel {

    public String barcodeId;
    public String name;
    public String amount;
    @Exclude
    public boolean isFound;

    public BarcodeModel() {
        barcodeId = "";
        name = "";
    }

    public BarcodeModel(String barcodeId, String name, boolean isFound) {
        this.barcodeId = barcodeId;
        this.name = name;
        this.isFound = isFound;
    }


}
