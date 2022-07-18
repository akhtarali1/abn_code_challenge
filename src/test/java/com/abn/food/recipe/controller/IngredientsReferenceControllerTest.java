package com.abn.food.recipe.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.abn.food.recipe.persistance.entity.IngredientReferenceEntity;
import com.abn.food.recipe.persistance.repository.IngredientReferenceRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IngredientsReferenceController.class)
class IngredientsReferenceControllerTest {

    private static final String URL = "/abn/kitchen/reference/ingredients";

    @Autowired private MockMvc mockMvc;
    @MockBean private IngredientReferenceRepository referenceRepository;

    @Test
    void getAllReferenceIngredients() throws Exception {
        IngredientReferenceEntity reference = new IngredientReferenceEntity();
        reference.setName("egg");
        given(referenceRepository.findAll()).willReturn(List.of(reference));
        mockMvc.perform(get(URL)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0]").value("egg"));
    }
}