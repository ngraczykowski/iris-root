package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.port.StripUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("strip")
class StripExtractor implements CategoryValueExtractor {

  private final StripUseCase stripUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch, String alert) {
    var value = stripUseCase.invoke(learningMatch.getMatchId());

    return CategoryValue
        .newBuilder()
        .setName("categories/strip")
        .setMatch(learningMatch.toName(alert))
        .setSingleValue(value.toString())
        .build();
  }
}
