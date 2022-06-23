package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningEntities;
import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
class CategoryModelHolder {

  private static final String CATEGORIES_PREFIX = "categories/";

  private static final String NNS = "NNS";
  private static final String NO = "NO";
  private static final String YES = "YES";

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
        .allowedValues(List.of("AML", "OTHER", "SAN", "PEP", "EXITS", "SSC", NNS))
        .valueRetriever(matchData -> {
          var caseInformation = matchData.getCaseInformation();
          var mappedValue = mapSourceRiskTypeValue(caseInformation.getExtendedAttribute5());

          return List.of(mappedValue);
        })
        .build();

    var terrorRelated = CategoryModel.builder()
        .name(CATEGORIES_PREFIX + "terrorRelated")
        .displayName("Terror Related")
        .type(CategoryType.ENUMERATED)
        .multiValue(false)
        .allowedValues(List.of(YES, NO))
        .valueRetriever(matchData -> {
          var caseInformation = matchData.getCaseInformation();
          var nnsEntities = matchData.getNnsEntities();
          var nnsIndividuals = matchData.getNnsIndividuals();

          if (NNS.equals(caseInformation.getExtendedAttribute5())) {
            return List.of(isTerrorRelated(nnsEntities, nnsIndividuals));
          }

          return List.of(NO);
        })
        .build();

    return List.of(customerType, hitType, terrorRelated);
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

  static String isTerrorRelated(
      List<NegativeNewsScreeningEntities> nnsEntities,
      List<NegativeNewsScreeningIndividuals> nnsIndividuals) {
    return nnsEntities.stream()
        .filter(entities -> entities.isContainingValue("Terror Related"))
        .findAny()
        .map(entities -> YES)
        .orElseGet(() -> isTerrorRelatedIndividuals(nnsIndividuals));
  }

  private static String isTerrorRelatedIndividuals(List<NegativeNewsScreeningIndividuals> nnsIndividuals) {
    return nnsIndividuals.stream()
        .filter(individuals -> individuals.isContainingValue("Terror Related"))
        .findAny()
        .map(entities -> YES)
        .orElse(NO);
  }
}
