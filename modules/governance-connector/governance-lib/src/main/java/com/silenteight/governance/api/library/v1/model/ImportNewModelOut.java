package com.silenteight.governance.api.library.v1.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.model.api.v1.ImportNewModelResponse;

@Value
@Builder
public class ImportNewModelOut {

  String model;

  static ImportNewModelOut createFrom(ImportNewModelResponse response) {
    return ImportNewModelOut
        .builder()
        .model(response.getModel())
        .build();
  }
}
