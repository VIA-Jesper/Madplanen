package dk.ipfortify.madplanen.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dk.ipfortify.madplanen.R;
import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;
import dk.ipfortify.madplanen.util.converters.WeightConverter;


public class FridgeContentAdapter extends RecyclerView.Adapter<FridgeContentAdapter.ViewHolder> {

    private final ArrayList<RecipeIngredient> mIngredients;


    public FridgeContentAdapter() {
        this.mIngredients = new ArrayList<>();
    }

    private String[] measureTypes;

    @NotNull
    @Override
    public FridgeContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.fragment_fridge_item, parent, false);
        measureTypes = v.getResources().getStringArray(R.array.measure_ingredient_in);
        return new ViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull FridgeContentAdapter.ViewHolder holder, int position) {


        holder.nameOfIngredient.setText(mIngredients.get(position).nameOfIngredient);
        holder.amountType.setText(measureTypes[mIngredients.get(position).amountType]); // get the string from resource to be language independent
        String amountNumber = mIngredients.get(position).amountType <= 1 ?
                String.format("%s", mIngredients.get(position).amountType == 1 ?
                        mIngredients.get(position).amountInGrams
                        : WeightConverter.gramToKilogram(mIngredients.get(position).amountInGrams))
                : String.format("%s", mIngredients.get(position).amountType == 3 ?
                mIngredients.get(position).amountInMililiter
                : WeightConverter.mililiterToLiter((float) mIngredients.get(position).amountInMililiter));
        holder.amountNumber.setText(amountNumber);

    }

    public void addItems(List<RecipeIngredient> ingredients){
        mIngredients.clear();
        mIngredients.addAll(ingredients);
        notifyDataSetChanged();
    }



    public void clearItems(){
        mIngredients.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }




    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView nameOfIngredient;
        final TextView amountNumber;
        final TextView amountType;


        ViewHolder(View itemView) {
            super(itemView);
            nameOfIngredient = itemView.findViewById(R.id.fragment_fridge_ContentDescriptionTextView);
            amountNumber = itemView.findViewById(R.id.fragment_fridge_amountNumberTextView);
            amountType = itemView.findViewById(R.id.fragment_fridge_measureInTextView);
        }
    }
}
