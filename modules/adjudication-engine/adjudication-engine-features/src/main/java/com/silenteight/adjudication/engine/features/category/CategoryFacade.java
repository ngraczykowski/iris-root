package com.silenteight.adjudication.engine.features.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.features.category.dto.CategoryDto;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryFacade {

  private final GetOrCreateCategoriesUseCase getOrCreateCategoriesUseCase;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<CategoryDto> getOrCreateCategories(List<String> categoryNames) {
    return getOrCreateCategoriesUseCase.getOrCreateCategories(categoryNames);
  }
}
