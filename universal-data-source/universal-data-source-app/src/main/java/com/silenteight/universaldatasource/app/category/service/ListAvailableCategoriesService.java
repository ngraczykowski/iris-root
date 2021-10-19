package com.silenteight.universaldatasource.app.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.ListCategoriesResponse;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.category.port.incoming.ListAvailableCategoriesUseCase;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryDataAccess;

import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Slf4j
class ListAvailableCategoriesService implements ListAvailableCategoriesUseCase {

  private final CategoryDataAccess categoryDataAccess;

  @Timed(value = "uds.category.use_cases", extraTags = { "action", "getAvailableCategories" })
  @Override
  public ListCategoriesResponse getAvailableCategories() {
    var allCategories = categoryDataAccess.getAllCategories();

    if (log.isDebugEnabled()) {
      log.debug(
          "Listing available categories: count={}, names={}",
          allCategories.size(), allCategories.stream().map(Category::getName).collect(toList()));
    }

    return ListCategoriesResponse.newBuilder()
        .addAllCategories(allCategories)
        .build();
  }
}
