package com.silenteight.universaldatasource.app.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesResponse;
import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.category.port.incoming.CreateCategoriesUseCase;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryDataAccess;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
class CreateCategoriesService implements CreateCategoriesUseCase {

  private final CategoryDataAccess categoryDataAccess;

  @Timed(value = "uds.category.use_cases", extraTags = { "action", "createCategories" })
  @Override
  public BatchCreateCategoriesResponse createCategories(
      List<Category> categoriesList) {

    var categories = categoryDataAccess.saveAll(categoriesList);

    if (log.isDebugEnabled()) {
      log.debug("Saved categories: count={}, categories={}", categories.size(),
          categories.stream().map(Category::getName).collect(toList()));
    }

    return BatchCreateCategoriesResponse.newBuilder()
        .addAllCategories(categories)
        .build();
  }
}
