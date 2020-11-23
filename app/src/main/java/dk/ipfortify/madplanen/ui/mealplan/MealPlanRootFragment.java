package dk.ipfortify.madplanen.ui.mealplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import dk.ipfortify.madplanen.R;
import dk.ipfortify.madplanen.data.model.nemlig.MealplanTypes;
import dk.ipfortify.madplanen.ui.adapter.ViewPagerAdapterMealplan;

public class MealPlanRootFragment extends Fragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public static SharedPreferences mSetupSharedPreferences;
    public static final String CURRENT_TAB = "currentTab";
    private static int mCurrentTab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mealplan_root, container, false);
        mViewPager = v.findViewById(R.id.fragment_mealplan_root_viewpager);
        mTabLayout = v.findViewById(R.id.fragment_mealplan_root_tabLayout);


        // setup shared pref to save current tab
        mSetupSharedPreferences = requireActivity().getSharedPreferences(CURRENT_TAB, Context.MODE_PRIVATE);

        if (! mSetupSharedPreferences.contains(CURRENT_TAB)) {
            SharedPreferences.Editor editor = mSetupSharedPreferences.edit();
            editor.putInt(CURRENT_TAB, 0);
            editor.apply();
        } else {
            mCurrentTab = mSetupSharedPreferences.getInt(CURRENT_TAB, 0);
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ViewPagerAdapterMealplan mAdapter = new ViewPagerAdapterMealplan(getChildFragmentManager());
        for (int i = 0; i < MealplanTypes.mealplanTypes.size(); i++) {
            Bundle b = new Bundle();
            b.putString("fragmentName", MealplanTypes.mealplanTypes.get(i));
            MealPlanFragment fragment = new MealPlanFragment();
            fragment.setArguments(b);
            mAdapter.addFragment(fragment, MealplanTypes.mealplanTypes.get(i));
        }

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(mCurrentTab);

        // save tabs
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mCurrentTab = tab.getPosition();
                SharedPreferences.Editor editor = mSetupSharedPreferences.edit();
                editor.putInt(CURRENT_TAB, mCurrentTab);
                editor.apply();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }



}