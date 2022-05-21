package com.silenteight.hsbc.bridge.model.transfer;

import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto;

public interface ModelClient {

  void updateModel(ModelInfo modelInfo);

  void sendModelStatus(ModelStatusUpdatedDto modelStatusUpdated);
}
