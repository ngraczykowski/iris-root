package com.silenteight.hsbc.datasource.date.dto;

import lombok.Value;

import java.util.List;

@Value
public class DateFeatureInputDto {
  String feature;
  List<String> alertedPartyDates;
  List<String> watchlistDates;
}
