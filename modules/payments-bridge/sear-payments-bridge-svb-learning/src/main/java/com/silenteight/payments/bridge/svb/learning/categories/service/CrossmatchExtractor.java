package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("crossmatch")
@RequiredArgsConstructor
class CrossmatchExtractor implements CategoryValueExtractor {

  private final NameAddressCrossmatchUseCase nameAddressCrossmatchUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch, String alert) {
    var value =
        nameAddressCrossmatchUseCase.call(learningMatch.toCrossmatchRequest());
    return CategoryValue
        .newBuilder()
        .setName("categories/crossmatch")
        .setMatch(learningMatch.toName(alert))
        .setSingleValue(value.toString())
        .build();
  }
}
