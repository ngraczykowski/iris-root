package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Qualifier("historicalRisk")
class HistoricalRiskExtractor implements CategoryValueExtractor {

  private final HistoricalRiskAssessmentUseCase historicalRiskAssessmentUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch) {

    var accountNumer = learningMatch.getAccountNumber();

    var result = historicalRiskAssessmentUseCase.invoke(HistoricalRiskAssessmentAgentRequest
        .builder()
        .accountNumber(accountNumer.isEmpty() || accountNumer.get().isBlank() ? learningMatch
            .getAlertedPartyNames()
            .get(0)
            .trim() : accountNumer.get())
        .ofacID(learningMatch.getOfacId())
        .build());

    return CategoryValue
        .newBuilder()
        .setName("categories/historicalRiskAssessment")
        .setMatch(learningMatch.getMatchName())
        .setSingleValue(result.toString())
        .build();
  }
}
