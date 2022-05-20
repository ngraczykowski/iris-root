package com.silenteight.hsbc.datasource.dto.nationalid;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class NationalIdInputResponse {

  @Builder.Default
  List<NationalIdInputDto> inputs = Collections.emptyList();
}
