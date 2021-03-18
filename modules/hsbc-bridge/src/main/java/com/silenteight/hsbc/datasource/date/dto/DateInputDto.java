package com.silenteight.hsbc.datasource.date.dto;

import lombok.Value;

import java.util.List;

@Value
public class DateInputDto {

  String match;
  List<DateFeatureInputDto> dateFeatureInputs;
}
