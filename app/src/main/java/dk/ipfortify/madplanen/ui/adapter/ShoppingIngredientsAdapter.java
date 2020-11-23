package dk.ipfortify.madplanen.ui.adapter;


import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

import dk.ipfortify.madplanen.R;
import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;

public class ShoppingIngredientsAdapter extends RecyclerView.Adapter<ShoppingIngredientsAdapter.ViewHolder> {



    private final HashMap<String, RecipeIngredient>  mIngredients;
    private final String[] mKeys;


    public ShoppingIngredientsAdapter(HashMap<String, RecipeIngredient> ingredientList) {


        mIngredients = ingredientList;
        mKeys = ingredientList.keySet().toArray(new String[mIngredients.size()]);
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_shopping_ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        viewHolder.ingredientName.setText(mKeys[position]);
        if (Objects.requireNonNull(mIngredients.get(mKeys[position])).amountInMililiter == 0.0) {
            viewHolder.ingredientAmount.setText(String.valueOf((int) Objects.requireNonNull(mIngredients.get(mKeys[position])).amountInGrams));
            viewHolder.ingredientAmount.append("g");
        }else {
            viewHolder.ingredientAmount.setText(String.valueOf((int) Objects.requireNonNull(mIngredients.get(mKeys[position])).amountInMililiter));
            viewHolder.ingredientAmount.append("ml");
        }


        if (Objects.requireNonNull(mIngredients.get(mKeys[position])).amountInMililiter < 0 || Objects.requireNonNull(mIngredients.get(mKeys[position])).amountInGrams < 0)
        {
            viewHolder.ingredientName.setPaintFlags(viewHolder.ingredientName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.ingredientAmount.setVisibility(View.INVISIBLE);
        }

    }



    public int getItemCount() {
        return mIngredients.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder  {

        final TextView ingredientName;
        final TextView ingredientAmount;

        ViewHolder(View itemView) {
            super(itemView);

            ingredientName = itemView.findViewById(R.id.fragment_shopping_ingredient_item_ingredientName);
            ingredientAmount = itemView.findViewById(R.id.fragment_shopping_ingredient_item_ingredientAmount);


        }




    }


}


