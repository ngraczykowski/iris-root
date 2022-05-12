package com.silenteight.hsbc.bridge.model;

import com.silenteight.hsbc.bridge.model.dto.ExportModelResponseDto;
import com.silenteight.hsbc.bridge.model.dto.SolvingModelDto;

public interface ModelServiceClient {

  SolvingModelDto getSolvingModel();

  ExportModelResponseDto exportModel(String version);

  String transferModel(byte[] jsonModel);

  void sendStatus(String version);
}
