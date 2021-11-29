package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.CompanyNameSurroundingUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CompanyNameSurroundingExtractor extends BaseCategoryValueExtractor {

  public static final String CATEGORY_COMPANY_NAME_SURROUNDING = "companyNameSurrounding";

  private final CompanyNameSurroundingUseCase companyNameSurroundingUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_COMPANY_NAME_SURROUNDING;
  }

  @Override
  protected String getValue(LearningMatch learningMatch) {
    return companyNameSurroundingUseCase
        .invoke(learningMatch.toCompanyNameSurroundingRequest())
        .toString();
  }
}
