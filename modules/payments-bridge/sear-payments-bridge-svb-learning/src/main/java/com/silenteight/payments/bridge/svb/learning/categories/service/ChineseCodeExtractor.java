package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.port.ChineseCommercialCodeUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("chinese")
@RequiredArgsConstructor
class ChineseCodeExtractor implements CategoryValueExtractor {

  private final ChineseCommercialCodeUseCase chineseCommercialCodeUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch, String alert) {
    var value = chineseCommercialCodeUseCase.invoke(learningMatch.getMatchedFieldValue());
    return CategoryValue
        .newBuilder()
        .setName("categories/chineseCode")
        .setMatch(learningMatch.toName(alert))
        .setSingleValue(value.toString())
        .build();
  }
}
