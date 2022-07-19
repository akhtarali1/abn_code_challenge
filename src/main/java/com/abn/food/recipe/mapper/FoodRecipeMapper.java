package com.abn.food.recipe.mapper;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static liquibase.repackaged.org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
     * @param recipeEntity           DB entity of Food recipe
     * @param isAdditionalDataRequired skip instructions mapping if false
     * @return mapped food recipe model
     */
    public FoodRecipe formFoodRecipeModel(FoodRecipeEntity recipeEntity, boolean isAdditionalDataRequired) {
        FoodRecipe foodRecipe = new FoodRecipe();
        foodRecipe.setDishType(recipeEntity.getDishType());
        foodRecipe.setServings(recipeEntity.getServings());
        formIngredientsModel(foodRecipe, recipeEntity, isAdditionalDataRequired);
        updateLink(foodRecipe, recipeEntity.getId());
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
        List<IngredientEntity> ingredientEntities = formIngredientsEntityFromModel(recipe.getIngredients(), entity);
        entity.getIngredients().addAll(ingredientEntities);
    }

    private void formIngredientsModel(FoodRecipe foodRecipe, FoodRecipeEntity recipeEntity, boolean isAdditionalDataRequired) {
        List<Ingredient> ingredients = new ArrayList<>();
        List<String> ingredientsWithQuantity = new ArrayList<>();
        recipeEntity.getIngredients().forEach(entity -> {
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
        if (isAdditionalDataRequired) {
            foodRecipe.setIngredients(ingredients);
            foodRecipe.setInstructions(recipeEntity.getInstructions());
        }
    }

    private List<IngredientEntity> formIngredientsEntityFromModel(List<Ingredient> ingredientNames, FoodRecipeEntity foodRecipeEntity) {
        // TO collect existing Ingredients in DB to update them.
        Map<String, IngredientEntity> existingIngredientEntityMap = ofNullable(foodRecipeEntity.getIngredients())
            .orElse(emptyList())
            .stream().collect(toMap(entity -> entity.getReferenceEntity().getName(), Function.identity()));
        //If Food recipe entity already exists in DB then ingredients should be cleared from persistence cache as we are overriding all
        if (isNotEmpty(foodRecipeEntity.getIngredients())) {
            foodRecipeEntity.getIngredients().clear();
        } else {
            foodRecipeEntity.setIngredients(new ArrayList<>());
        }

        return ingredientNames.stream()
            .map(ingredient -> formIngredientEntity(ingredient, foodRecipeEntity, existingIngredientEntityMap.get(ingredient.getName())))
            .collect(toList());
    }

    private IngredientEntity formIngredientEntity(Ingredient ingredient, FoodRecipeEntity foodRecipeEntity, IngredientEntity ingredientEntity) {
        IngredientReferenceEntity referenceEntity = ingredientReferenceRepository.findByNameEqualsIgnoreCase(ingredient.getName())
            .orElseGet(() -> ingredientReferenceRepository.save(formIngredientReferenceEntity(ingredient.getName())));

        IngredientEntity entity = ofNullable(ingredientEntity).orElseGet(IngredientEntity::new);
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

    private void updateLink(FoodRecipe foodRecipe, Long id) {
        Link link = linkTo(methodOn(FoodRecipeController.class)
            .getFoodRecipe(id))
            .withSelfRel();
        foodRecipe.add(link);
    }
}
