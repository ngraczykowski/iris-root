package com.silenteight.hsbc.datasource.dto.ispep;


import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class IsPepInputDto {

  String match;
  IsPepFeatureInputDto isPepFeatureInput;
}
