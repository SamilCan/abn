package com.abn.recipe.web.rest;

import com.abn.recipe.repository.RecipeRepository;
import com.abn.recipe.service.RecipeService;
import com.abn.recipe.service.dto.RecipeDTO;
import com.abn.recipe.service.model.RecipeFilterParameters;
import com.abn.recipe.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.abn.recipe.domain.Recipe}.
 */
@RestController
@RequestMapping("/api")
public class RecipeResource {

    private final Logger log = LoggerFactory.getLogger(RecipeResource.class);

    private static final String ENTITY_NAME = "recipe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecipeService recipeService;

    private final RecipeRepository recipeRepository;

    public RecipeResource(RecipeService recipeService, RecipeRepository recipeRepository) {
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
    }

    /**
     * {@code POST  /recipes} : Create a new recipe.
     *
     * @param recipeDTO the recipeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recipeDTO, or with status {@code 400 (Bad Request)} if the recipe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recipes")
    public ResponseEntity<RecipeDTO> createRecipe(@Valid @RequestBody RecipeDTO recipeDTO) throws URISyntaxException {
        log.debug("REST request to save Recipe : {}", recipeDTO);
        if (recipeDTO.getId() != null) {
            throw new BadRequestAlertException("A new recipe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecipeDTO result = recipeService.save(recipeDTO);
        return ResponseEntity
            .created(new URI("/api/recipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /recipes/:id} : Updates an existing recipe.
     *
     * @param id the id of the recipeDTO to save.
     * @param recipeDTO the recipeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipeDTO,
     * or with status {@code 400 (Bad Request)} if the recipeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recipeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recipes/{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody RecipeDTO recipeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Recipe : {}, {}", id, recipeDTO);
        if (recipeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecipeDTO result = recipeService.update(recipeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recipeDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /recipes/:id} : Partial updates given fields of an existing recipe, field will ignore if it is null
     *
     * @param id the id of the recipeDTO to save.
     * @param recipeDTO the recipeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipeDTO,
     * or with status {@code 400 (Bad Request)} if the recipeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recipeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recipeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recipes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecipeDTO> partialUpdateRecipe(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody RecipeDTO recipeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Recipe partially : {}, {}", id, recipeDTO);
        if (recipeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecipeDTO> result = recipeService.partialUpdate(recipeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recipeDTO.getId())
        );
    }

    /**
     * {@code GET  /recipes} : get all the recipes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recipes in body.
     */
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Recipes");
        Page<RecipeDTO> page = recipeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recipes} : get filtered the recipes.
     *
     * @param vegetarian the vegetarian of the recipeDTO to retrieve.
     * @param serving the serving of the recipeDTO to retrieve.
     * @param includeIngredients the array of the ingredients which should be included by the recipeDTO to retrieve.
     * @param excludeIngredients the array of the ingredients which should be excluded by the recipeDTO to retrieve.
     * @param searchInstructions the text which should be present in the instructions of the recipeDTO to retrieve.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recipes in body.
     */
    @GetMapping("/recipes/query")
    public ResponseEntity<List<RecipeDTO>> getFilteredRecipes(
        @RequestParam(value = "vegetarian", required = false) String vegetarian,
        @RequestParam(value = "serving", required = false) Integer serving,
        @RequestParam Optional<List<String>> includeIngredients,
        @RequestParam Optional<List<String>> excludeIngredients,
        @RequestParam(value = "searchInstructions", required = false) String searchInstructions,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        var filterParams = RecipeFilterParameters
            .builder()
            .vegetarian(vegetarian != null ? Boolean.valueOf(vegetarian) : null)
            .serving(serving)
            .includeIngredients(includeIngredients)
            .excludeIngredients(excludeIngredients)
            .searchInstructions(searchInstructions)
            .build();
        log.debug("REST request to get a filtered page of Recipes {} {}", Boolean.valueOf(vegetarian), serving);
        Page<RecipeDTO> page = recipeService.findByFilter(filterParams, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recipes/:id} : get the "id" recipe.
     *
     * @param id the id of the recipeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable String id) {
        log.debug("REST request to get Recipe : {}", id);
        Optional<RecipeDTO> recipeDTO = recipeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipeDTO);
    }

    /**
     * {@code DELETE  /recipes/:id} : delete the "id" recipe.
     *
     * @param id the id of the recipeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        log.debug("REST request to delete Recipe : {}", id);
        recipeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
