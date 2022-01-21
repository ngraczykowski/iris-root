package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingRequest;
import com.silenteight.payments.bridge.agents.port.CompanyNameSurroundingUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_COMPANY_NAME_SURROUNDING;

@Service
@RequiredArgsConstructor
class CompanyNameSurroundingProcess extends BaseCategoryValueProcess {

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
