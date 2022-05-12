package com.silenteight.hsbc.bridge.model.transfer;

import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto;
import com.silenteight.hsbc.bridge.model.dto.ModelType;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;

public interface ModelManager {

  void transferModelToJenkins(ModelInfo modelInfo);

  ModelStatusUpdatedDto transferModelFromJenkins(ModelInfoRequest modelInfoRequest);

  void transferModelStatus(ModelInfoRequest modelInfoStatusRequest);

  byte[] exportModel(Details modelDetails);

  boolean supportsModelType(ModelType modelType);

  interface Details {

    String getType();

    String getVersion();
  }
}
