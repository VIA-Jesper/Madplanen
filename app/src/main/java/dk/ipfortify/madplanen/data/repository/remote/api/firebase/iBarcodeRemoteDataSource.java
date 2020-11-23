package dk.ipfortify.madplanen.data.repository.remote.api.firebase;

import dk.ipfortify.madplanen.data.model.BarcodeModel;

public interface iBarcodeRemoteDataSource {
    void save(BarcodeModel barcodeModel);

    void get(String barcodeId);
}
