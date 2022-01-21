package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.CompanyNameSurroundingUseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_COMPANY_NAME_SURROUNDING;

@Service
@RequiredArgsConstructor
class CompanyNameSurroundingCategoryExtractor extends BaseCategoryValueExtractor {

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
