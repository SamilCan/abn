package com.abn.recipe.service.impl;

import com.abn.recipe.domain.Recipe;
import com.abn.recipe.repository.RecipeRepository;
import com.abn.recipe.service.RecipeService;
import com.abn.recipe.service.dto.RecipeDTO;
import com.abn.recipe.service.mapper.RecipeMapper;
import com.abn.recipe.service.model.RecipeFilterParameters;
import com.mongodb.client.MongoClient;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Recipe}.
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    private final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final RecipeMapper recipeMapper;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    @Override
    public RecipeDTO save(RecipeDTO recipeDTO) {
        log.debug("Request to save Recipe : {}", recipeDTO);
        Recipe recipe = recipeMapper.toEntity(recipeDTO);
        recipe = recipeRepository.save(recipe);
        return recipeMapper.toDto(recipe);
    }

    @Override
    public RecipeDTO update(RecipeDTO recipeDTO) {
        log.debug("Request to update Recipe : {}", recipeDTO);
        Recipe recipe = recipeMapper.toEntity(recipeDTO);
        recipe = recipeRepository.save(recipe);
        return recipeMapper.toDto(recipe);
    }

    @Override
    public Optional<RecipeDTO> partialUpdate(RecipeDTO recipeDTO) {
        log.debug("Request to partially update Recipe : {}", recipeDTO);

        return recipeRepository
            .findById(recipeDTO.getId())
            .map(existingRecipe -> {
                recipeMapper.partialUpdate(existingRecipe, recipeDTO);

                return existingRecipe;
            })
            .map(recipeRepository::save)
            .map(recipeMapper::toDto);
    }

    @Override
    public Page<RecipeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Recipes");
        return recipeRepository.findAll(pageable).map(recipeMapper::toDto);
    }

    @Override
    public Page<RecipeDTO> findByFilter(RecipeFilterParameters filterParameters, Pageable pageable) {
        final Query query = new Query();

        if (filterParameters.getVegetarian() != null) {
            query.addCriteria(Criteria.where("vegetarian").is(filterParameters.getVegetarian()));
        }

        if (filterParameters.getServing() != null) {
            query.addCriteria(Criteria.where("serving").is(filterParameters.getServing()));
        }

        if (filterParameters.getSearchInstructions() != null) {
            query.addCriteria(Criteria.where("instructions").regex(filterParameters.getSearchInstructions()));
        }

        var includeList = filterParameters.getIncludeIngredients();
        var excludeList = filterParameters.getExcludeIngredients();

        // TODO: fix the error when both are presents
        if (includeList.isPresent() && excludeList.isPresent()) {
            Criteria criteria = new Criteria();
            criteria.andOperator(
                Criteria.where("ingredients").all(includeList.get()),
                Criteria.where("ingredients").nin(excludeList.get())
            );
            query.addCriteria(criteria);
        }

        if (includeList.isPresent()) {
            query.addCriteria(Criteria.where("ingredients").all(includeList.get()));
        }

        if (excludeList.isPresent()) {
            query.addCriteria(Criteria.where("ingredients").nin(excludeList.get()));
        }

        query.with(pageable);

        final List<RecipeDTO> recipeDTOList = mongoTemplate.find(query, RecipeDTO.class, "recipe");

        return PageableExecutionUtils.getPage(
            recipeDTOList,
            pageable,
            () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), RecipeDTO.class)
        );
    }

    @Override
    public Optional<RecipeDTO> findOne(String id) {
        log.debug("Request to get Recipe : {}", id);
        return recipeRepository.findById(id).map(recipeMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Recipe : {}", id);
        recipeRepository.deleteById(id);
    }
}
