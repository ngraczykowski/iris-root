package com.silenteight.adjudication.engine.analysis.solve;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.CategoryProvider;
import com.silenteight.adjudication.engine.solve.category.CategoryFacade;
import com.silenteight.adjudication.engine.solve.category.dto.CategoryDto;

import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class SolveCategoryProvider implements CategoryProvider {

  private final CategoryFacade categoryFacade;

  @Override
  public List<CategoryDto> getCategories(List<String> categoryNames) {
    return categoryFacade.getOrCreateCategories(categoryNames);
  }
}
