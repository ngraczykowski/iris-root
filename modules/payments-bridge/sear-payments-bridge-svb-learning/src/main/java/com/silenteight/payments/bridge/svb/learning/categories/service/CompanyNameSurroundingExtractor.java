package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingAgentResponse;
import com.silenteight.payments.bridge.agents.port.CompanyNameSurroundingUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("companyNameSurrounding")
class CompanyNameSurroundingExtractor implements CategoryValueExtractor {

  private final CompanyNameSurroundingUseCase companyNameSurroundingUseCase;

  @Override
  public CategoryValue extract(LearningMatch learningMatch) {

    CompanyNameSurroundingAgentResponse result =
        companyNameSurroundingUseCase.invoke(learningMatch.toCompanyNameSurroundingRequest());

    return CategoryValue
        .newBuilder()
        .setName("categories/companyNameSurrounding")
        .setMatch(learningMatch.getMatchName())
        .setSingleValue(result.toString())
        .build();
  }
}
