package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.port.SpecificCommonTermsUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("specificCommonTerms")
class SpecificCommonTermsExtractor implements CategoryValueExtractor {

  private final SpecificCommonTermsUseCase specificCommonTermsUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch, String alert) {

    var value = specificCommonTermsUseCase.invoke(learningMatch.toSpecificCommonTermsRequest());

    return CategoryValue
        .newBuilder()
        .setName("categories/specificCommonTerms")
        .setMatch(learningMatch.toName(alert))
        .setSingleValue(value.toString())
        .build();
  }
}
