package com.silenteight.governance.api.library.v1.model;

public interface ModelServiceClient {

  SolvingModelOut getSolvingModel();

  ExportModelOut exportModel(String version);

  String transferModel(byte[] jsonModel);

  void sendStatus(String modelName);
}
