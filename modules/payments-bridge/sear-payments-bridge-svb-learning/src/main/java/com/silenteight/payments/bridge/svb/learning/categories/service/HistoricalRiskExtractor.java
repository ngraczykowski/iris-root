package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class HistoricalRiskExtractor extends BaseCategoryValueExtractor {

  public static final String CATEGORY_HISTORICAL_RISK_ASSESSMENT = "historicalRiskAssessment";

  private final HistoricalRiskAssessmentUseCase historicalRiskAssessmentUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_HISTORICAL_RISK_ASSESSMENT;
  }

  @Override
  protected String getValue(LearningMatch learningMatch) {
    var accountNumberOrFirstName = learningMatch.getAccountNumberOrFirstName()
        .filter(String::isBlank)
        .orElseGet(() -> learningMatch.getFirstAlertedPartyName().orElse(""));

    return historicalRiskAssessmentUseCase.invoke(HistoricalRiskAssessmentAgentRequest
            .builder()
            .accountNumber(accountNumberOrFirstName)
            .ofacID(learningMatch.getOfacId())
            .build())
        .toString();
  }
}
