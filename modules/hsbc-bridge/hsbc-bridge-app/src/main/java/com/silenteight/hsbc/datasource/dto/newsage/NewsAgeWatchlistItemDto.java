package com.silenteight.hsbc.datasource.dto.newsage;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class NewsAgeWatchlistItemDto {

  String id;
  String type;
  String furtherInformation;
}
