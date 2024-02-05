package com.abn.recipe.service.model;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeFilterParameters {

    private Boolean vegetarian;
    private Integer serving;
    private Optional<List<String>> includeIngredients;
    private Optional<List<String>> excludeIngredients;
    private String searchInstructions;
}
