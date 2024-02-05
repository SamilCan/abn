package com.abn.recipe.service;

import com.abn.recipe.service.dto.RecipeDTO;
import com.abn.recipe.service.model.RecipeFilterParameters;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.abn.recipe.domain.Recipe}.
 */
public interface RecipeService {
    /**
     * Save a recipe.
     *
     * @param recipeDTO the entity to save.
     * @return the persisted entity.
     */
    RecipeDTO save(RecipeDTO recipeDTO);

    /**
     * Updates a recipe.
     *
     * @param recipeDTO the entity to update.
     * @return the persisted entity.
     */
    RecipeDTO update(RecipeDTO recipeDTO);

    /**
     * Partially updates a recipe.
     *
     * @param recipeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RecipeDTO> partialUpdate(RecipeDTO recipeDTO);

    /**
     * Get all the recipes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecipeDTO> findAll(Pageable pageable);

    /**
     * Get filtered the recipes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecipeDTO> findByFilter(RecipeFilterParameters filterParameters, Pageable pageable);

    /**
     * Get the "id" recipe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecipeDTO> findOne(String id);

    /**
     * Delete the "id" recipe.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
