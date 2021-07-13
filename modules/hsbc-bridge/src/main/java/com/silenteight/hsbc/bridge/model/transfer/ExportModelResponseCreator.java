package com.silenteight.hsbc.bridge.model.transfer;

import lombok.Value;

import com.silenteight.hsbc.bridge.model.ExportModelResponseDto;
import com.silenteight.hsbc.bridge.model.rest.output.ExportModelResponse;

import java.nio.charset.StandardCharsets;

@Value(staticConstructor = "of")
class ExportModelResponseCreator {

  ExportModelResponseDto exportModel;

  ExportModelResponse create() {
    var response = new ExportModelResponse();
    response.setModelJson(new String(exportModel.getModelJson(), StandardCharsets.UTF_8));
    return response;
  }
}
