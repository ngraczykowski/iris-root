package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_CROSSMATCH;

@Service
@RequiredArgsConstructor
class CrossmatchExtractor extends BaseCategoryValueExtractor {

  private final NameAddressCrossmatchUseCase nameAddressCrossmatchUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_CROSSMATCH;
  }

  @Override
  protected String getValue(LearningMatch learningMatch) {
    return nameAddressCrossmatchUseCase
        .call(learningMatch.toCrossmatchRequest())
        .toString();
  }

}
