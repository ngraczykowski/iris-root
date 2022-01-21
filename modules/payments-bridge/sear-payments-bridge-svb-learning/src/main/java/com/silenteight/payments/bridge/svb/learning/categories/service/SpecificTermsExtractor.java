package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_SPECIFIC_TERMS;

@Service
@RequiredArgsConstructor
class SpecificTermsExtractor extends BaseCategoryValueExtractor {

  private final SpecificTermsUseCase specificTermsUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_SPECIFIC_TERMS;
  }

  @Override
  protected String getValue(LearningMatch learningMatch) {
    return specificTermsUseCase.invoke(learningMatch.toSpecificTermsRequest()).getValue();
  }
}
