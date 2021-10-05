package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.TwoLinesNameAgentRequest;
import com.silenteight.payments.bridge.agents.port.TwoLinesNameUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("twoLinesName")
class TwoLinesNameExtractor implements CategoryValueExtractor {

  private final TwoLinesNameUseCase twoLinesNameUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch, String alert) {

    var value = twoLinesNameUseCase.invoke(
        TwoLinesNameAgentRequest
            .builder()
            .alertedPartyAddresses(learningMatch.getAddresses())
            .build());

    return CategoryValue
        .newBuilder()
        .setName("categories/twoLines")
        .setMatch(learningMatch.getMatchName())
        .setSingleValue(value.toString())
        .build();
  }
}
