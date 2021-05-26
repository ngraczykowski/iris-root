package com.silenteight.hsbc.datasource.dto.date;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
@Builder
public class DateFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyDates = emptyList();
  @Builder.Default
  List<String> watchlistDates = emptyList();
}
