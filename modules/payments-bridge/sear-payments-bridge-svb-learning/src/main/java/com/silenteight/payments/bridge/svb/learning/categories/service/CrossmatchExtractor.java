package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CrossmatchExtractor extends BaseCategoryValueExtractor {

  public static final String CATEGORY_CROSSMATCH = "crossmatch";

  private final NameAddressCrossmatchUseCase nameAddressCrossmatchUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_CROSSMATCH;
  }

  @Override
  protected String getValue(LearningMatch learningMatch) {
    return nameAddressCrossmatchUseCase.call(learningMatch.toCrossmatchRequest()).toString();
  }

}
