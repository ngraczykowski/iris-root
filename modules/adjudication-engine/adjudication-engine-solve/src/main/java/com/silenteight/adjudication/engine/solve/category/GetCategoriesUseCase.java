package com.silenteight.adjudication.engine.solve.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solve.category.dto.CategoryDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
class GetCategoriesUseCase {

  private final CategoryRepository repository;

  @Transactional(readOnly = true)
  List<CategoryDto> getCategories(List<String> categoryNames) {
    return repository
        .findAllByNameIn(categoryNames)
        .stream()
        .map(Category::toDto)
        .collect(toList());
  }
}
