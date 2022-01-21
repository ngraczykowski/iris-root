package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_HISTORICAL_RISK_ASSESSMENT;

@Service
@RequiredArgsConstructor
class HistoricalRiskExtractor extends BaseCategoryValueExtractor {

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
