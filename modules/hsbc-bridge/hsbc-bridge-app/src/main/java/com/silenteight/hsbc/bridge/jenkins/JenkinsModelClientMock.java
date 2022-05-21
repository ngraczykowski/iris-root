package com.silenteight.hsbc.bridge.jenkins;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto;
import com.silenteight.hsbc.bridge.model.transfer.ModelClient;

@Slf4j
class JenkinsModelClientMock implements ModelClient {

  @Override
  public void updateModel(ModelInfo modelInfo) {
    log.info("Model {} updated", modelInfo.getName());
  }

  @Override
  public void sendModelStatus(ModelStatusUpdatedDto modelStatusUpdated) {
    log.info(
        "Model {} with status {} sent", modelStatusUpdated.getName(),
        modelStatusUpdated.getStatus());
  }
}
