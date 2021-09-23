package com.silenteight.universaldatasource.app.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesResponse;
import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.universaldatasource.app.category.port.incoming.CreateCategoriesUseCase;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryDataAccess;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
class CreateCategoriesService implements CreateCategoriesUseCase {

  private final CategoryDataAccess categoryDataAccess;

  @Override
  public BatchCreateCategoriesResponse createCategories(
      List<Category> categoriesList) {
    var categories = categoryDataAccess.saveAll(categoriesList);

    if (log.isDebugEnabled()) {
      log.debug("Saved categories: categoriesCount={}", categories.size());
    }

    return BatchCreateCategoriesResponse.newBuilder()
        .addAllCategories(categories)
        .build();
  }
}
