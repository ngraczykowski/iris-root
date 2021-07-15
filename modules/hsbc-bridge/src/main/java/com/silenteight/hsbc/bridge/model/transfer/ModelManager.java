package com.silenteight.hsbc.bridge.model.transfer;

import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.dto.ModelType;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;
import com.silenteight.hsbc.bridge.model.rest.output.ExportModelResponse;

public interface ModelManager {

  void transferModelToJenkins(ModelInfo modelInfo);

  void transferModelFromJenkins(ModelInfoRequest modelInfoRequest);

  void transferModelStatus(ModelInfoStatusRequest modelInfoStatusRequest);

  ExportModelResponse exportModel(Details modelDerails);

  boolean supportsModelType(ModelType modelType);

  interface Details {

    String getType();

    String getVersion();
  }
}
