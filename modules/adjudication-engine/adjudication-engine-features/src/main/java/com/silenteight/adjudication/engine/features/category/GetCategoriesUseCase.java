package com.silenteight.adjudication.engine.features.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.features.category.dto.CategoryDto;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Slf4j
class GetCategoriesUseCase {

  private final CategoryRepository repository;

  @Transactional(readOnly = true)
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  List<CategoryDto> getCategories(List<String> categoryNames) {
    log.info("Get categories for category names. Category name size: {}", categoryNames.size());
    return repository
        .findAllByNameIn(categoryNames)
        .stream()
        .map(Category::toDto)
        .collect(toList());
  }
}
