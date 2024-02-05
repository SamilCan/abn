package com.abn.recipe.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.abn.recipe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecipeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipeDTO.class);
        RecipeDTO recipeDTO1 = new RecipeDTO();
        recipeDTO1.setId("id1");
        RecipeDTO recipeDTO2 = new RecipeDTO();
        assertThat(recipeDTO1).isNotEqualTo(recipeDTO2);
        recipeDTO2.setId(recipeDTO1.getId());
        assertThat(recipeDTO1).isEqualTo(recipeDTO2);
        recipeDTO2.setId("id2");
        assertThat(recipeDTO1).isNotEqualTo(recipeDTO2);
        recipeDTO1.setId(null);
        assertThat(recipeDTO1).isNotEqualTo(recipeDTO2);
    }
}
