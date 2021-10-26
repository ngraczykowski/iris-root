package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingAgentResponse;
import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingRequest;
import com.silenteight.payments.bridge.agents.port.CompanyNameSurroundingUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@Qualifier("companyNameSurrounding")
@RequiredArgsConstructor
class CompanyNameSurroundingProcess implements CategoryValueProcess {

  public static final String CATEGORIES_COMPANY_NAME_SURROUNDING =
      "categories/companyNameSurrounding";

  private final CompanyNameSurroundingUseCase companyNameSurroundingUseCase;

  @Override
  public CategoryValue extract(HitData hitData, String matchValue) {
    var alertedPartyNames =
        Optional.ofNullable(hitData.getAlertedPartyData().getNames()).orElseGet(
            Collections::emptyList);

    CompanyNameSurroundingAgentResponse response = companyNameSurroundingUseCase.invoke(
        CompanyNameSurroundingRequest.builder().allNames(alertedPartyNames).build());

    return CategoryValue
        .newBuilder()
        .setName(CATEGORIES_COMPANY_NAME_SURROUNDING)
        .setMatch(matchValue)
        .setSingleValue(response.name())
        .build();
  }
}
