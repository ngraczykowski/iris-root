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

  private static String mapSourceRiskTypeValue(String sourceValue) {
    switch (sourceValue) {
      case "AML":
      case "CTF-P2":
      case "INNIA":
      case "MX-AML":
      case "MX-SHCP":
        return "AML";
      case "AE-MEWOLF":
      case "MENA-GREY":
      case "MEWOLF":
      case "MX-DARK-GREY":
      case "SAN":
      case "US-HBUS":
        return "SAN";
      case "PEP":
        return "PEP";
      case "SCION":
        return "EXITS";
      case "SSC":
        return "SSC";
      default:
        return "OTHER";
    }
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
