package com.silenteight.payments.bridge.svb.learning.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;
import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.CategoryType;
import com.silenteight.payments.bridge.datasource.category.port.CreateCategoriesClient;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_PREFIX;

@RequiredArgsConstructor
class CreateCategoriesUseCase {

  private final CreateCategoriesClient createCategoriesClient;
  private final Map<String, CreateCategoriesProperties.Category> categories;

  @EventListener(ApplicationReadyEvent.class)
  public void createCategories() {
    createCategoriesClient.createCategories(BatchCreateCategoriesRequest
        .newBuilder()
        .addAllCategories(getAllCategories())
        .build());
  }

  private List<Category> getAllCategories() {
    return categories.entrySet().stream().map(this::createCategory).collect(Collectors.toList());
  }

  private Category createCategory(Entry<String, CreateCategoriesProperties.Category> category) {
    var value = category.getValue();
    return Category
        .newBuilder()
        .setName(CATEGORY_PREFIX + category.getKey())
        .setDisplayName(value.getDisplayName())
        .setType(CategoryType.valueOf(value.getType()))
        .setMultiValue(Boolean.parseBoolean(value.getMultiValue()))
        .addAllAllowedValues(value.getAllowedValues())
        .build();
  }
}
