package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("specificTerm")
class SpecificTermExtractor implements CategoryValueExtractor {

  private final SpecificTermsUseCase specificTermsUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch, String alert) {
    var value = specificTermsUseCase.invoke(learningMatch.toSpecificTermsRequest());

    return CategoryValue
        .newBuilder()
        .setName("categories/specificTerms")
        .setMatch(learningMatch.getMatchName())
        .setSingleValue(value.toString())
        .build();
  }
}
