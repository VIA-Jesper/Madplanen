package dk.ipfortify.madplanen.ui.barcode;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Objects;

import dk.ipfortify.madplanen.data.model.BarcodeModel;
import dk.ipfortify.madplanen.data.repository.IngredientRepository;
import dk.ipfortify.madplanen.data.repository.iBarcode;

public class BarCodeScannerActivityViewModel extends AndroidViewModel {


    private final iBarcode repository;


    public BarCodeScannerActivityViewModel(@NonNull Application application) {
        super(application);
        repository = IngredientRepository.getInstance(application);
        repository.barcode_init();
    }

    public void init() {
        repository.barcode_init();
    }
    public void updateBarcodeData(String barcode) {
        repository.barcode_set(barcode);
    }

    public LiveData<BarcodeModel> getBarcodeModel() {
        return repository.barcode_get();
    }

    public void saveBarcodeName(String name, String amount) {
        repository.barcode_save(name, amount);

    }

    public void setBarcodeId(String id) {
        Objects.requireNonNull(repository.barcode_get().getValue()).barcodeId = id;
    }
}
