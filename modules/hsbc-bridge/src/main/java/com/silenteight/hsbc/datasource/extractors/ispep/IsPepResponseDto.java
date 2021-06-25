package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.hsbc.datasource.dto.ispep.ReasonDto;

@Builder
@Value
public class IsPepResponseDto {

  @NonNull
  String solution;
  ReasonDto reason;
}
