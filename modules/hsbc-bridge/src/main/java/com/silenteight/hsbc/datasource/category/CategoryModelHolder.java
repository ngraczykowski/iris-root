package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
class CategoryModelHolder {

  private static final String CATEGORIES_PREFIX = "categories/";

  private final List<CategoryModel> categories = createCategories();
  private final Map<String, List<String>> categoryProperties;

  private List<CategoryModel> createCategories() {
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
        .displayName("Risk Type")
        .type(CategoryType.ENUMERATED)
        .multiValue(false)
        .allowedValues(List.of("AML", "OTHER", "SAN", "PEP", "EXITS", "SSC"))
        .valueRetriever(matchData -> {
          var caseInformation = matchData.getCaseInformation();
          var mappedValue = mapSourceRiskTypeValue(caseInformation.getExtendedAttribute5());

          return List.of(mappedValue);
        })
        .build();

    return List.of(customerType, hitType);
  }

  String mapSourceRiskTypeValue(String sourceValue) {
    return categoryProperties.entrySet().stream()
        .filter(e -> e.getValue().contains(sourceValue))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse("OTHER");
  }

  List<CategoryModel> getCategories() {
    return List.copyOf(categories);
  }
}
