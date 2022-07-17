package com.abn.food.recipe.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abn.food.recipe.persistance.entity.FoodRecipeEntity;

/**
 * Food Recipe Repository
 *
 * @author Akhtar
 */
@Repository
public interface FoodRecipeRepository extends JpaRepository<FoodRecipeEntity, Long> {
}
