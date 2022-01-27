package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;
import com.silenteight.payments.bridge.firco.datasource.model.CategoryValueExtractModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_HISTORICAL_RISK_ASSESSMENT;

@Service
@RequiredArgsConstructor
class HistoricalRiskAssessmentProcess implements CategoryValueProcess {

  private final HistoricalRiskAssessmentUseCase historicalRiskAssessmentUseCase;

  @Override
  public CategoryValue createCategoryValue(CategoryValueExtractModel categoryValueExtractModel) {
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_HISTORICAL_RISK_ASSESSMENT)
        .setAlert(categoryValueExtractModel.getAlertName())
        .setMatch(categoryValueExtractModel.getMatchName())
        .setSingleValue(getValue(categoryValueExtractModel.getHitData()))
        .build();
  }

  private String getValue(HitData hitData) {
    return historicalRiskAssessmentUseCase.invoke(createRequest(hitData)).toString();
  }

  @Nonnull
  private static HistoricalRiskAssessmentAgentRequest createRequest(HitData hitData) {
    var accountNumberOrName = hitData.getAlertedPartyAccountNumberOrFirstName().orElse("");

    return HistoricalRiskAssessmentAgentRequest
        .builder()
        .accountNumber(accountNumberOrName)
        .ofacID(hitData.getHitAndWlPartyData().getId())
        .build();
  }
}
