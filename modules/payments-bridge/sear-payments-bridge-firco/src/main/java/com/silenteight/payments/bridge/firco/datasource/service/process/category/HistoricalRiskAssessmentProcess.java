package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@RequiredArgsConstructor
class HistoricalRiskAssessmentProcess extends BaseCategoryValueProcess {

  public static final String CATEGORY_HISTORICAL_RISK_ASSESSMENT = "historicalRiskAssessment";

  private final HistoricalRiskAssessmentUseCase historicalRiskAssessmentUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_HISTORICAL_RISK_ASSESSMENT;
  }

  @Override
  protected String getValue(HitData hitData) {
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
