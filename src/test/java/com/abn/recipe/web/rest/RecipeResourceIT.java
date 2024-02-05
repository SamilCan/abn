package com.abn.recipe.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.abn.recipe.IntegrationTest;
import com.abn.recipe.domain.Recipe;
import com.abn.recipe.repository.RecipeRepository;
import com.abn.recipe.service.dto.RecipeDTO;
import com.abn.recipe.service.mapper.RecipeMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link RecipeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecipeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VEGETARIAN = false;
    private static final Boolean UPDATED_VEGETARIAN = true;

    private static final Integer DEFAULT_SERVING = 1;
    private static final Integer UPDATED_SERVING = 2;

    private static final String[] DEFAULT_INGREDIENTS = new String[] { "AAAAAAAAAA", "BBBBBBBBBB" };
    private static final String[] UPDATED_INGREDIENTS = new String[] { "AAAAAAAAAA", "BBBBBBBBBB" };

    private static final String DEFAULT_INSTRUCTIONS = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recipes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final String ENTITY_API_FILTER_URL_ID = ENTITY_API_URL + "/query?";

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private MockMvc restRecipeMockMvc;

    private Recipe recipe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recipe createEntity() {
        Recipe recipe = new Recipe()
            .name(DEFAULT_NAME)
            .vegetarian(DEFAULT_VEGETARIAN)
            .serving(DEFAULT_SERVING)
            .ingredients(DEFAULT_INGREDIENTS)
            .instructions(DEFAULT_INSTRUCTIONS);
        return recipe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recipe createUpdatedEntity() {
        Recipe recipe = new Recipe()
            .name(UPDATED_NAME)
            .vegetarian(UPDATED_VEGETARIAN)
            .serving(UPDATED_SERVING)
            .ingredients(UPDATED_INGREDIENTS)
            .instructions(UPDATED_INSTRUCTIONS);
        return recipe;
    }

    @BeforeEach
    public void initTest() {
        recipeRepository.deleteAll();
        recipe = createEntity();
    }

    @Test
    void createRecipe() throws Exception {
        int databaseSizeBeforeCreate = recipeRepository.findAll().size();
        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);
        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isCreated());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeCreate + 1);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRecipe.getVegetarian()).isEqualTo(DEFAULT_VEGETARIAN);
        assertThat(testRecipe.getServing()).isEqualTo(DEFAULT_SERVING);
        assertThat(testRecipe.getIngredients()[0]).isEqualTo(DEFAULT_INGREDIENTS[0]);
        assertThat(testRecipe.getInstructions()).isEqualTo(DEFAULT_INSTRUCTIONS);
    }

    @Test
    void createRecipeWithExistingId() throws Exception {
        // Create the Recipe with an existing ID
        recipe.setId("existing_id");
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        int databaseSizeBeforeCreate = recipeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setName(null);

        // Create the Recipe, which fails.
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkVegetarianIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setVegetarian(null);

        // Create the Recipe, which fails.
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkServingIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setServing(null);

        // Create the Recipe, which fails.
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkInstructionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeRepository.findAll().size();
        // set the field null
        recipe.setInstructions(null);

        // Create the Recipe, which fails.
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRecipes() throws Exception {
        // Initialize the database
        recipeRepository.save(recipe);

        // Get all the recipeList
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipe.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].vegetarian").value(hasItem(DEFAULT_VEGETARIAN.booleanValue())))
            .andExpect(jsonPath("$.[*].serving").value(hasItem(DEFAULT_SERVING)))
            .andExpect(jsonPath("$.[*].ingredients").isArray())
            .andExpect(jsonPath("$.[*].instructions").value(hasItem(DEFAULT_INSTRUCTIONS)));
    }

    @Test
    void getRecipe() throws Exception {
        // Initialize the database
        recipeRepository.save(recipe);

        // Get the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL_ID, recipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recipe.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.vegetarian").value(DEFAULT_VEGETARIAN.booleanValue()))
            .andExpect(jsonPath("$.serving").value(DEFAULT_SERVING))
            .andExpect(jsonPath("$.ingredients[0]").value(DEFAULT_INGREDIENTS[0]))
            .andExpect(jsonPath("$.instructions").value(DEFAULT_INSTRUCTIONS));
    }

    @Test
    void getVegetarianRecipes() throws Exception {
        // Initialize the database
        recipeRepository.save(recipe);

        // Get the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_API_FILTER_URL_ID, "vegetarian=true"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(recipe.getId()))
            .andExpect(jsonPath("$.[*].name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.[*].vegetarian").value(DEFAULT_VEGETARIAN.booleanValue()));
    }

    @Test
    void getNotVegetarianRecipes() throws Exception {
        // Initialize the database
        recipe.setVegetarian(false);
        recipeRepository.save(recipe);

        // Get the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_API_FILTER_URL_ID, "vegetarian=false"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(recipe.getId()))
            .andExpect(jsonPath("$.[*].name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.[*].vegetarian").value(false));
    }

    @Test
    void getServingForThreeRecipes() throws Exception {
        // Initialize the database
        recipe.serving(3);
        recipeRepository.save(recipe);

        // Get the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_API_FILTER_URL_ID, "serving=3"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(recipe.getId()))
            .andExpect(jsonPath("$.[*].name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.[*].vegetarian").value(DEFAULT_VEGETARIAN.booleanValue()))
            .andExpect(jsonPath("$.[*].serving").value(3));
    }

    @Test
    void getServingForOneAndNotVegetarianRecipes() throws Exception {
        // Initialize the database
        recipe.serving(1);
        recipe.setVegetarian(false);
        recipeRepository.save(recipe);

        // Get the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_API_FILTER_URL_ID + "serving=1&vegetarian=false"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(recipe.getId()))
            .andExpect(jsonPath("$.[*].name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.[*].vegetarian").value(false))
            .andExpect(jsonPath("$.[*].serving").value(1));
    }

    @Test
    void getInstructionHasOvenRecipes() throws Exception {
        // Initialize the database
        recipe.instructions("use oven to bake");
        recipeRepository.save(recipe);

        // Get the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_API_FILTER_URL_ID + "searchInstructions=oven"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(recipe.getId()))
            .andExpect(jsonPath("$.[*].name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.[*].instructions").value("use oven to bake"))
            .andExpect(jsonPath("$.[*].vegetarian").value(DEFAULT_VEGETARIAN.booleanValue()))
            .andExpect(jsonPath("$.[*].serving").value(DEFAULT_SERVING));
    }

    @Test
    void getIngredientsIncludesAppleRecipes() throws Exception {
        // Initialize the database
        final String[] ingredients = new String[] { "apple", "potatoes" };
        recipe.setIngredients(ingredients);
        recipeRepository.save(recipe);

        // Get the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_API_FILTER_URL_ID + "includeIngredients=apple"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(recipe.getId()))
            .andExpect(jsonPath("$.[*].name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.[*].ingredients[0]").value(ingredients[0]))
            .andExpect(jsonPath("$.[*].vegetarian").value(DEFAULT_VEGETARIAN.booleanValue()))
            .andExpect(jsonPath("$.[*].serving").value(DEFAULT_SERVING));
    }

    @Test
    void getIngredientsExcludesAppleRecipes() throws Exception {
        // Initialize the database
        final String[] ingredients = new String[] { "apple", "potatoes" };
        recipe.setIngredients(ingredients);
        recipeRepository.save(recipe);

        // Get the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_API_FILTER_URL_ID + "excludeIngredients=orange"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(recipe.getId()))
            .andExpect(jsonPath("$.[*].name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.[*].ingredients[0]").value(ingredients[0]))
            .andExpect(jsonPath("$.[*].vegetarian").value(DEFAULT_VEGETARIAN.booleanValue()))
            .andExpect(jsonPath("$.[*].serving").value(DEFAULT_SERVING));
    }

    @Test
    void getNonExistingRecipe() throws Exception {
        // Get the recipe
        restRecipeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingRecipe() throws Exception {
        // Initialize the database
        recipeRepository.save(recipe);

        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();

        // Update the recipe
        Recipe updatedRecipe = recipeRepository.findById(recipe.getId()).get();
        updatedRecipe
            .name(UPDATED_NAME)
            .vegetarian(UPDATED_VEGETARIAN)
            .serving(UPDATED_SERVING)
            .ingredients(UPDATED_INGREDIENTS)
            .instructions(UPDATED_INSTRUCTIONS);
        RecipeDTO recipeDTO = recipeMapper.toDto(updatedRecipe);

        restRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRecipe.getVegetarian()).isEqualTo(UPDATED_VEGETARIAN);
        assertThat(testRecipe.getServing()).isEqualTo(UPDATED_SERVING);
        assertThat(testRecipe.getIngredients()).isEqualTo(UPDATED_INGREDIENTS);
        assertThat(testRecipe.getInstructions()).isEqualTo(UPDATED_INSTRUCTIONS);
    }

    @Test
    void putNonExistingRecipe() throws Exception {
        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();
        recipe.setId(UUID.randomUUID().toString());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRecipe() throws Exception {
        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();
        recipe.setId(UUID.randomUUID().toString());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRecipe() throws Exception {
        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();
        recipe.setId(UUID.randomUUID().toString());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRecipeWithPatch() throws Exception {
        // Initialize the database
        recipeRepository.save(recipe);

        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();

        // Update the recipe using partial update
        Recipe partialUpdatedRecipe = new Recipe();
        partialUpdatedRecipe.setId(recipe.getId());

        partialUpdatedRecipe.name(UPDATED_NAME).vegetarian(UPDATED_VEGETARIAN);

        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecipe))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRecipe.getVegetarian()).isEqualTo(UPDATED_VEGETARIAN);
        assertThat(testRecipe.getServing()).isEqualTo(DEFAULT_SERVING);
        assertThat(testRecipe.getIngredients()[0]).isEqualTo(DEFAULT_INGREDIENTS[0]);
        assertThat(testRecipe.getInstructions()).isEqualTo(DEFAULT_INSTRUCTIONS);
    }

    @Test
    void fullUpdateRecipeWithPatch() throws Exception {
        // Initialize the database
        recipeRepository.save(recipe);

        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();

        // Update the recipe using partial update
        Recipe partialUpdatedRecipe = new Recipe();
        partialUpdatedRecipe.setId(recipe.getId());

        partialUpdatedRecipe
            .name(UPDATED_NAME)
            .vegetarian(UPDATED_VEGETARIAN)
            .serving(UPDATED_SERVING)
            .ingredients(UPDATED_INGREDIENTS)
            .instructions(UPDATED_INSTRUCTIONS);

        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecipe))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
        Recipe testRecipe = recipeList.get(recipeList.size() - 1);
        assertThat(testRecipe.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRecipe.getVegetarian()).isEqualTo(UPDATED_VEGETARIAN);
        assertThat(testRecipe.getServing()).isEqualTo(UPDATED_SERVING);
        assertThat(testRecipe.getIngredients()).isEqualTo(UPDATED_INGREDIENTS);
        assertThat(testRecipe.getInstructions()).isEqualTo(UPDATED_INSTRUCTIONS);
    }

    @Test
    void patchNonExistingRecipe() throws Exception {
        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();
        recipe.setId(UUID.randomUUID().toString());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recipeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recipeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRecipe() throws Exception {
        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();
        recipe.setId(UUID.randomUUID().toString());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recipeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRecipe() throws Exception {
        int databaseSizeBeforeUpdate = recipeRepository.findAll().size();
        recipe.setId(UUID.randomUUID().toString());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(recipeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipe in the database
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRecipe() throws Exception {
        // Initialize the database
        recipeRepository.save(recipe);

        int databaseSizeBeforeDelete = recipeRepository.findAll().size();

        // Delete the recipe
        restRecipeMockMvc
            .perform(delete(ENTITY_API_URL_ID, recipe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Recipe> recipeList = recipeRepository.findAll();
        assertThat(recipeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
