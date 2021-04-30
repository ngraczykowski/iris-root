package com.silenteight.adjudication.engine.solve.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solve.category.dto.CategoryDto;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class GetOrCreateCategoriesUseCase {

  private final CreateUniqueCategoriesUseCase createUniqueCategoriesUseCase;
  private final GetCategoriesUseCase getCategoriesUseCase;

  List<CategoryDto> getOrCreateCategories(List<String> categories) {
    createUniqueCategoriesUseCase.createUniqueCategories(categories);
    return getCategoriesUseCase.getCategories(categories);
  }
}
