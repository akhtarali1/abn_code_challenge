package com.abn.food.recipe.mapper;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.abn.food.recipe.controller.FoodRecipeController;
import com.abn.food.recipe.model.FoodRecipe;
import com.abn.food.recipe.model.Ingredient;
import com.abn.food.recipe.persistance.entity.FoodRecipeEntity;
import com.abn.food.recipe.persistance.entity.IngredientEntity;
import com.abn.food.recipe.persistance.entity.IngredientReferenceEntity;
import com.abn.food.recipe.persistance.repository.IngredientReferenceRepository;

/**
 * Food Recipe Mapper
 *
 * @author Akhtar
 */
@Component
public class FoodRecipeMapper {

    private static final String SPACE = " ";

    private final IngredientReferenceRepository ingredientReferenceRepository;

    /**
     * Constructor initialization
     *
     * @param ingredientReferenceRepository ingredient reference repository
     */
    public FoodRecipeMapper(IngredientReferenceRepository ingredientReferenceRepository) {
        this.ingredientReferenceRepository = ingredientReferenceRepository;
    }

    /**
     * Map Food recipe model from entity
     *
     * @param recipeEntity DB entity of Food recipe
     * @return mapped food recipe model
     */
    public FoodRecipe formFoodRecipeModel(FoodRecipeEntity recipeEntity) {
        FoodRecipe foodRecipe = new FoodRecipe();
        foodRecipe.setId(recipeEntity.getId());
        foodRecipe.setDishType(recipeEntity.getDishType());
        foodRecipe.setInstructions(recipeEntity.getInstructions());
        foodRecipe.setServings(recipeEntity.getServings());
        formIngredientsModel(foodRecipe, recipeEntity.getIngredients());
        updateLink(foodRecipe);
        return foodRecipe;
    }

    /**
     * Map Food recipe entity from Model
     *
     * @param recipe Food recipe model to mapped to entity
     * @param entity Food recipe entity to be updated with model values
     * @param id     of the entity for persisting in repository
     */
    public void formFoodRecipeEntity(FoodRecipe recipe, FoodRecipeEntity entity, Long id) {
        entity.setId(id);
        entity.setDishType(recipe.getDishType());
        entity.setInstructions(recipe.getInstructions());
        entity.setServings(recipe.getServings());
        entity.setIngredients(formIngredientsEntityFromModel(recipe.getIngredients(), entity));
    }

    private void formIngredientsModel(FoodRecipe foodRecipe, List<IngredientEntity> ingredientEntities) {
        List<Ingredient> ingredients = new ArrayList<>();
        List<String> ingredientsWithQuantity = new ArrayList<>();
        ingredientEntities.forEach(entity -> {
            String ingredientName = entity.getReferenceEntity().getName();
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientName);
            ingredient.setQuantity(entity.getQuantity());
            ingredient.setUnit(entity.getUnit());
            ingredients.add(ingredient);

            String ingredientWithQuantity;
            if (StringUtils.isNotEmpty(entity.getUnit())) {
                ingredientWithQuantity = ingredientName + SPACE + entity.getQuantity() + entity.getUnit();
            }
            else {
                ingredientWithQuantity = entity.getQuantity() + SPACE + ingredientName;
            }
            ingredientsWithQuantity.add(ingredientWithQuantity);
        });

        foodRecipe.setIngredientsWithQuantity(ingredientsWithQuantity);
        foodRecipe.setIngredients(ingredients);
    }

    private List<IngredientEntity> formIngredientsEntityFromModel(List<Ingredient> ingredientNames, FoodRecipeEntity foodRecipeEntity) {
        return ingredientNames.stream()
            .map(ingredient -> formIngredientEntity(ingredient, foodRecipeEntity))
            .collect(toList());
    }

    private IngredientEntity formIngredientEntity(Ingredient ingredient, FoodRecipeEntity foodRecipeEntity) {
        IngredientReferenceEntity referenceEntity = ingredientReferenceRepository.findByNameEqualsIgnoreCase(ingredient.getName())
            .orElseGet(() -> ingredientReferenceRepository.save(formIngredientReferenceEntity(ingredient.getName())));

        IngredientEntity entity = new IngredientEntity();
        entity.setQuantity(ingredient.getQuantity());
        entity.setUnit(ingredient.getUnit());
        entity.setReferenceEntity(referenceEntity);
        entity.setFoodRecipe(foodRecipeEntity);
        return entity;
    }

    private IngredientReferenceEntity formIngredientReferenceEntity(String name) {
        IngredientReferenceEntity referenceEntity = new IngredientReferenceEntity();
        referenceEntity.setName(name);
        return referenceEntity;
    }

    private void updateLink(FoodRecipe foodRecipe) {
        Link link = linkTo(methodOn(FoodRecipeController.class)
            .getFoodRecipe(foodRecipe.getId()))
            .withSelfRel();
        foodRecipe.add(link);
    }
}
