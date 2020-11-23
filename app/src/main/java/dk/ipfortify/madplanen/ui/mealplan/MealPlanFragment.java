package dk.ipfortify.madplanen.ui.mealplan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import dk.ipfortify.madplanen.R;
import dk.ipfortify.madplanen.data.model.madplanen.Recipe;
import dk.ipfortify.madplanen.ui.adapter.RecipeAdapter;


public class MealPlanFragment extends Fragment implements RecipeAdapter.OnListItemClickListener {

    private MealPlanViewModel mViewModel;
    private RecipeAdapter mAdapter;
    private String mealTypeName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mealTypeName = this.requireArguments().getString("fragmentName");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_mealplan, container, false);
        mViewModel = new ViewModelProvider(this).get(mealTypeName, MealPlanViewModel.class); // mealtypename is inserted as key for the particular viewmodel to prevent the same is used multiple times (source: https://stackoverflow.com/a/54862554)
        mViewModel.updateRecipes(mealTypeName);


        RecyclerView mRecyclerView = v.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));


        mAdapter = new RecipeAdapter(MealPlanFragment.this);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.getRecipes(mealTypeName).observe(getViewLifecycleOwner(), recipes -> {
            mAdapter.clearItems();
            mAdapter.addItems(recipes);
        });
    }



    @Override
    public void onListItemClick(int clickedItemIndex) {
        Bundle bundle = new Bundle();
        bundle.putString("link_url", Objects.requireNonNull(mViewModel.getRecipes(mealTypeName).getValue()).get(clickedItemIndex).url);
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
