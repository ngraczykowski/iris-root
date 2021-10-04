package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.port.SanctionedNationalityUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("sanctionedNationality")
class SanctionedNationalityExtractor implements CategoryValueExtractor {

  private final SanctionedNationalityUseCase sanctionedNationalityUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch, String alert) {
    var value = sanctionedNationalityUseCase.invoke(learningMatch.getMessageData());

    return CategoryValue
        .newBuilder()
        .setName("categories/sanctionedNationality")
        .setMatch(learningMatch.toName(alert))
        .setSingleValue(value.toString())
        .build();
  }
}
