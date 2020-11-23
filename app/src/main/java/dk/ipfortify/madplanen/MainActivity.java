package dk.ipfortify.madplanen;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rhexgomez.typer.roboto.TyperRoboto;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@SuppressWarnings({"unused", "RedundantSuppression", "rawtypes"})
public class MainActivity extends AppCompatActivity  {

    private MaterialToolbar mTopToolBar;
    private BottomNavigationView mBottomNavigation;
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView mNavigationView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private ImageView mBackdropImageView;
    private TextView mLoggedInUserTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Auth
        mAuth = FirebaseAuth.getInstance();

        // appbarlayout
        mAppBarLayout = findViewById(R.id.appBarLayout);

        // topappbar
        mTopToolBar = findViewById(R.id.topAppBar);
        setSupportActionBar(mTopToolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // setup drawer
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById (R.id.nav_view);
        mLoggedInUserTextView = mNavigationView.getHeaderView(0).findViewById(R.id.loggedInUserTextView);

        updateAuthUi();



        // appbar config
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                // Nav considered 'home' with hamburger
                R.id.nav_favorites, R.id.nav_mealplan, R.id.nav_fridge)
                .setOpenableLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, navController);

        // collapsingtoolbarlayout
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setCollapsedTitleTypeface(TyperRoboto.ROBOTO_REGULAR());
        mCollapsingToolbarLayout.setExpandedTitleTypeface(TyperRoboto.ROBOTO_REGULAR());

        // backdrop image
        mBackdropImageView = findViewById(R.id.backdrop);



        //bottomnavigationview
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(mBottomNavigation, navController);


        // scroll behavior
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> destinationSettings(navController, destination));


        // style
        styleBottomBar();
    }

    @SuppressWarnings("unchecked")
    private void destinationSettings(NavController navController, NavDestination destination) {
        // set title when changing destination
        mCollapsingToolbarLayout.setTitle(Objects.requireNonNull(navController.getCurrentDestination()).getLabel());


        // if bottom appbar is not shown, reset and make sure its shown on page change.
        ViewGroup.LayoutParams layoutParams = mBottomNavigation.getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.Behavior coordinatorLayoutBehavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
            if (coordinatorLayoutBehavior instanceof HideBottomViewOnScrollBehavior) {
                HideBottomViewOnScrollBehavior<BottomNavigationView> behavior = (HideBottomViewOnScrollBehavior<BottomNavigationView>) coordinatorLayoutBehavior;
                behavior.slideUp(mBottomNavigation);
            }
        }
        // settings for different view can be changed here
        if (destination.getId() == R.id.nav_recipie) {
            mTopToolBar.setBackground(new ColorDrawable(Color.parseColor("#00000000")));
            lockAppBar(false, (String) destination.getLabel());
        } else {// set default behavior of appbarlayout expansion
            lockAppBar(true, (String) destination.getLabel());
        }
    }

    private void styleBottomBar() {
        float radius = getResources().getDimension(R.dimen.default_corner_radius);

        MaterialShapeDrawable materialShapeDrawable = (MaterialShapeDrawable)mBottomNavigation.getBackground();
        materialShapeDrawable.setShapeAppearanceModel(materialShapeDrawable.getShapeAppearanceModel()
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .build());
    }

    public void setTopAppBarTitle(String title) {
        mCollapsingToolbarLayout.setTitle(title);
    }

    private void lockAppBar(boolean isLocked, String title) {

        if (!isLocked) {
            mAppBarLayout.setExpanded(true, true);
            mAppBarLayout.setActivated(true);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
            lp.height = (int) getResources().getDimension(R.dimen.recipeImageHeight);
            mCollapsingToolbarLayout.setTitleEnabled(true);
            mCollapsingToolbarLayout.setTitle(title);
        } else {
            mAppBarLayout.setExpanded(false, true);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)mAppBarLayout.getLayoutParams();
            lp.height = (int) getResources().getDimension(R.dimen.appbar_height);
            mAppBarLayout.setLayoutParams(lp);
            mCollapsingToolbarLayout.setTitleEnabled(false);
            mTopToolBar.setTitle(title);
        }

    }

    public ImageView getBackdropImageView() {
        return mBackdropImageView;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    /*
            Login
     */
    public void logoutClick(MenuItem item) {
        signOut(item.getActionView());

    }

    public void loginClick(MenuItem item) {
        login(item.getActionView());
    }

    public void login(View view) {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.madplanen_logo)
                .setTheme(R.style.Base_Theme_MyApp)
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(signInIntent, 42);
    }

    public void signOut(View v) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> updateAuthUi());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 42) {
            handleSignInRequest(resultCode);
        }
    }

    private void handleSignInRequest(int resultCode) {
        if (resultCode == RESULT_OK) {
            updateAuthUi();
        }
    }

    private boolean isLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        return user != null;
    }

    private void updateAuthUi() {
        mNavigationView.getMenu().clear();
        if (isLoggedIn()) {
            mNavigationView.inflateMenu(R.menu.activity_main_drawer_logged_in);
            mLoggedInUserTextView.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
        } else {
            mNavigationView.inflateMenu(R.menu.activity_main_drawer_logged_out);
            mLoggedInUserTextView.setText("");
        }
    }



}