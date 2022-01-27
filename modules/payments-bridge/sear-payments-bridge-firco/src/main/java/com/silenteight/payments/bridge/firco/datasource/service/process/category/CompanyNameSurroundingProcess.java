package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingRequest;
import com.silenteight.payments.bridge.agents.port.CompanyNameSurroundingUseCase;
import com.silenteight.payments.bridge.firco.datasource.model.CategoryValueExtractModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_COMPANY_NAME_SURROUNDING;

@Service
@RequiredArgsConstructor
class CompanyNameSurroundingProcess implements CategoryValueProcess {

  private final CompanyNameSurroundingUseCase companyNameSurroundingUseCase;

  @Override
  public CategoryValue createCategoryValue(CategoryValueExtractModel categoryValueExtractModel) {
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_COMPANY_NAME_SURROUNDING)
        .setAlert(categoryValueExtractModel.getAlertName())
        .setMatch(categoryValueExtractModel.getMatchName())
        .setSingleValue(getValue(categoryValueExtractModel.getHitData()))
        .build();
  }

  private String getValue(HitData hitData) {
    var alertedPartyNames =
        Optional.ofNullable(hitData.getAlertedPartyData().getNames()).orElseGet(
            Collections::emptyList);

    return companyNameSurroundingUseCase.invoke(
        CompanyNameSurroundingRequest.builder().allNames(alertedPartyNames).build()).name();
  }
}
