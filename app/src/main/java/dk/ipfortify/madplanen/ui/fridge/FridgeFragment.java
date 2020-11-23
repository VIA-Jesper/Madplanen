package dk.ipfortify.madplanen.ui.fridge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import dk.ipfortify.madplanen.R;
import dk.ipfortify.madplanen.util.SwipeHelper;
import dk.ipfortify.madplanen.ui.adapter.FridgeContentAdapter;


@SuppressWarnings({"unused", "RedundantSuppression"})
public class FridgeFragment extends Fragment {

    private FridgeViewModel mViewModel;
    private RecyclerView mRecyclerView;
    FridgeContentAdapter mAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_fridge, container, false);


        //menu
        setHasOptionsMenu(true);
        // view
        mRecyclerView = v.findViewById(R.id.fragment_fridge_recycleView);
        mAdapter = new FridgeContentAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // listeners
        v.findViewById(R.id.fragment_fridge_add_contentButton).setOnClickListener(content ->
                Navigation.findNavController(content).navigate(R.id.action_nav_fridge_to_fridgeAddContentFragment));

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FridgeViewModel.class);
        mViewModel.getAllIngredients().observe(getViewLifecycleOwner(), ingredients -> {
            mAdapter.clearItems();
            mAdapter.addItems(ingredients);

        });

        SwipeHelper sh = new SwipeHelper(getContext(), mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                    underlayButtons.add(new UnderlayButton(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_delete_24), ContextCompat.getColor(requireContext(), R.color.goldpalette_500), pos -> mViewModel.deleteItem(Objects.requireNonNull(mViewModel.getAllIngredients().getValue()).get(pos))));
                underlayButtons.add(new UnderlayButton( AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_edit_24), ContextCompat.getColor(requireContext(), R.color.goldpalette_500), pos -> {

                    Bundle b = new Bundle();
                    b.putSerializable("ingredient", Objects.requireNonNull(mViewModel.getAllIngredients().getValue()).get(pos));
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_nav_fridge_to_fridgeAddContentFragment, b);
                }));
            }
        };
        sh.attachSwipe();
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_fridge_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.fragment_fridge_menu_delete_all) {
            mViewModel.deleteAll();
        }
        return super.onOptionsItemSelected(item);
    }



}