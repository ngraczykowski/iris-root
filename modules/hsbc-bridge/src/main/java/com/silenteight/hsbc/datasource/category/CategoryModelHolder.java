package com.silenteight.hsbc.datasource.category;

import java.util.List;
import java.util.Optional;

class CategoryModelHolder {

  private static final List<CategoryModel> CATEGORIES = createCategories();

  private static List<CategoryModel> createCategories() {

    var sourceSystem = CategoryModel.builder()
        .name("sourceSystem")
        .displayName("Source System")
        .type(CategoryType.ANY_STRING)
        .multiValue(false)
        .allowedValues(List.of())
        .build();

    var country = CategoryModel.builder()
        .name("country")
        .displayName("Country")
        .type(CategoryType.ANY_STRING)
        .multiValue(true)
        .allowedValues(List.of())
        .build();

    var customerType = CategoryModel.builder()
        .name("customerType")
        .displayName("Customer Type (Individual / Company)")
        .type(CategoryType.ENUMERATED)
        .multiValue(false)
        .allowedValues(List.of("I", "C"))
        .build();

    var hitType = CategoryModel.builder()
        .name("hitType")
        .displayName("Hit Type (PEP / AM / SAN)")
        .type(CategoryType.ENUMERATED)
        .multiValue(false)
        .allowedValues(List.of("SAN", "PEP", "AM"))
        .build();

    return List.of(
        sourceSystem,
        country,
        customerType,
        hitType);
  }

  static List<CategoryModel> getCategories() {
    return CATEGORIES;
  }

  static Optional<CategoryModel> getCategoryModelByName(String name) {
    return CATEGORIES.stream()
        .filter(c->c.getName().equals(name))
        .findFirst();
  }
}
