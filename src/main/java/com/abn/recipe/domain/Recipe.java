package com.abn.recipe.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Recipe.
 */
@Document(collection = "recipe")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("vegetarian")
    private Boolean vegetarian;

    @NotNull
    @Min(value = 1)
    @Field("serving")
    private Integer serving;

    @NotEmpty
    @Field("ingredients")
    private String[] ingredients;

    @NotNull
    @Field("instructions")
    private String instructions;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Recipe id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Recipe name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVegetarian() {
        return this.vegetarian;
    }

    public Recipe vegetarian(Boolean vegetarian) {
        this.setVegetarian(vegetarian);
        return this;
    }

    public void setVegetarian(Boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public Integer getServing() {
        return this.serving;
    }

    public Recipe serving(Integer serving) {
        this.setServing(serving);
        return this;
    }

    public void setServing(Integer serving) {
        this.serving = serving;
    }

    public String[] getIngredients() {
        return this.ingredients;
    }

    public Recipe ingredients(String[] ingredients) {
        this.setIngredients(ingredients);
        return this;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public Recipe instructions(String instructions) {
        this.setInstructions(instructions);
        return this;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        return id != null && id.equals(((Recipe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", vegetarian='" + getVegetarian() + "'" +
            ", serving=" + getServing() +
            ", ingredients='" + getIngredients() + "'" +
            ", instructions='" + getInstructions() + "'" +
            "}";
    }
}
