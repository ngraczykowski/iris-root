package com.silenteight.hsbc.datasource.dto.newsage;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class NewsAgeFeatureInputDto {

  String feature;
  NewsAgeWatchlistItemDto watchlistItem;
}
