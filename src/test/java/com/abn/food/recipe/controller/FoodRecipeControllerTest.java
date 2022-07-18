package com.abn.food.recipe.controller;

import static com.abn.food.recipe.enums.DishType.NON_VEGETARIAN;
import static java.util.Collections.emptySet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.abn.food.recipe.model.FoodRecipe;
import com.abn.food.recipe.model.Ingredient;
import com.abn.food.recipe.service.FoodRecipeServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FoodRecipeController.class)
class FoodRecipeControllerTest {

    private static final String URL = "/abn/kitchen/foodRecipe";

    @Autowired private MockMvc mockMvc;
    @MockBean private FoodRecipeServiceImpl foodRecipeService;

    @Test
    void postFoodRecipe() throws Exception {
        given(foodRecipeService.saveFoodRecipe(any())).willReturn(foodRecipe());
        mockMvc.perform(post(URL)
                .contentType(APPLICATION_JSON)
                .content("{\"dishType\":\"NON_VEGETARIAN\",\"servings\":2," +
                    "\"ingredients\":[{\"name\":\"onion\",\"quantity\":100,\"unit\":\"gm\"},{\"name\":\"eggs\",\"quantity\":2}]," +
                    "\"instructions\":\"Beat in oven for 2mins & eat. Don't ask me how to cook\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.servings").value(4))
            .andExpect(jsonPath("$.dishType").value(NON_VEGETARIAN.name()))
            .andExpect(jsonPath("$.instructions").value("Cook Yourself"))
            .andExpect(jsonPath("$.ingredients").isArray());

    }

    @Test
    void putFoodRecipe() throws Exception {
        given(foodRecipeService.updateFoodRecipe(any(), eq(1L))).willReturn(foodRecipe());
        mockMvc.perform(put(URL + "/1")
                .contentType(APPLICATION_JSON)
                .content("{\"dishType\":\"NON_VEGETARIAN\",\"servings\":2," +
                    "\"ingredients\":[{\"name\":\"onion\",\"quantity\":100,\"unit\":\"gm\"},{\"name\":\"eggs\",\"quantity\":2}]," +
                    "\"instructions\":\"Beat in oven for 2mins & eat. Don't ask me how to cook\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.servings").value(4))
            .andExpect(jsonPath("$.dishType").value(NON_VEGETARIAN.name()))
            .andExpect(jsonPath("$.instructions").value("Cook Yourself"))
            .andExpect(jsonPath("$.ingredients").isArray());
    }

    @Test
    void getFoodRecipes() throws Exception {
        given(foodRecipeService.getAllFoodRecipes(null, null, emptySet(), emptySet(), emptySet())).willReturn(List.of(foodRecipe()));
        mockMvc.perform(get(URL)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].servings").value(4))
            .andExpect(jsonPath("$.[0].dishType").value(NON_VEGETARIAN.name()))
            .andExpect(jsonPath("$.[0].instructions").value("Cook Yourself"))
            .andExpect(jsonPath("$.[0].ingredients").isArray());
    }

    @Test
    void getFoodRecipesWithQueries() throws Exception {
        given(foodRecipeService.getAllFoodRecipes(true, 4, emptySet(), emptySet(), emptySet())).willReturn(List.of(foodRecipe()));
        mockMvc.perform(get(URL)
                .contentType(APPLICATION_JSON)
                .param("isVegetarian", "true")
                .param("numberOfServings", "4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].servings").value(4))
            .andExpect(jsonPath("$.[0].dishType").value(NON_VEGETARIAN.name()))
            .andExpect(jsonPath("$.[0].instructions").value("Cook Yourself"))
            .andExpect(jsonPath("$.[0].ingredients").isArray());
    }

    @Test
    void getFoodRecipesWithIngredientsSearch() throws Exception {
        given(foodRecipeService.getAllFoodRecipes(eq(null), eq(4), eq(emptySet()), eq(emptySet()), anySet())).willReturn(List.of(foodRecipe()));
        mockMvc.perform(get(URL)
                .contentType(APPLICATION_JSON)
                .param("instructions", "oven")
                .param("numberOfServings", "4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].servings").value(4))
            .andExpect(jsonPath("$.[0].dishType").value(NON_VEGETARIAN.name()))
            .andExpect(jsonPath("$.[0].instructions").value("Cook Yourself"))
            .andExpect(jsonPath("$.[0].ingredients").isArray());
    }

    @Test
    void getFoodRecipe() throws Exception {
        given(foodRecipeService.getIndividualFoodRecipe(1L)).willReturn(foodRecipe());
        mockMvc.perform(get(URL + "/1")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.servings").value(4))
            .andExpect(jsonPath("$.dishType").value(NON_VEGETARIAN.name()))
            .andExpect(jsonPath("$.instructions").value("Cook Yourself"))
            .andExpect(jsonPath("$.ingredients").isArray());
    }

    @Test
    void deleteFoodRecipe() throws Exception {
        mockMvc.perform(delete(URL + "/1")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    private FoodRecipe foodRecipe() {
        FoodRecipe foodRecipe = new FoodRecipe();
        foodRecipe.setId(1L);
        foodRecipe.setServings(4);
        foodRecipe.setDishType(NON_VEGETARIAN);
        foodRecipe.setInstructions("Cook Yourself");
        foodRecipe.setIngredients(List.of(new Ingredient()));
        return foodRecipe;
    }
}