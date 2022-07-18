package com.abn.food.recipe.service;

import static com.abn.food.recipe.enums.DishType.NON_VEGETARIAN;
import static com.abn.food.recipe.enums.DishType.VEGAN;
import static com.abn.food.recipe.enums.DishType.VEGETARIAN;
import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abn.food.recipe.enums.DishType;
import com.abn.food.recipe.mapper.FoodRecipeMapper;
import com.abn.food.recipe.model.FoodRecipe;
import com.abn.food.recipe.persistance.entity.FoodRecipeEntity;
import com.abn.food.recipe.persistance.entity.IngredientEntity;
import com.abn.food.recipe.persistance.entity.IngredientReferenceEntity;
import com.abn.food.recipe.persistance.repository.FoodRecipeRepository;

@ExtendWith(MockitoExtension.class)
class FoodRecipeServiceImplTest {

    @Mock private FoodRecipeMapper foodRecipeMapper;
    @Mock private FoodRecipeRepository foodRecipeRepository;

    @InjectMocks private FoodRecipeServiceImpl foodRecipeService;

    @Test
    void saveFoodRecipe() {
        FoodRecipe foodRecipeModel = getFoodRecipe();
        FoodRecipeEntity entity = new FoodRecipeEntity();
        when(foodRecipeRepository.save(any())).thenReturn(entity);
        when(foodRecipeMapper.formFoodRecipeModel(entity)).thenReturn(foodRecipeModel);

        FoodRecipe savedFoodRecipe = foodRecipeService.saveFoodRecipe(foodRecipeModel);
        verify(foodRecipeMapper).formFoodRecipeEntity(eq(savedFoodRecipe), any(), eq(null));
        assertEquals(VEGAN, savedFoodRecipe.getDishType());
        assertEquals(Integer.valueOf(4), savedFoodRecipe.getServings());
    }

    @Test
    void updateFoodRecipe() {
        FoodRecipe foodRecipeModel = getFoodRecipe();
        FoodRecipeEntity entity = new FoodRecipeEntity();

        when(foodRecipeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(foodRecipeRepository.save(any())).thenReturn(entity);
        when(foodRecipeMapper.formFoodRecipeModel(entity)).thenReturn(foodRecipeModel);

        FoodRecipe updatedFoodRecipe = foodRecipeService.updateFoodRecipe(foodRecipeModel, 1L);
        verify(foodRecipeMapper).formFoodRecipeEntity(updatedFoodRecipe, entity, 1L);
        assertEquals(VEGAN, updatedFoodRecipe.getDishType());
        assertEquals(Integer.valueOf(4), updatedFoodRecipe.getServings());
    }

    @Test
    void getIndividualFoodRecipe() {
        FoodRecipe foodRecipeModel = getFoodRecipe();
        FoodRecipeEntity entity = new FoodRecipeEntity();
        when(foodRecipeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(foodRecipeMapper.formFoodRecipeModel(entity)).thenReturn(foodRecipeModel);

        FoodRecipe foodRecipe = foodRecipeService.getIndividualFoodRecipe(1L);
        assertEquals(VEGAN, foodRecipe.getDishType());
        assertEquals(Integer.valueOf(4), foodRecipe.getServings());
    }

    @Test
    void removeFoodRecipe() {
        foodRecipeService.removeFoodRecipe(1L);
        verify(foodRecipeRepository).deleteById(1L);
    }

    @Test
    void getAllFoodRecipes() {
        when(foodRecipeRepository.findAll()).thenReturn(getFoodRecipeEntities());
        when(foodRecipeMapper.formFoodRecipeModel(any())).thenCallRealMethod();
        List<FoodRecipe> recipes = foodRecipeService.getAllFoodRecipes(null, null, emptySet(), emptySet(), emptySet());

        assertEquals(3, recipes.size());
        assertEquals(NON_VEGETARIAN, recipes.get(0).getDishType());
        assertEquals(Integer.valueOf(4), recipes.get(0).getServings());
        assertEquals(Integer.valueOf(500), recipes.get(1).getIngredients().get(1).getQuantity());
        assertEquals("gm", recipes.get(1).getIngredients().get(2).getUnit());
        assertEquals("2 eggs", recipes.get(1).getIngredientsWithQuantity().get(0));
        assertEquals("milk 500ml", recipes.get(0).getIngredientsWithQuantity().get(1));
    }

    @Test
    void getAllFoodRecipesWithVegetarian() {
        when(foodRecipeRepository.findAll()).thenReturn(getFoodRecipeEntities());
        when(foodRecipeMapper.formFoodRecipeModel(any())).thenCallRealMethod();
        List<FoodRecipe> recipes = foodRecipeService.getAllFoodRecipes(true, null, emptySet(), emptySet(), emptySet());

        assertEquals(2, recipes.size());
        assertEquals(VEGETARIAN, recipes.get(0).getDishType());
        assertEquals(Integer.valueOf(2), recipes.get(0).getServings());
        assertEquals(Integer.valueOf(500), recipes.get(0).getIngredients().get(1).getQuantity());
        assertEquals("gm", recipes.get(1).getIngredients().get(2).getUnit());
        assertEquals("2 eggs", recipes.get(1).getIngredientsWithQuantity().get(0));
        assertEquals("milk 500ml", recipes.get(0).getIngredientsWithQuantity().get(1));
    }

    @Test
    void getAllFoodRecipesWithIncludeAndExcludeIngredients() {
        when(foodRecipeRepository.findAll()).thenReturn(getFoodRecipeEntities());
        List<FoodRecipe> recipes = foodRecipeService.getAllFoodRecipes(true, null, Set.of("MILK"), Set.of("SUGAR"), emptySet());

        assertEquals(0, recipes.size());
    }

    @Test
    void getAllFoodRecipesWithExcludeIngredientsNotMatching() {
        when(foodRecipeRepository.findAll()).thenReturn(getFoodRecipeEntities());
        when(foodRecipeMapper.formFoodRecipeModel(any())).thenCallRealMethod();
        List<FoodRecipe> recipes = foodRecipeService.getAllFoodRecipes(true, 3, Set.of("MILK"), Set.of("SALT"), emptySet());

        assertEquals(1, recipes.size());
        assertEquals(VEGAN, recipes.get(0).getDishType());
        assertEquals(Integer.valueOf(3), recipes.get(0).getServings());
        assertEquals(Integer.valueOf(500), recipes.get(0).getIngredients().get(1).getQuantity());
        assertEquals("gm", recipes.get(0).getIngredients().get(2).getUnit());
        assertEquals("2 eggs", recipes.get(0).getIngredientsWithQuantity().get(0));
        assertEquals("milk 500ml", recipes.get(0).getIngredientsWithQuantity().get(1));
    }

    @Test
    void getAllFoodRecipesBySearchingInstructionsKeywords() {
        when(foodRecipeRepository.findAll()).thenReturn(getFoodRecipeEntities());
        when(foodRecipeMapper.formFoodRecipeModel(any())).thenCallRealMethod();
        List<FoodRecipe> recipes = foodRecipeService.getAllFoodRecipes(true, null, emptySet(), emptySet(), Set.of("OVEN"));

        assertEquals(1, recipes.size());
        assertEquals(VEGETARIAN, recipes.get(0).getDishType());
        assertEquals(Integer.valueOf(2), recipes.get(0).getServings());
        assertEquals(Integer.valueOf(500), recipes.get(0).getIngredients().get(1).getQuantity());
        assertEquals("gm", recipes.get(0).getIngredients().get(2).getUnit());
        assertEquals("2 eggs", recipes.get(0).getIngredientsWithQuantity().get(0));
        assertEquals("milk 500ml", recipes.get(0).getIngredientsWithQuantity().get(1));
    }

    private List<FoodRecipeEntity> getFoodRecipeEntities() {
        List<FoodRecipeEntity> foodRecipeEntities = new ArrayList<>();
        foodRecipeEntities.add(getFoodRecipeEntity("cook on Stove", 4, NON_VEGETARIAN));
        foodRecipeEntities.add(getFoodRecipeEntity("cook on Oven", 2, VEGETARIAN));
        foodRecipeEntities.add(getFoodRecipeEntity("cook under Sun", 3, VEGAN));
        return foodRecipeEntities;
    }

    private FoodRecipeEntity getFoodRecipeEntity(String instructions, Integer servings, DishType dishType) {
        FoodRecipeEntity recipeEntity = new FoodRecipeEntity();
        recipeEntity.setInstructions(instructions);
        recipeEntity.setServings(servings);
        recipeEntity.setDishType(dishType);
        recipeEntity.setIngredients(new ArrayList<>());
        recipeEntity.getIngredients().add(getIngredients("eggs", 2, null));
        recipeEntity.getIngredients().add(getIngredients("milk", 500, "ml"));
        recipeEntity.getIngredients().add(getIngredients("sugar", 100, "gm"));
        return recipeEntity;
    }

    private IngredientEntity getIngredients(String name, Integer quantity, String unit) {
        IngredientEntity entity = new IngredientEntity();
        entity.setQuantity(quantity);
        entity.setUnit(unit);
        entity.setReferenceEntity(new IngredientReferenceEntity());
        entity.getReferenceEntity().setName(name);
        return entity;
    }

    private FoodRecipe getFoodRecipe() {
        FoodRecipe foodRecipeModel = new FoodRecipe();
        foodRecipeModel.setDishType(VEGAN);
        foodRecipeModel.setServings(4);
        return foodRecipeModel;
    }
}