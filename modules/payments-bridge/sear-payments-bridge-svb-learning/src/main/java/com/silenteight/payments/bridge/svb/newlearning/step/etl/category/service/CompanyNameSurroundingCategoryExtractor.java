package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.CompanyNameSurroundingUseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CompanyNameSurroundingCategoryExtractor extends BaseCategoryValueExtractor {

  public static final String CATEGORY_COMPANY_NAME_SURROUNDING = "companyNameSurrounding";

  private final CompanyNameSurroundingUseCase companyNameSurroundingUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_COMPANY_NAME_SURROUNDING;
  }

  @Override
  protected String getValue(EtlHit etlHit) {
    return companyNameSurroundingUseCase
        .invoke(etlHit.toCompanyNameSurroundingRequest())
        .toString();
  }
}
