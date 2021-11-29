package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class SpecificTermExtractor extends BaseCategoryValueExtractor {

  public static final String CATEGORY_SPECIFIC_TERMS = "specificTerms";

  private final SpecificTermsUseCase specificTermsUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_SPECIFIC_TERMS;
  }

  @Override
  protected String getValue(LearningMatch learningMatch) {
    return specificTermsUseCase.invoke(learningMatch.toSpecificTermsRequest()).toString();
  }
}
