package com.silenteight.payments.bridge.svb.learning.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;
import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.CategoryType;
import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingAgentResponse;
import com.silenteight.payments.bridge.agents.model.NameAddressCrossmatchAgentResponse;
import com.silenteight.payments.bridge.common.dto.common.MessageStructure;
import com.silenteight.payments.bridge.datasource.category.port.CreateCategoriesClient;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.*;

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
        specificTerms2Category(),
        historicalRiskAssessmentCategory(),
        watchListTypeCategory(),
        matchTypeCategory(),
        companyNameSurroundingCategory(),
        messageStructureCategory()
    );
  }

  private static Category crossmatchCategory() {
    return Category
        .newBuilder()
        .setName(CATEGORY_NAME_CROSSMATCH)
        .setDisplayName("Name Address Crossmatch")
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(NameAddressCrossmatchAgentResponse.getValues())
        .build();
  }

  private static Category specificTermsCategory() {
    return Category
        .newBuilder()
        .setName(CATEGORY_NAME_SPECIFIC_TERMS)
        .setDisplayName(CATEGORY_SPECIFIC_TERMS_DISPLAY_NAME)
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO"))
        .build();
  }

  private static Category specificTerms2Category() {
    return Category
        .newBuilder()
        .setName(CATEGORY_NAME_SPECIFIC_TERMS_2)
        .setDisplayName(CATEGORY_SPECIFIC_TERMS_2_DISPLAY_NAME)
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "YES_PTP", "NO"))
        .build();
  }

  private static Category historicalRiskAssessmentCategory() {
    return Category
        .newBuilder()
        .setName(CATEGORY_NAME_HISTORICAL_RISK_ASSESSMENT)
        .setDisplayName(CATEGORY_HISTORICAL_RISK_ASSESSMENT_DISPLAY_NAME)
        .setType(CategoryType.ENUMERATED)
        .setMultiValue(false)
        .addAllAllowedValues(List.of("YES", "NO"))
        .build();
  }

  private static Category watchListTypeCategory() {
    return Category
        .newBuilder()
        .setName(CATEGORY_NAME_WATCHLIST_TYPE)
        .setDisplayName(CATEGORY_WATCHLIST_TYPE_DISPLAY_NAME)
        .setType(CategoryType.ENUMERATED)
        .addAllAllowedValues(List.of("ADDRESS", "COMPANY", "INDIVIDUAL", "VESSEL"))
        .setMultiValue(false)
        .build();
  }

  private static Category matchTypeCategory() {
    return Category
        .newBuilder()
        .setName(CATEGORY_NAME_MATCH_TYPE)
        .setDisplayName(CATEGORY_MATCH_TYPE_DISPLAY_NAME)
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
        .setName(CATEGORY_NAME_COMPANY_NAME_SURROUNDING)
        .setDisplayName(CATEGORY_COMPANY_NAME_SURROUNDING_DISPLAY_NAME)
        .setType(CategoryType.ENUMERATED)
        .addAllAllowedValues(CompanyNameSurroundingAgentResponse.getValues())
        .setMultiValue(false)
        .build();
  }

  private static Category messageStructureCategory() {
    return Category
        .newBuilder()
        .setName(CATEGORY_NAME_MESSAGE_STRUCTURE)
        .setDisplayName(CATEGORY_MESSAGE_STRUCTURE_DISPLAY_NAME)
        .setType(CategoryType.ENUMERATED)
        .addAllAllowedValues(MessageStructure.getValues())
        .setMultiValue(false)
        .build();
  }
}
