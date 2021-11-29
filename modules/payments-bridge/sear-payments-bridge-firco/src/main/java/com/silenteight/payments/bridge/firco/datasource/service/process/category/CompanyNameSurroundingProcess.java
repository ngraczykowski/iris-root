package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingRequest;
import com.silenteight.payments.bridge.agents.port.CompanyNameSurroundingUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class CompanyNameSurroundingProcess extends BaseCategoryValueProcess {

  public static final String CATEGORY_COMPANY_NAME_SURROUNDING = "companyNameSurrounding";

  private final CompanyNameSurroundingUseCase companyNameSurroundingUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_COMPANY_NAME_SURROUNDING;
  }

  @Override
  protected String getValue(HitData hitData) {
    var alertedPartyNames =
        Optional.ofNullable(hitData.getAlertedPartyData().getNames()).orElseGet(
            Collections::emptyList);

    return companyNameSurroundingUseCase.invoke(
        CompanyNameSurroundingRequest.builder().allNames(alertedPartyNames).build()).name();
  }
}
