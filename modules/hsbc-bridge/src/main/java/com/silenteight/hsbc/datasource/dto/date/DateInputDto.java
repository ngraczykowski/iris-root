package com.silenteight.hsbc.datasource.dto.date;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class DateInputDto {

  String match;
  @Builder.Default
  List<DateFeatureInputDto> featureInputs = emptyList();
}
