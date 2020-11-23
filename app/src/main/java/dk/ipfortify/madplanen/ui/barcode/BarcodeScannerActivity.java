package dk.ipfortify.madplanen.ui.barcode;

import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.util.Objects;

import dk.ipfortify.madplanen.R;
import dk.ipfortify.madplanen.data.model.BarcodeModel;
import dk.ipfortify.madplanen.util.ScannerView;


@SuppressWarnings({"unused", "RedundantSuppression"})
public class BarcodeScannerActivity extends AppCompatActivity {

    private static final int EAN13_LENGTH = 13;

    private BarCodeScannerActivityViewModel mViewModel;
    private SurfaceView mCameraView;
    private CameraSource mCameraSource;
    private ScannerView mScannerView;
    private TextView mFoundItem;
    private LinearLayout mLinearLayoutInformationArea;
    private LinearLayout mLinearLayoutWaiting;
    private LinearLayout mLinearLayoutFoundItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        mViewModel = new ViewModelProvider(this).get(BarCodeScannerActivityViewModel.class);
        mViewModel.init();
        // set views
        mCameraView = (SurfaceView) findViewById(R.id.camera_view);
        mScannerView = findViewById(R.id.scan_scannerView);
        mFoundItem = findViewById(R.id.scan_foundItemTextView);
        mLinearLayoutInformationArea = findViewById(R.id.scan_informationArea);
        mLinearLayoutWaiting = findViewById(R.id.scan_waitingLayout);
        mLinearLayoutFoundItem = findViewById(R.id.scan_foundItemLinearLayout);
        // set visibility
        isLookingForBarcode(true);
        // observ
        mViewModel.init();
        mViewModel.getBarcodeModel().observe(this, this::barcodeChanged);
        // initialize
        initializeCamera();
    }

    private void isLookingForBarcode(boolean isLooking) {
        if (isLooking)
        {
            mLinearLayoutInformationArea.setVisibility(View.GONE);
            mLinearLayoutWaiting.setVisibility(View.GONE);
            mLinearLayoutFoundItem.setVisibility(View.GONE);
        } else {
            mLinearLayoutWaiting.setVisibility(View.GONE);
            mLinearLayoutFoundItem.setVisibility(View.VISIBLE);
        }

    }

    private void initializeCamera() {
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.EAN_13)
                        .build();

        mCameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .build();

        mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[] {Manifest.permission.CAMERA}, 100);
                        return;
                    }
                    mCameraSource.start(mCameraView.getHolder());

                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // not needed
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();


                if (barcodes.size() != 0) {
                    String barcode = barcodes.valueAt(0).displayValue;

                    if (barcodes.valueAt(0).displayValue.length() == EAN13_LENGTH && !barcode.equals(Objects.requireNonNull(mViewModel.getBarcodeModel().getValue()).barcodeId)) {
                        mScannerView.stopAnimation();

                        runOnUiThread(() -> {
                            mLinearLayoutInformationArea.setVisibility(View.VISIBLE);
                            mLinearLayoutWaiting.setVisibility(View.VISIBLE);
                        });
                        mViewModel.setBarcodeId(barcode);
                        mViewModel.updateBarcodeData(barcode);
                    }
                }
            }
        });
    }

    private void barcodeChanged(BarcodeModel barcodeModel) {
        if (barcodeModel != null && !barcodeModel.name.isEmpty()) {
            mFoundItem.setText(barcodeModel.name);
            isLookingForBarcode(false);
        }

        if (barcodeModel != null  && !barcodeModel.barcodeId.isEmpty() && !barcodeModel.isFound) {
            createAlertDialogNotFound();
        }
    }

    private void createAlertDialogNotFound() {
        final EditText input = new EditText(this);
        input.setHint(R.string.item_name);
        input.setMaxLines(1);
        final EditText amount = new EditText(this);
        amount.setHint(R.string.amount);
        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(5,5,5,5);
        layout.addView(input);
        layout.addView(amount);
        new MaterialAlertDialogBuilder(mFoundItem.getContext())
                .setTitle(R.string.not_found_in_db)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    // save the item to the db (for the sake of this PoC. Item will be overwritten in DB
                    mViewModel.saveBarcodeName(input.getText().toString(), amount.getText().toString());

                    // finish
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", input.getText().toString());
                    resultIntent.putExtra("amount", amount.getText().toString());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    // nothing
                })
                .setView(layout)
                .show();
    }

    private void createAlertDialogIncorrect() {
        final EditText input = new EditText(this);
        input.setHint(R.string.item_name);
        input.setMaxLines(1);
        final EditText amount = new EditText(this);
        amount.setHint(R.string.amount);
        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(5,5,5,5);
        layout.addView(input);
        layout.addView(amount);

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.type_correct_name)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    // save the item to the db (for the sake of this PoC. Item will be overwritten in DB
                    mViewModel.saveBarcodeName(input.getText().toString(), amount.getText().toString());

                    // finish
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", input.getText().toString());
                    resultIntent.putExtra("amount", amount.getText().toString());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    // nothing
                })
                .setView(layout)
                .show();
    }

    public void correctClick(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", Objects.requireNonNull(mViewModel.getBarcodeModel().getValue()).name);
        resultIntent.putExtra("amount", Objects.requireNonNull(mViewModel.getBarcodeModel().getValue()).amount);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void incorrectClick(View view) {
        createAlertDialogIncorrect();
    }


}