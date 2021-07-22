package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class CategoryModelHolder {

  private static final String CATEGORIES_PREFIX = "categories/";

  private final List<CategoryModel> categories = createCategories();
  private final Map<String, List<String>> categoryProperties;

  private List<CategoryModel> createCategories() {
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
        .valueRetriever(matchData -> List.of())
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

    return List.of(
        sourceSystem,
        country,
        customerType,
        hitType);
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
