package dk.ipfortify.madplanen.data.repository;

import android.app.Application;
import android.content.Context;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.ipfortify.madplanen.data.model.madplanen.Recipe;
import dk.ipfortify.madplanen.data.repository.local.dao.RecipeLocalDataSource;
import dk.ipfortify.madplanen.data.repository.local.database.RecipeDatabase;
import dk.ipfortify.madplanen.data.repository.local.database.RecipeDatabase_Impl;
import dk.ipfortify.madplanen.data.repository.remote.api.RecipeRemoteDataSourceNemlig;
import dk.ipfortify.madplanen.data.repository.remote.api.iRecipeRemoteDataSourceNemlig;
import kotlinx.coroutines.Dispatchers;
import util.CurrentThreadExecutor;

/*
        TEST CASES DISPLAYING THE USAGE OF MOCKITO

 */


@RunWith(MockitoJUnitRunner.class)
public class RecipeRepositoryMockitoTest {


    RecipeLocalDataSource mRecipeLocalDataSource;

    RecipeRemoteDataSourceNemlig mRecipeRemoteDataSource;

    RecipeRepository repository;

    @Before
    public void setUp() {

        mRecipeLocalDataSource = Mockito.mock(RecipeLocalDataSource.class);
        mRecipeRemoteDataSource = Mockito.mock(RecipeRemoteDataSourceNemlig.class);

        repository = new RecipeRepository(mRecipeLocalDataSource, mRecipeRemoteDataSource, new CurrentThreadExecutor());

        MockitoAnnotations.initMocks(this);

    }


    @Test
    public void listener_add_AddedToLocalDataSource() {
        // Arrange
        Recipe r = new Recipe();
        r.name = "Test recipe";

        // Act
        repository.listener_recipe_add(r);

        // Assert
        Mockito.verify(mRecipeLocalDataSource).insert(r);

    }



    @Test
    public void recipe_update_GetRecipeDetails_LocalStorageCheck() {

        // Arrange
        String recipeId = "recipeId1";
        Recipe r1 = new Recipe();
        r1.name = "Test Recipe";
        Mockito.when(mRecipeLocalDataSource.getRecipeByUrlId(recipeId)).thenReturn(new Recipe());

        // Act
        repository.recipe_update(recipeId);

        // Assert
        Mockito.verify(mRecipeLocalDataSource).getRecipeByUrlId(recipeId);


    }

    @Test
    public void listener_recipe_add_TryToGetDetailedRecipes() {

        // Arrange
        String mealPlanName = "TestPlan";
        Recipe r1 = new Recipe();
        r1.url = "url 1";
        Recipe r2 = new Recipe();
        r2.url = "url 2";
        Recipe r3 = new Recipe();
        r3.url = "url 3";
        List<Recipe> recipeList = new ArrayList<>(Arrays.asList(r1, r2, r3));

        // Act
        repository.listener_recipe_add(mealPlanName, recipeList);

        // Assert
        Mockito.verify(mRecipeLocalDataSource).getRecipeByUrlId(r1.url);
        Mockito.verify(mRecipeLocalDataSource).getRecipeByUrlId(r2.url);
        Mockito.verify(mRecipeLocalDataSource).getRecipeByUrlId(r3.url);

    }


    @Test
    public void recipe_update_GetRecipeDetails_RemoteStorageCheck() {
        // Arrange
        String recipeId = "recipeId1";
        Recipe r1 = new Recipe();
        r1.name = "Test Recipe";
        Mockito.when(mRecipeLocalDataSource.getRecipeByUrlId(recipeId)).thenReturn(null);

        // Act
        repository.recipe_update(recipeId);

        // Assert
        Mockito.verify(mRecipeRemoteDataSource).getRecipeByUrlId(recipeId, repository);

    }


    @Test
    public void mealPlan_update_callDaoString() {

        // Arrange
        String mealPlanName = "mealplan1";
        // Act
        repository.mealPlan_update(mealPlanName);
        // Assert
        Mockito.verify(mRecipeRemoteDataSource).getMealPlan(repository, mealPlanName);
    }

    @Test
    public void mealPlan_get_NotNull() {
        // Arrange
        String mealPlanName = "mealPlan1";
        // Act
        // Assert
        Assert.assertNotNull(repository.mealPlan_get(mealPlanName));
    }


    @Test
    public void fav_update_GetAllFavorites() {

        // Arrange

        // Act
        repository.fav_update();
        // Assert
        Mockito.verify(mRecipeLocalDataSource).getRecipesWithFavoriteFlagSetTo(true);

    }


    @Test
    public void fav_addToFavoriteList_UrlFoundLocalSource() {

        // Arrange
        Recipe r1 = new Recipe();
        r1.recipeId = 100;
        r1.name = "Recipe 1";
        r1.url = "url 1";
        r1.isFavorite = true;

        Mockito.when(mRecipeLocalDataSource.getRecipeByUrlId(r1.url)).thenReturn(r1);

        // Act
        repository.fav_addToFavoriteList(r1);

        // Assert
        Mockito.verify(mRecipeLocalDataSource).getRecipeByUrlId(r1.url);
        Mockito.verify(mRecipeLocalDataSource).updateRecipeFavorite(r1.recipeId, r1.isFavorite);
        Mockito.verifyZeroInteractions(mRecipeRemoteDataSource);
    }

    @Test
    public void fav_addToFavoriteList_UrlFoundRemoteSource() {

        // Arrange
        Recipe r1 = new Recipe();
        r1.recipeId = 100;
        r1.name = "Recipe 1";
        r1.url = "url 1";
        r1.isFavorite = true;
        r1.isShoppingList = false;

        Mockito.when(mRecipeLocalDataSource.getRecipeByUrlId(r1.url)).thenReturn(null);

        // Act
        repository.fav_addToFavoriteList(r1);

        // Assert
        Mockito.verify(mRecipeLocalDataSource, Mockito.times(1)).getRecipeByUrlId(r1.url);
        Mockito.verify((RecipeRemoteDataSourceNemlig)mRecipeRemoteDataSource).getRecipeByUrlId(r1.url, repository, r1.isFavorite, r1.isShoppingList);
    }


    @Test
    public void fav_addToShoppingList_UrlFoundLocalSource() {

        // Arrange
        Recipe r1 = new Recipe();
        r1.recipeId = 100;
        r1.name = "Recipe 1";
        r1.url = "url 1";
        r1.isShoppingList = true;

        Mockito.when(mRecipeLocalDataSource.getRecipeByUrlId(r1.url)).thenReturn(r1);

        // Act
        repository.fav_addToShoppingList(r1);

        // Assert
        Mockito.verify(mRecipeLocalDataSource).getRecipeByUrlId(r1.url);
        Mockito.verify(mRecipeLocalDataSource).updateRecipeShoppingList(r1.recipeId, r1.isShoppingList);
        Mockito.verifyZeroInteractions(mRecipeRemoteDataSource);
    }

    @Test
    public void fav_addToShoppingList_UrlFoundRemoteSource() {

        // Arrange
        Recipe r1 = new Recipe();
        r1.recipeId = 100;
        r1.name = "Recipe 1";
        r1.url = "url 1";
        r1.isShoppingList = true;

        Mockito.when(mRecipeLocalDataSource.getRecipeByUrlId(r1.url)).thenReturn(null);

        // Act
        repository.fav_addToShoppingList(r1);

        // Assert
        Mockito.verify(mRecipeLocalDataSource, Mockito.times(1)).getRecipeByUrlId(r1.url);
        Mockito.verify((RecipeRemoteDataSourceNemlig)mRecipeRemoteDataSource).getRecipeByUrlId(r1.url, repository, r1.isFavorite, r1.isShoppingList);

    }

    @Test
    public void SingletonTest() {

        // Arrange

        // Act
        RecipeRepository r = RecipeRepository.getInstance(Mockito.mock(Application.class));
        // Assert
        Assert.assertNotNull(r);
    }



}