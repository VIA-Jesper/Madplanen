package dk.ipfortify.madplanen.data.repository.remote.api.firebase;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import dk.ipfortify.madplanen.data.listeners.iFridgeRepostitoryListener;
import dk.ipfortify.madplanen.data.model.BarcodeModel;

public class BarcodeRemoteDataSource implements iBarcodeRemoteDataSource {

    private final DatabaseReference ref;
    private static final String BARCODE_FOLDER = "barcodes";
    private final iFridgeRepostitoryListener listener;
    private final Map<String, BarcodeModel> barcodeCache;


    public BarcodeRemoteDataSource(iFridgeRepostitoryListener listener) {
        this.listener = listener;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        barcodeCache = new HashMap<>();
    }

    @Override
    public void save(BarcodeModel barcodeModel){
        if (!barcodeModel.barcodeId.equals("-1")) // extra check
            ref.child(BARCODE_FOLDER).child(barcodeModel.barcodeId).setValue(barcodeModel);
    }



    @Override
    public void get(String barcodeId) {
        if (barcodeCache.containsKey(barcodeId)) {
            try {
                Thread.sleep(100);// some delay or ui changes cant keep up
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listener.addListener(barcodeCache.get(barcodeId));
        } else {
            ref.child(BARCODE_FOLDER).child(barcodeId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    BarcodeModel bm = snapshot.getValue(BarcodeModel.class);
                    if (bm == null) {
                        listener.addListener(new BarcodeModel(barcodeId, "", false));
                    } else {
                        bm.isFound = true;
                        listener.addListener(bm);
                        barcodeCache.put(barcodeId, bm);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.addListener(new BarcodeModel("-1", "", false));
                }
            });
        }


    }


}
