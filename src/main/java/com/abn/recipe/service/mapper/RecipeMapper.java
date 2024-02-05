package com.abn.recipe.service.mapper;

import com.abn.recipe.domain.Recipe;
import com.abn.recipe.service.dto.RecipeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recipe} and its DTO {@link RecipeDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecipeMapper extends EntityMapper<RecipeDTO, Recipe> {}
