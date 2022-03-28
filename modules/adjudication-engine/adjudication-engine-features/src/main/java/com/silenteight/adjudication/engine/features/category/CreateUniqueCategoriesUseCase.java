package com.silenteight.adjudication.engine.features.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class CreateUniqueCategoriesUseCase {

  private final CategoryDataAccess dataAccess;

  @Transactional
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  int createUniqueCategories(List<String> categoryNames) {
    return dataAccess.addCategories(categoryNames);
  }
}
