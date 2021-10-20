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
      List<Category> newCategories) {

    log.info("Creating categories: count={}, categories={}", newCategories.size(),
        newCategories.stream().map(Category::getName).collect(toList()));

    var createdCategories = categoryDataAccess.saveAll(newCategories);

    if (log.isDebugEnabled()) {
      log.debug("Created new categories: count={}, categories={}", createdCategories.size(),
          createdCategories.stream().map(Category::getName).collect(toList()));
    }

    return BatchCreateCategoriesResponse.newBuilder()
        .addAllCategories(createdCategories)
        .build();
  }
}
