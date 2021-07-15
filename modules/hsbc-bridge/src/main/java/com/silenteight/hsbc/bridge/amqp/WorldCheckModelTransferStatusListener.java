package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto;
import com.silenteight.hsbc.bridge.model.transfer.WorldCheckModelManager;
import com.silenteight.worldcheck.api.v1.ModelStatusUpdated;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
@RequiredArgsConstructor
class WorldCheckModelTransferStatusListener {

  private final WorldCheckModelManager worldCheckModelManager;

  @RabbitListener(queues = "${silenteight.bridge.amqp.ingoing.model-loaded-queue}")
  public void handleWorldCheckStatus(ModelStatusUpdated modelStatus) {
    log.info(
        "Received ModelStatusUpdated for WorldCheck model type={}", modelStatus.getModelType());

    var modelStatusUpdated = convertToModelStatusUpdated(modelStatus);
    worldCheckModelManager.transferWorldCheckModelStatus(modelStatusUpdated);
  }

  private ModelStatusUpdatedDto convertToModelStatusUpdated(ModelStatusUpdated modelStatus) {
    return ModelStatusUpdatedDto.builder()
        .name(modelStatus.getModelName())
        .url(modelStatus.getModelUri())
        .type(modelStatus.getModelType().name())
        .status(modelStatus.getModelStatus().name())
        .build();
  }
}
