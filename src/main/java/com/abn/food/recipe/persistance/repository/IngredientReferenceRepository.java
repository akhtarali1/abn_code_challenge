package com.abn.food.recipe.persistance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abn.food.recipe.persistance.entity.IngredientReferenceEntity;

/**
 * Ingredient Reference Repository
 *
 * @author Akhtar
 */
@Repository
public interface IngredientReferenceRepository extends JpaRepository<IngredientReferenceEntity, Long> {

    /**
     * Fetch matched Ingredient Reference
     *
     * @param name Ingredient name
     * @return matched Ingredient reference
     */
    Optional<IngredientReferenceEntity> findByNameEqualsIgnoreCase(String name);
}
