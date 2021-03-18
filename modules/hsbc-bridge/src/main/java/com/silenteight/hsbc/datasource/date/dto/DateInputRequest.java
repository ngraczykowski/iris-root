package com.silenteight.hsbc.datasource.date.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DateInputRequest {
  List<String> matches;
  List<String> features;
}
