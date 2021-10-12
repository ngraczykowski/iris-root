package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;
import com.silenteight.payments.bridge.svb.etl.port.GetAccountNumberUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Qualifier("historicalRisk")
class HistoricalRiskExtractor implements CategoryValueExtractor {

  private final HistoricalRiskAssessmentUseCase historicalRiskAssessmentUseCase;
  private final GetAccountNumberUseCase getAccountNumberUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch, String alert) {

    var optAccount = getAccountNumberUseCase.getAccountNumber(
        learningMatch.toGetAccountNumberRequest());

    if (optAccount.isEmpty())
      throw new IllegalArgumentException("Couldn't get match account number");

    var result = historicalRiskAssessmentUseCase.invoke(HistoricalRiskAssessmentAgentRequest
        .builder()
        .accountNumber(optAccount.get())
        .ofacID(learningMatch.getMatchId())
        .build());

    return CategoryValue
        .newBuilder()
        .setName("categories/historicalRiskAssessment")
        .setMatch(learningMatch.getMatchName())
        .setSingleValue(result.toString())
        .build();
  }
}
