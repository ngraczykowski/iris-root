package com.silenteight.hsbc.datasource.category;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

class CategoryModelHolder {

  private static final List<CategoryModel> CATEGORIES = createCategories();
  private static final String CATEGORIES_PREFIX = "categories/";

  private static List<CategoryModel> createCategories() {

    var sourceSystem = CategoryModel.builder()
        .name(CATEGORIES_PREFIX + "sourceSystem")
        .displayName("Source System")
        .type(CategoryType.ANY_STRING)
        .multiValue(false)
        .allowedValues(List.of())
        .valueRetriever(matchData -> {
          var caseInformation = matchData.getCaseInformation();
          return List.of(ofNullable(caseInformation.getSourceName()).orElse(""));
        })
        .build();

    var country = CategoryModel.builder()
        .name(CATEGORIES_PREFIX + "country")
        .displayName("Country")
        .type(CategoryType.ANY_STRING)
        .multiValue(true)
        .allowedValues(List.of())
        .valueRetriever(matchData -> List.of("GB"))
        .build();

    var customerType = CategoryModel.builder()
        .name(CATEGORIES_PREFIX + "customerType")
        .displayName("Customer Type")
        .type(CategoryType.ENUMERATED)
        .multiValue(false)
        .allowedValues(List.of("I", "C"))
        .valueRetriever(matchData -> List.of(matchData.isIndividual() ? "I" : "C"))
        .build();

    var hitType = CategoryModel.builder()
        .name(CATEGORIES_PREFIX + "hitType")
        .displayName("Hit Type")
        .type(CategoryType.ENUMERATED)
        .multiValue(false)
        .allowedValues(List.of("SAN", "PEP", "AM"))
        .valueRetriever(matchData -> {
          var caseInformation = matchData.getCaseInformation();
          return List.of(ofNullable(caseInformation.getExtendedAttribute5()).orElse(""));
        })
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
        .filter(c -> c.getName().equals(name))
        .findFirst();
  }
}
