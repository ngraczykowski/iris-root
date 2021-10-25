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
        .addAllCategories(getAllCategories())
        .build());
  }

  private static List<Category> getAllCategories() {
    return List.of(
        crossmatchCategory(),
        specificTermsCategory(),
        historicalRiskAssessmentCategory(),
        watchListTypeCategory(),
        matchTypeCategory(),
        companyNameSurroundingCategory()
    );
  }

  private static Category crossmatchCategory() {
    return Category
        .newBuilder()
        .setName("categories/crossmatch")
        .setDisplayName("Name Address Crossmatch")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("NO_DECISION", "CROSSMATCH", "NO_CROSSMATCH"))
        .build();
  }

  private static Category specificTermsCategory() {
    return Category
        .newBuilder()
        .setName("categories/specificTerms")
        .setDisplayName("Specific Terms")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO"))
        .build();
  }

  private static Category historicalRiskAssessmentCategory() {
    return Category
        .newBuilder()
        .setName("categories/historicalRiskAssessment")
        .setDisplayName("Historical Risk Assessment")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO"))
        .build();
  }

  private static Category watchListTypeCategory() {
    return Category
        .newBuilder()
        .setName("categories/watchlistType")
        .setDisplayName("Watchlist Type")
        .setType(CategoryType.ENUMERATED)
        .addAllAllowedValues(List.of("ADDRESS", "COMPANY", "INDIVIDUAL", "VESSEL"))
        .setMultiValue(false)
        .build();
  }

  private static Category matchTypeCategory() {
    return Category
        .newBuilder()
        .setName("categories/matchType")
        .setDisplayName("Match Type")
        .setType(CategoryType.ENUMERATED)
        .addAllAllowedValues(
            List.of("ERROR", "UNKNOWN", "NAME", "SEARCH_CODE", "PASSPORT", "NATIONAL_ID", "BIC",
                "EMBARGO", "FML_RULE"))
        .setMultiValue(false)
        .build();
  }

  private static Category companyNameSurroundingCategory() {
    return Category
        .newBuilder()
        .setName("categories/companyNameSurrounding")
        .setDisplayName("Company Name Surrounding")
        .setType(CategoryType.ANY_STRING)
        .setMultiValue(false)
        .build();
  }
}
