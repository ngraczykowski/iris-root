package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("watchlistType")
@RequiredArgsConstructor
class WatchlistTypeExtractor implements CategoryValueExtractor {

  @Override
  public CategoryValue extract(LearningMatch learningMatch, String alert) {
    return CategoryValue
        .newBuilder()
        .setName("categories/watchlistType")
        .setMatch(learningMatch.getMatchName())
        .setSingleValue(learningMatch.getWatchlistType().toString())
        .build();
  }
}
