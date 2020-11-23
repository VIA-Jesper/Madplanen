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
import dk.ipfortify.madplanen.util.converters.RecipeAmountType;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {



    private final ArrayList<RecipeIngredient> ingredients;
    public RecipeIngredientsAdapter() {
        ingredients = new ArrayList<>();
    }

    @NotNull
    public RecipeIngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_recipe_item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // amount
        if (ingredients.get(position).amountNumber == 0.0) {
            boolean isInt = ingredients.get(position).amountInGrams == Math.ceil(ingredients.get(position).amountInGrams);
            holder.amount.setText(
                    isInt ? "" + Math.round(ingredients.get(position).amountInGrams) :
                            "" + ingredients.get(position).amountInGrams);
        } else {
            boolean isInt = ingredients.get(position).amountNumber == Math.ceil(ingredients.get(position).amountNumber);
            holder.amount.setText(
                    isInt ? "" + Math.round(ingredients.get(position).amountNumber) :
                            "" + ingredients.get(position).amountNumber);
        }

        // unit
        holder.units.setText(String.valueOf(RecipeAmountType.values()[ingredients.get(position).amountType]));
        if (ingredients.get(position).amountType == 0) // means its not in system and should not be displayed
        {
            holder.units.setVisibility(View.INVISIBLE);
            //holder.amount.setVisibility(View.INVISIBLE);
        }
        // text
        holder.text.setText(ingredients.get(position).nameOfIngredient);
        // weight
        if (ingredients.get(position).amountInGrams != 0.0) {
            boolean isInt = ingredients.get(position).amountInGrams == Math.ceil(ingredients.get(position).amountInGrams);
            holder.weight.setText(
                    isInt ? "" + Math.round(ingredients.get(position).amountInGrams) :
                            "" + ingredients.get(position).amountInGrams);

            holder.weight.append("g");
        } else if (ingredients.get(position).amountInMililiter != 0.0 ) {
            boolean isInt = ingredients.get(position).amountInMililiter == Math.ceil(ingredients.get(position).amountInMililiter);
            holder.weight.setText(
                    isInt ? "" + Math.round(ingredients.get(position).amountInMililiter) :
                            "" + ingredients.get(position).amountInMililiter);
            holder.weight.append("ml");
        } else {
            holder.weight.setText("-");
        }

    }

    public void addItems(List<RecipeIngredient> ingredients){
        this.ingredients.addAll(ingredients);
        notifyDataSetChanged();
    }

    public void clearItems() {
        ingredients.clear();
        notifyDataSetChanged();
    }


    public int getItemCount() {
        return ingredients.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        final TextView units;
        final TextView amount;
        final TextView text;
        final TextView weight;


        ViewHolder(View itemView) {
            super(itemView);
            units = itemView.findViewById(R.id.recipefragment_item_unit);
            amount = itemView.findViewById(R.id.recipefragment_item_amount);
            text = itemView.findViewById(R.id.recipefragment_item_text);
            weight = itemView.findViewById(R.id.recipefragment_item_inGram);
        }
    }
}