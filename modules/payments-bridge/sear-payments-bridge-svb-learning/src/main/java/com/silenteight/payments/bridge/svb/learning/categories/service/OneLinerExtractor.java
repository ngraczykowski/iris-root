package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.port.OneLinerUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("oneliner")
@RequiredArgsConstructor
class OneLinerExtractor implements CategoryValueExtractor {

  private final OneLinerUseCase oneLinerUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch, String alert) {
    var value = oneLinerUseCase.invoke(learningMatch.toOneLinerAgentRequest());

    return CategoryValue
        .newBuilder()
        .setName("categories/oneLiner")
        .setMatch(learningMatch.toName(alert))
        .setSingleValue(value.toString())
        .build();
  }
}
