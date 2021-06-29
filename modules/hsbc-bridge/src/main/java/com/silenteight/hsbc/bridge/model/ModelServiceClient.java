package com.silenteight.hsbc.bridge.model;

public interface ModelServiceClient {

  SolvingModelDto getSolvingModel();

  ExportModelResponseDto exportModel(String name);

  void transferModel(byte[] model);

  void sendStatus(String modelName);
}
