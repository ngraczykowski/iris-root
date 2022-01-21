package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_WATCHLIST_TYPE;

@Service
@RequiredArgsConstructor
class WatchlistTypeExtractor extends BaseCategoryValueExtractor {

  @Override
  protected String getCategoryName() {
    return CATEGORY_WATCHLIST_TYPE;
  }

  @Override
  protected String getValue(LearningMatch learningMatch) {
    return learningMatch.getWatchlistType().toString();
  }
}
