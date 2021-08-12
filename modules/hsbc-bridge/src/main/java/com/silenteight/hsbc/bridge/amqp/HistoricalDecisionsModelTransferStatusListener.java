package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto;
import com.silenteight.hsbc.bridge.model.transfer.HistoricalDecisionsModelManager;
import com.silenteight.proto.historicaldecisions.model.v1.api.ModelStatusUpdated;


import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
@RequiredArgsConstructor
class HistoricalDecisionsModelTransferStatusListener {

  private final HistoricalDecisionsModelManager historicalDecisionsModelManager;

  @RabbitListener(queues = "${silenteight.bridge.amqp.ingoing.historical-decisions-model-loaded-queue}")
  public void handleHistoricalDecisionsStatus(ModelStatusUpdated modelStatus) {
    log.info(
        "Received ModelStatusUpdated for HistoricalDecisions model type={}",
        modelStatus.getModelType());

    var modelStatusUpdated = convertToModelStatusUpdated(modelStatus);
    historicalDecisionsModelManager.transferHistoricalDecisionsModelStatus(modelStatusUpdated);
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
