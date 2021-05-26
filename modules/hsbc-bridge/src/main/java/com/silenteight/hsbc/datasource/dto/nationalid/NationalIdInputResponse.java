package com.silenteight.hsbc.datasource.dto.nationalid;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class NationalIdInputResponse {

  @Builder.Default
  List<NationalIdInputDto> inputs = emptyList();
}
