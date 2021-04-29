package com.silenteight.hsbc.bridge.transfer;

public interface ModelClient {

  Model getModel(ModelInfo modelInfo);

  interface Model {

    String getVersion();

    String getUrl();
  }
}
