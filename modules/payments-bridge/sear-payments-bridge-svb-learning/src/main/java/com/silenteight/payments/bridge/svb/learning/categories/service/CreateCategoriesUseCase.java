package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;
import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.CategoryType;
import com.silenteight.payments.bridge.categories.port.outgoing.CreateCategoriesClient;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class CreateCategoriesUseCase {

  private final CreateCategoriesClient createCategoriesClient;

  @EventListener(ApplicationReadyEvent.class)
  public void createCategories() {
    createCategoriesClient.createCategories(BatchCreateCategoriesRequest
        .newBuilder()
        .addAllCategories(
            List.of(
                crossmatchCategory(),
                oneLinerCategory(), specificTermsCategory(),
                twoLinesCategory(), historicalRiskAssessmentCategory(),
                watchListTypeCategory(), matchTypeCategory()))
        .build());
  }

  private static Category crossmatchCategory() {
    return Category
        .newBuilder()
        .setName("categories/crossmatch")
        .setDisplayName("Name Address Crossmatch Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("NO_DECISION", "CROSSMATCH", "NO_CROSSMATCH"))
        .build();
  }

  private static Category oneLinerCategory() {
    return Category
        .newBuilder()
        .setName("categories/oneLiner")
        .setDisplayName("One Liner Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO", "NO_DATA"))
        .build();
  }

  private static Category specificTermsCategory() {
    return Category
        .newBuilder()
        .setName("categories/specificTerms")
        .setDisplayName("Specific Terms Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO"))
        .build();
  }

  private static Category twoLinesCategory() {
    return Category
        .newBuilder()
        .setName("categories/twoLines")
        .setDisplayName("Two Lines Name Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO", "NO_DATA"))
        .build();
  }

  private static Category historicalRiskAssessmentCategory() {
    return Category
        .newBuilder()
        .setName("categories/historicalRiskAssessment")
        .setDisplayName("Historical Risk Assessment Category")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO"))
        .build();
  }

  private static Category watchListTypeCategory() {
    return Category
        .newBuilder()
        .setName("categories/watchListType")
        .setDisplayName("WatchList Type Category")
        .setType(CategoryType.ENUMERATED)
        .addAllAllowedValues(List.of("INDIVIDUAL", "ORGANIZATION", "ENTITY_TYPE_UNSPECIFIED"))
        .setMultiValue(false)
        .build();
  }

  private static Category matchTypeCategory() {
    return Category
        .newBuilder()
        .setName("categories/matchType")
        .setDisplayName("Match Type Category")
        .setType(CategoryType.ENUMERATED)
        .addAllAllowedValues(
            List.of("ERROR", "UNKNOWN", "NAME", "SEARCH_CODE", "PASSPORT", "NATIONAL_ID", "BIC",
                "EMBARGO", "FML_RULE"))
        .setMultiValue(false)
        .build();
  }

}
