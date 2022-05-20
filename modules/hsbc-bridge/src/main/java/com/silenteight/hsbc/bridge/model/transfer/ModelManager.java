package com.silenteight.hsbc.bridge.model.transfer;

import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.dto.ModelType;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;

public interface ModelManager {

  void transferModelToJenkins(ModelInfo modelInfo);

  void transferModelFromJenkins(ModelInfoRequest modelInfoRequest);

  void markAsUsedOnProd(String version);

  byte[] exportModel(Details modelDetails);

  boolean supportsModelType(ModelType modelType);

  interface Details {

    String getType();

    String getVersion();
  }

  default ModelInfoRequest createModelInfoRequest(ModelInfoRequest modelInfoRequest, String url) {
    var request = new ModelInfoRequest();
    request.setName(modelInfoRequest.getName());
    request.setType(modelInfoRequest.getType());
    request.setChangeType(modelInfoRequest.getChangeType());
    request.setUrl(url);
    return request;
  }

}
