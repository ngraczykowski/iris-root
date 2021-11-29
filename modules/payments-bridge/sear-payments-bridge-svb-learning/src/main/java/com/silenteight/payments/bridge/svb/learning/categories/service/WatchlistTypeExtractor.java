package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class WatchlistTypeExtractor extends BaseCategoryValueExtractor {

  public static final String CATEGORY_WATCHLIST_TYPE = "watchlistType";

  @Override
  protected String getCategoryName() {
    return CATEGORY_WATCHLIST_TYPE;
  }

  @Override
  protected String getValue(LearningMatch learningMatch) {
    return learningMatch.getWatchlistType().toString();
  }
}
