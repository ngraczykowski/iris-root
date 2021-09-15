package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.DelimiterInNameLineAgentRequest;
import com.silenteight.payments.bridge.agents.port.DelimiterInNameLineUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("delimiter")
@RequiredArgsConstructor
class DelimiterExtractor implements CategoryValueExtractor {

  private final DelimiterInNameLineUseCase delimiterInNameLineUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch, String alert) {
    var value = delimiterInNameLineUseCase.invoke(DelimiterInNameLineAgentRequest
        .builder()
        .allMatchingFieldsValue(learningMatch.getMatchedFieldValue())
        .messageFieldStructureText(
            learningMatch.getMessageFieldStructure().toString())
        .build());
    return CategoryValue
        .newBuilder()
        .setName("categories/delimiter")
        .setMatch(learningMatch.toName(alert))
        .setSingleValue(value.toString())
        .build();
  }
}
