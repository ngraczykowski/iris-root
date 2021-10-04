package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.MatchtextFirstTokenOfAddressAgentRequest;
import com.silenteight.payments.bridge.agents.port.MatchTextFirstTokenOfAddressUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("matchFirstTokenAddress")
@RequiredArgsConstructor
class MatchFirstTokenAddressExtractor implements CategoryValueExtractor {

  private final MatchTextFirstTokenOfAddressUseCase matchTextFirstTokenOfAddressUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch, String alert) {
    var value = matchTextFirstTokenOfAddressUseCase.invoke(MatchtextFirstTokenOfAddressAgentRequest
        .builder()
        .matchingTexts(learningMatch.getMatchingTexts())
        .addresses(learningMatch.getAddresses())
        .build());

    return CategoryValue
        .newBuilder()
        .setName("categories/firstTokenAddress")
        .setMatch(learningMatch.toName(alert))
        .setSingleValue(value.toString())
        .build();
  }
}
