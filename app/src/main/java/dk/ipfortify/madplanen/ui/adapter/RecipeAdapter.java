package dk.ipfortify.madplanen.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import dk.ipfortify.madplanen.R;
import dk.ipfortify.madplanen.data.model.madplanen.Recipe;
import dk.ipfortify.madplanen.util.BlurBuilder;


import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {


    private final List<Recipe> mRecipes;
    final private OnListItemClickListener mOnListItemClickListener;


    public RecipeAdapter(OnListItemClickListener listener) {
        this.mRecipes = new ArrayList<>();
        mOnListItemClickListener = listener;
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_mealplan_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.headerText.setText(mRecipes.get(position).name);
        viewHolder.prepTimeText.setText(mRecipes.get(position).totalWorkTime);

        if (mRecipes.get(position).isFavorite) {
            viewHolder.favoriteIcon.setColorFilter(Color.RED);
        } else {
            viewHolder.favoriteIcon.clearColorFilter();
        }


        if (mRecipes.get(position).isShoppingList) {
            viewHolder.shoppingListIcon.setImageResource(R.drawable.ic_baseline_remove_24);
        } else {
            viewHolder.shoppingListIcon.setImageResource(R.drawable.ic_baseline_add_24);
        }



        Glide.with(viewHolder.itemView)
                .asBitmap()
                .load(mRecipes.get(position).imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        viewHolder.image.setImageBitmap(resource);
                        Bitmap blured = BlurBuilder.blur(viewHolder.itemView.getContext(), resource);
                        viewHolder.blurImage.setImageBitmap(blured);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

    }

    public void addItems(List<Recipe> recipes) {
        mRecipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mRecipes.clear();
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return mRecipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        final ImageView image;
        final ImageView blurImage;
        final TextView headerText;
        final TextView prepTimeText;
        final ImageView favoriteIcon;
        final ImageView shoppingListIcon;


        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.fragment_mealplan_item_cardImage);
            blurImage = itemView.findViewById(R.id.fragment_mealplan_item_cardImage_blur);
            headerText = itemView.findViewById(R.id.fragment_mealplan_item_cardHeaderText);
            prepTimeText = itemView.findViewById(R.id.fragment_mealplan_item_cardPrepTimeText);
            favoriteIcon = itemView.findViewById(R.id.fragment_mealplan_item_favoriteIcon);
            favoriteIcon.setOnClickListener(this);
            shoppingListIcon = itemView.findViewById(R.id.fragment_mealplan_item_shoppingListIcon);
            shoppingListIcon.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v == shoppingListIcon) {
                mOnListItemClickListener.shoppingIconClick(mRecipes.get(getAdapterPosition()));
                notifyDataSetChanged();
            } else if (v == favoriteIcon) {
                mOnListItemClickListener.favIconClick(mRecipes.get(getAdapterPosition()));
                notifyDataSetChanged();
            } else {
                mOnListItemClickListener.onListItemClick(getAdapterPosition());
            }
        }
    }


    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);

        void favIconClick(Recipe recipe);

        void shoppingIconClick(Recipe recipe);
    }

}




