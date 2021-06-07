package com.silenteight.hsbc.bridge.model.transfer;

public interface ModelClient {

  void updateModel(ModelInfo modelInfo);

  void sendModelStatus(ModelStatusUpdatedDto modelStatusUpdated);
}
