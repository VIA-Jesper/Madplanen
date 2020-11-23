package dk.ipfortify.madplanen.ui.recipe;


import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import dk.ipfortify.madplanen.MainActivity;
import dk.ipfortify.madplanen.R;
import dk.ipfortify.madplanen.data.model.madplanen.RecipeInstruction;
import dk.ipfortify.madplanen.ui.adapter.RecipeIngredientsAdapter;

public class RecipeFragment extends Fragment {

    private String linkUrl;
    
    private TextView mHeaderTextView;
    private TextView mTotalTimeTextView;
    private TextView mWorkTimeTextView;
    private TextView mInstructionsTextView;
    RecipeIngredientsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linkUrl = requireArguments().getString("link_url");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_recipe, container, false);
        RecipeViewModel mViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        mViewModel.update(linkUrl);


        mHeaderTextView = v.findViewById(R.id.recipefragment_header);
        mTotalTimeTextView = v.findViewById(R.id.recipefragment_totaltime);
        mWorkTimeTextView = v.findViewById(R.id.recipefragment_worktime);
        RecyclerView mRecyclerviewIngredients = v.findViewById(R.id.recipefragment_ingredientsListView);
        mRecyclerviewIngredients.setNestedScrollingEnabled(false);

        mInstructionsTextView = v.findViewById(R.id.recipefragment_instructionListView);

        mAdapter = new RecipeIngredientsAdapter();
        mRecyclerviewIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerviewIngredients.setAdapter(mAdapter);

        mViewModel.getRecipe().observe(getViewLifecycleOwner(), content -> {
            if (content != null) {
                mAdapter.clearItems();
                mAdapter.addItems(content.ingredients);

                loadBackdrop(content.imageUrl);

                ((MainActivity) requireActivity()).setTopAppBarTitle(content.name);


                mHeaderTextView.setText(content.name);
                mTotalTimeTextView.setText(content.totalWorkTime);
                mWorkTimeTextView.setText(content.totalPrepareTime);

                StringBuilder instruction = new StringBuilder();
                for (RecipeInstruction instruct : content.instructions) {
                    instruction.append(instruct.step);
                }
                mInstructionsTextView.setText(Html.fromHtml(instruction.toString(),
                        Html.FROM_HTML_MODE_COMPACT|
                                Html.FROM_HTML_OPTION_USE_CSS_COLORS|
                                Html.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING|
                                Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST|
                                Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM|
                                Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH|
                                Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE|
                                Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL
                ));
            }
        });



        return v;
    }

    private void loadBackdrop(String imageUrl) {
        ImageView backdrop = ((MainActivity) requireActivity()).getBackdropImageView();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(null)
                .error(null);

        Glide.with(requireView())
                .load(imageUrl)
                .apply(options)
                .into(backdrop);
    }




    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }


}


