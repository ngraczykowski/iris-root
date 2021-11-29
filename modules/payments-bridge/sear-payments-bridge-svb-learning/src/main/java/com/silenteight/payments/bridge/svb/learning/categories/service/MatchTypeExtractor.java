package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class MatchTypeExtractor extends BaseCategoryValueExtractor {

  public static final String CATEGORY_MATCH_TYPE = "matchType";

  @Override
  protected String getCategoryName() {
    return CATEGORY_MATCH_TYPE;
  }

  @Override
  protected String getValue(LearningMatch learningMatch) {
    return learningMatch.getSolutionType().name();
  }
}
