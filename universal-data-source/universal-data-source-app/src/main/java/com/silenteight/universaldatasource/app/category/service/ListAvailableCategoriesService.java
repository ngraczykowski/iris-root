package com.silenteight.universaldatasource.app.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.ListCategoriesResponse;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.category.port.incoming.ListAvailableCategoriesUseCase;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryDataAccess;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Slf4j
@EnableConfigurationProperties(ListAvailableCategoriesProperties.class)
class ListAvailableCategoriesService implements ListAvailableCategoriesUseCase {

  private final CategoryDataAccess categoryDataAccess;

  private final ListAvailableCategoriesProperties properties;

  @Timed(value = "uds.category.use_cases", extraTags = { "action", "getAvailableCategories" })
  @Override
  public ListCategoriesResponse getAvailableCategories() {

    var allCategories = categoryDataAccess.getAllCategories();
    var filteredCategories = filterCategories(allCategories);

    if (log.isDebugEnabled()) {
      log.debug(
          "Listing available categories: count={}, names={}",
          filteredCategories.size(), filteredCategories.stream().map(Category::getName).collect(toList()));
    }

    return ListCategoriesResponse.newBuilder()
        .addAllCategories(filteredCategories)
        .build();
  }

  private List<Category> filterCategories(List<Category> allCategories) {
    return allCategories.stream()
        .filter(this::isContainsCategory)
        .collect(toList());
  }

  private boolean isContainsCategory(Category category) {
    return properties.getAvailable().contains(category.getName());
  }
}
