package com.silenteight.hsbc.bridge.model;

public interface ModelServiceClient {

  SolvingModelDto getSolvingModel();

  void transferModel(byte[] model);

  void sendStatus(String modelName);
}
