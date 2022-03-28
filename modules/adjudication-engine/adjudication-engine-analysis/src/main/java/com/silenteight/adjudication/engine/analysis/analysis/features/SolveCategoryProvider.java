package com.silenteight.adjudication.engine.analysis.analysis.features;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.CategoryProvider;
import com.silenteight.adjudication.engine.features.category.CategoryFacade;
import com.silenteight.adjudication.engine.features.category.dto.CategoryDto;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class SolveCategoryProvider implements CategoryProvider {

  private final CategoryFacade categoryFacade;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public List<CategoryDto> getCategories(List<String> categoryNames) {
    return categoryFacade.getOrCreateCategories(categoryNames);
  }
}
