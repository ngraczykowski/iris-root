package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.SpecificTerms2UseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_SPECIFIC_TERMS_2;

@Service
@RequiredArgsConstructor
class SpecificTerms2Extractor extends BaseCategoryValueExtractor {

  private final SpecificTerms2UseCase specificTerms2UseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_SPECIFIC_TERMS_2;
  }

  @Override
  protected String getValue(LearningMatch learningMatch) {
    return specificTerms2UseCase.invoke(learningMatch.toSpecificTermsRequest()).getValue();
  }
}
