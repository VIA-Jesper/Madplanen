package dk.ipfortify.madplanen.ui.fridge;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Objects;

import dk.ipfortify.madplanen.ui.barcode.BarcodeScannerActivity;
import dk.ipfortify.madplanen.R;
import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;
import dk.ipfortify.madplanen.util.converters.WeightConverter;

import static android.app.Activity.RESULT_OK;


public class FridgeAddContentFragment extends Fragment {

    private FridgeAddContentViewModel mViewModel;

    private TextInputEditText ingredientName;
    private TextInputEditText amountNumber;
    private AutoCompleteTextView measureIn;
    private String[] ingredientTypes;

    private RecipeIngredient recipeIngredient;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fridge_add_content, container, false);
        //menu
        setHasOptionsMenu(true);

        // views
        MaterialButton mSaveButton = v.findViewById(R.id.fragment_fridge_add_content_saveButton);
        MaterialButton mScanButton = v.findViewById(R.id.fragment_fridge_add_content_scanButton);
        ingredientName = v.findViewById(R.id.fragment_fridge_add_content_ingredientName);
        amountNumber = v.findViewById(R.id.fragment_fridge_add_content_amountOfIngredient);
        TextView mEnableFeatureTextView = v.findViewById(R.id.fragment_fridge_add_content_enableScanningFeatureTextView);

        // Ingredient types
        measureIn = v.findViewById(R.id.fragment_fridge_add_measureIn);
        ingredientTypes = getResources().getStringArray(R.array.measure_ingredient_in);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ingredientTypes);
        measureIn.setAdapter(adapter);

        // listeners
        mSaveButton.setOnClickListener(this::onClickSaveButton);
        mScanButton.setOnClickListener(this::scanButtonClicked);

        // logged in features
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mEnableFeatureTextView.setVisibility(View.GONE);
        } else {
            mScanButton.setVisibility(View.GONE);
        }


        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FridgeAddContentViewModel.class);

        // is this an edit or new item?
        Bundle b = getArguments();
        if (b != null) {
            recipeIngredient = (RecipeIngredient) b.getSerializable("ingredient");
            ingredientName.setText(recipeIngredient.nameOfIngredient);

            measureIn.setText(ingredientTypes[recipeIngredient.amountType], false);

            if (recipeIngredient.amountType == 0) { // kg
                amountNumber.setText(String.valueOf((int) WeightConverter.gramToKilogram(recipeIngredient.amountInGrams)));
            } else if (recipeIngredient.amountType == 1) { // g
                amountNumber.setText(String.valueOf((int) recipeIngredient.amountInGrams));
            } else if (recipeIngredient.amountType == 2) { // l
                amountNumber.setText(String.valueOf((int) WeightConverter.mililiterToLiter((float) recipeIngredient.amountInMililiter)));
            } else if (recipeIngredient.amountType == 3) { // ml
                amountNumber.setText(String.valueOf((int) recipeIngredient.amountInMililiter));
            }
        }

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item=menu.findItem(R.id.nav_shopping);
        if(item!=null)
            item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    private void onClickSaveButton(View view) {

        mViewModel.getName().setValue(Objects.requireNonNull(ingredientName.getText()).toString());
        mViewModel.getAmountNumber().setValue(Float.valueOf(Objects.requireNonNull(amountNumber.getText()).toString()));
        mViewModel.getMeasureIn().setValue(findSelectedListValue(measureIn.getText().toString()));
        if (recipeIngredient != null){
            mViewModel.updateIngredient(recipeIngredient);
        }
        else {
            mViewModel.saveIngredient();
        }

        requireActivity().onBackPressed();
    }


    private void scanButtonClicked(View view) {
        Intent barcodeIntent = new Intent(view.getContext(), BarcodeScannerActivity.class);
        startActivityForResult(barcodeIntent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            System.out.println("RESULT CODE: " + resultCode);
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ingredientName.setText(data.getStringExtra("name"));
                    amountNumber.setText(data.getStringExtra("amount"));
                }
            }
        }
    }

    private int findSelectedListValue(String text) {
        for (int i = 0; i < ingredientTypes.length; i++) {
            if (ingredientTypes[i].equals(text))
                return i;
        }
        return ingredientTypes.length; // return last item, which should be 0 (other)
    }




}