package com.abn.food.recipe;

import static com.abn.food.recipe.enums.DishType.NON_VEGETARIAN;
import static com.abn.food.recipe.enums.DishType.VEGAN;
import static com.abn.food.recipe.enums.DishType.VEGETARIAN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * Integration Test with various use-cases executed in order all possible operations
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class AbnRecipeApplicationIntegrationTests {

    private static final String URL = "/abn/kitchen/foodRecipe";
    private static final String REFERENCE_URL = "/abn/kitchen/reference/ingredients";

    @Autowired private MockMvc mvc;

    @Test
    @Order(1)
    void postFirstFoodRecipe() throws Exception {
        mvc.perform(post(URL)
                .contentType(APPLICATION_JSON)
                .content(convertJsonToStringFromFile("egg_omelette.json")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.recipeName").value("Egg Omelette"))
            .andExpect(jsonPath("$.servings").value(2))
            .andExpect(jsonPath("$.dishType").value(NON_VEGETARIAN.name()))
            .andExpect(jsonPath("$.instructions").hasJsonPath())
            .andExpect(jsonPath("$.ingredients").isArray());

        mvc.perform(get(REFERENCE_URL)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[1]").value("eggs"));
    }

    @Test
    @Order(2)
    void postMoreFoodRecipes() throws Exception {
        mvc.perform(post(URL)
                .contentType(APPLICATION_JSON)
                .content(convertJsonToStringFromFile("biryani.json")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.servings").value(3))
            .andExpect(jsonPath("$.dishType").value(NON_VEGETARIAN.name()))
            .andExpect(jsonPath("$.instructions").value("On Stove Fry chicken & Onion in oil. Then Add rice and water. Mix well and cook for 20mins on low flame"))
            .andExpect(jsonPath("$.ingredients").isArray());

        mvc.perform(get(REFERENCE_URL)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[2]").value("rice"))
            .andExpect(jsonPath("$.[4]").value("oil"));

        mvc.perform(get(URL)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[1].servings").value(3))
            .andExpect(jsonPath("$.[0].dishType").value(NON_VEGETARIAN.name()))
            .andExpect(jsonPath("$.[1].instructions").doesNotHaveJsonPath())
            .andExpect(jsonPath("$.[1].ingredients").doesNotHaveJsonPath());
    }

    @Test
    @Order(3)
    void postVegetarianFoodRecipe() throws Exception {
        mvc.perform(post(URL)
                .contentType(APPLICATION_JSON)
                .content(convertJsonToStringFromFile("potato_fry.json")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.servings").value(5))
            .andExpect(jsonPath("$.dishType").value(VEGETARIAN.name()))
            .andExpect(jsonPath("$.instructions").hasJsonPath())
            .andExpect(jsonPath("$._links.self.href").hasJsonPath());

        mvc.perform(get(REFERENCE_URL)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[2]").value("rice"))
            .andExpect(jsonPath("$.[5]").value("potato"));

        mvc.perform(get(URL)
                .contentType(APPLICATION_JSON)
                .param("instructions", "oven"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[2]").doesNotHaveJsonPath())
            .andExpect(jsonPath("$.[1].servings").value(5))
            .andExpect(jsonPath("$.[1].dishType").value(VEGETARIAN.name()))
            .andExpect(jsonPath("$.[1].instructions").doesNotHaveJsonPath())
            .andExpect(jsonPath("$.[1].ingredientsWithQuantity.[2]").value("chilli powder 20gm"));
    }

    @Test
    @Order(4)
    void postVeganFoodRecipe() throws Exception {
        mvc.perform(post(URL)
                .contentType(APPLICATION_JSON)
                .content(convertJsonToStringFromFile("poached rhubarb.json")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.servings").value(12))
            .andExpect(jsonPath("$.dishType").value(VEGAN.name()))
            .andExpect(jsonPath("$.instructions").hasJsonPath())
            .andExpect(jsonPath("$._links.self.href").hasJsonPath());

        mvc.perform(get(REFERENCE_URL)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[6]").value("mustard"))
            .andExpect(jsonPath("$.[8]").value("rhubarb"));

        mvc.perform(get(URL)
                .contentType(APPLICATION_JSON)
                .param("isVegetarian", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[2]").doesNotHaveJsonPath())
            .andExpect(jsonPath("$.[1].servings").value(12))
            .andExpect(jsonPath("$.[1].dishType").value(VEGAN.name()))
            .andExpect(jsonPath("$.[1].instructions").doesNotHaveJsonPath())
            .andExpect(jsonPath("$.[1].ingredientsWithQuantity.[3]").value("fresh thyme 1bunch"));
    }

    @Test
    @Order(5)
    void getFoodRecipesWithIncludeExcludeFilter() throws Exception {
        mvc.perform(get(URL)
                .contentType(APPLICATION_JSON)
                .param("includeIngredients", "sugar")
                .param("excludeIngredients", "oil"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[1]").doesNotHaveJsonPath())
            .andExpect(jsonPath("$.[0].servings").value(12))
            .andExpect(jsonPath("$.[0].dishType").value(VEGAN.name()))
            .andExpect(jsonPath("$.[0].instructions").doesNotHaveJsonPath())
            .andExpect(jsonPath("$.[0].ingredientsWithQuantity.[2]").value("sugar 300gm"));
    }

    @Test
    @Order(6)
    void deleteAndGetFoodRecipes() throws Exception {
        mvc.perform(delete(URL + "/2")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent());

        mvc.perform(get(URL)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[3]").doesNotHaveJsonPath())
            .andExpect(jsonPath("$.[2].links.[0].href").hasJsonPath());

        mvc.perform(get(URL)
                .contentType(APPLICATION_JSON)
                .param("includeIngredients", "rice"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Order(7)
    void putFirstFoodRecipe() throws Exception {
        mvc.perform(put(URL + "/1")
                .contentType(APPLICATION_JSON)
                .content(convertJsonToStringFromFile("egg_omelette_update.json")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.servings").value(2))
            .andExpect(jsonPath("$.dishType").value(NON_VEGETARIAN.name()))
            .andExpect(jsonPath("$.instructions").hasJsonPath())
            .andExpect(jsonPath("$.ingredients.[2]").doesNotHaveJsonPath());

        mvc.perform(get(REFERENCE_URL)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0]").value("onion"));

        mvc.perform(get(URL + "/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.servings").value(2))
            .andExpect(jsonPath("$.dishType").value(NON_VEGETARIAN.name()))
            .andExpect(jsonPath("$.instructions").hasJsonPath())
            .andExpect(jsonPath("$.ingredients").isArray());
    }

    private String convertJsonToStringFromFile(String fileName) {
        try {
            return Resources.toString(Resources.getResource(fileName), Charsets.UTF_8);
        }
        catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
