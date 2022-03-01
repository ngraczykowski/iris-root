package com.silenteight.payments.bridge.svb.learning.step.etl.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_HISTORICAL_RISK_ASSESSMENT;

@Service
@RequiredArgsConstructor
class HistoricalRiskCategoryExtractor extends BaseCategoryValueExtractor {

  private final HistoricalRiskAssessmentUseCase historicalRiskAssessmentUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_HISTORICAL_RISK_ASSESSMENT;
  }

  @Override
  protected String getValue(EtlHit etlHit) {
    var accountNumberOrFirstName = etlHit.getAccountNumberOrFirstName()
        .filter(String::isBlank)
        .orElseGet(() -> etlHit.getFirstAlertedPartyName().orElse(""));

    return historicalRiskAssessmentUseCase.invoke(HistoricalRiskAssessmentAgentRequest
            .builder()
            .accountNumber(accountNumberOrFirstName)
            .ofacID(etlHit.getFkcoVListFmmId())
            .build())
        .toString();
  }
}
