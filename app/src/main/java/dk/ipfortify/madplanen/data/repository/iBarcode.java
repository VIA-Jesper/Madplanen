package dk.ipfortify.madplanen.data.repository;

import androidx.lifecycle.MutableLiveData;

import dk.ipfortify.madplanen.data.model.BarcodeModel;

public interface iBarcode {

    void barcode_init();
    MutableLiveData<BarcodeModel> barcode_get();
    void barcode_set(String barcodeText);
    void barcode_save(String name, String amount);
}
