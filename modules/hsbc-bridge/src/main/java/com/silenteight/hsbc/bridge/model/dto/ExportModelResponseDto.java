package com.silenteight.hsbc.bridge.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExportModelResponseDto {

  byte[] modelJson;
  int id;
  String name;
  String version;
}
