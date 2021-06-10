package com.silenteight.hsbc.datasource.dto.ispep;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Builder
@Value
public class IsPepFeatureSolutionDto {

  String solution;
  Map<String, String> reason;
}
