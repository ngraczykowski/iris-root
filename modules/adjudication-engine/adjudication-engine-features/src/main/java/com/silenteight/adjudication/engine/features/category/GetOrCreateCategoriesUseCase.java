package com.silenteight.adjudication.engine.features.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.features.category.dto.CategoryDto;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class GetOrCreateCategoriesUseCase {

  private final CreateUniqueCategoriesUseCase createUniqueCategoriesUseCase;
  private final GetCategoriesUseCase getCategoriesUseCase;

  @Timed(
      histogram = true,
      percentiles = { 0.5, 0.95, 0.99 },
      value = "ae.features.use_cases",
      extraTags = { "package", "category" }
  )
  List<CategoryDto> getOrCreateCategories(List<String> categories) {
    createUniqueCategoriesUseCase.createUniqueCategories(categories);
    return getCategoriesUseCase.getCategories(categories);
  }
}
