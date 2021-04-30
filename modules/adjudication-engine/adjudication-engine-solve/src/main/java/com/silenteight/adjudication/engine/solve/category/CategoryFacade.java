package com.silenteight.adjudication.engine.solve.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solve.category.dto.CategoryDto;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryFacade {

  private final GetOrCreateCategoriesUseCase getOrCreateCategoriesUseCase;

  public List<CategoryDto> getOrCreateCategories(List<String> categoryNames) {
    return getOrCreateCategoriesUseCase.getOrCreateCategories(categoryNames);
  }
}
