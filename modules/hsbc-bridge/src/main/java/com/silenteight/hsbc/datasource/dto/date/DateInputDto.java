package com.silenteight.hsbc.datasource.dto.date;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class DateInputDto {

  String match;
  List<DateFeatureInputDto> featureInputs;
}
