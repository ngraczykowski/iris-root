package com.silenteight.adjudication.engine.features.category;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class CreateUniqueCategoriesUseCase {

  private final CategoryDataAccess dataAccess;

  @Transactional
  int createUniqueCategories(List<String> categoryNames) {
    return dataAccess.addCategories(categoryNames);
  }
}
