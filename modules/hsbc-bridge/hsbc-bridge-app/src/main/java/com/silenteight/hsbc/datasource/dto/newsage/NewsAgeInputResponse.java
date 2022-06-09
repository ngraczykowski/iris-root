package com.silenteight.hsbc.datasource.dto.newsage;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@Builder
public class NewsAgeInputResponse {

  @Builder.Default
  List<NewsAgeInputDto> inputs = Collections.emptyList();
}
