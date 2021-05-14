package com.silenteight.hsbc.datasource.dto.date;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DateFeatureInputDto {
  String feature;
  List<String> alertedPartyDates;
  List<String> watchlistDates;
}
