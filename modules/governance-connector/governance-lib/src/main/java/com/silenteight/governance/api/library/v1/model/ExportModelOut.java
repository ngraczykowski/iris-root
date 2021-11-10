package com.silenteight.governance.api.library.v1.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.model.api.v1.ExportModelResponse;

@Value
@Builder
public class ExportModelOut {

  byte[] modelJson;
  int id;
  String name;
  String version;

  static ExportModelOut createFrom(ExportModelResponse response) {
    return ExportModelOut.builder()
        .modelJson(response.getModelJson().toByteArray())
        .id(response.getId())
        .name(response.getName())
        .version(response.getVersion())
        .build();
  }
}
