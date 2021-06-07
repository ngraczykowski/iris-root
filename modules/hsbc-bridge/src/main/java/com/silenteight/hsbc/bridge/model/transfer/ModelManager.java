package com.silenteight.hsbc.bridge.model.transfer;

import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;

public interface ModelManager {

  void transferModelToJenkins(ModelInfo modelInfo);

  void transferModelFromJenkins(ModelInfoRequest modelInfoRequest);

  void transferModelStatus(ModelInfoStatusRequest modelInfoStatusRequest);

  boolean supportsModelType(ModelType modelType);
}
