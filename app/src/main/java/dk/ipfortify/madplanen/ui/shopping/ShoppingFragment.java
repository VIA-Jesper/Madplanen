package dk.ipfortify.madplanen.ui.shopping;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import dk.ipfortify.madplanen.R;
import dk.ipfortify.madplanen.ui.adapter.ShoppingIngredientsAdapter;

public class ShoppingFragment extends Fragment {

    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_shopping, container, false);

        // menu
        setHasOptionsMenu(true);
        // view
        mRecyclerView = v.findViewById(R.id.fragment_shopping_recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ShoppingFragmentViewModel mViewModel = new ViewModelProvider(this).get(ShoppingFragmentViewModel.class);

        mViewModel.getIngredients().observe(getViewLifecycleOwner(), ingredients -> {
            ShoppingIngredientsAdapter adapter = new ShoppingIngredientsAdapter(ingredients);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        });


    }
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item=menu.findItem(R.id.nav_shopping);
        if(item!=null)
            item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

}