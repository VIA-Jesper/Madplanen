package dk.ipfortify.madplanen.ui.favorite;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import dk.ipfortify.madplanen.R;
import dk.ipfortify.madplanen.data.model.madplanen.Recipe;
import dk.ipfortify.madplanen.ui.adapter.RecipeAdapter;

public class FavoriteRecipesFragment extends Fragment implements RecipeAdapter.OnListItemClickListener {

    private FavoriteRecipesViewModel mViewModel;
    private RecipeAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);

        RecyclerView mRecyclerView = v.findViewById(R.id.fragment_favorite_recyclerView);
        mAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavoriteRecipesViewModel.class);

        mViewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            mAdapter.clearItems();
            mAdapter.addItems(recipes);
        });
        mViewModel.update();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Bundle bundle = new Bundle();
        String urlId = Objects.requireNonNull(mViewModel.getRecipes().getValue()).get(clickedItemIndex).url;
        bundle.putString("link_url", urlId);
        Navigation.findNavController(requireView()).navigate(R.id.nav_recipie, bundle);
    }

    @Override
    public void favIconClick(Recipe recipe) {
        mViewModel.updateFavorite(recipe);
    }

    @Override
    public void shoppingIconClick(Recipe recipe) {
        mViewModel.updateShoppingList(recipe);
    }
}