package com.silenteight.hsbc.bridge.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExportModelResponseDto {

  byte[] modelJson;
}
