package com.silenteight.hsbc.datasource.dto.date;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.datasource.dto.name.EntityType;

import java.util.Collections;
import java.util.List;

@Value
@Builder
public class DateFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> alertedPartyDates = Collections.emptyList();
  @Builder.Default
  List<String> watchlistDates = Collections.emptyList();
  EntityType alertedPartyType;
  SeverityMode mode;
}
