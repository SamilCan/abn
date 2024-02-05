package com.abn.recipe.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.abn.recipe.domain.Recipe} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecipeDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private Boolean vegetarian;

    @NotNull
    @Min(value = 1)
    private Integer serving;

    private String[] ingredients;

    @NotNull
    private String instructions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(Boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public Integer getServing() {
        return serving;
    }

    public void setServing(Integer serving) {
        this.serving = serving;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecipeDTO)) {
            return false;
        }

        RecipeDTO recipeDTO = (RecipeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recipeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecipeDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", vegetarian='" + getVegetarian() + "'" +
            ", serving=" + getServing() +
            ", ingredients='" + getIngredients() + "'" +
            ", instructions='" + getInstructions() + "'" +
            "}";
    }
}
