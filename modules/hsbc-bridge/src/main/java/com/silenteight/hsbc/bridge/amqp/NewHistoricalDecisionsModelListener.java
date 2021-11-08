package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ChangeType;
import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.transfer.HistoricalDecisionsModelManager;
import com.silenteight.proto.historicaldecisions.model.v1.api.ModelPersisted;


import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
@RequiredArgsConstructor
class NewHistoricalDecisionsModelListener {

  private final String address;
  private final HistoricalDecisionsModelManager historicalDecisionsModelManager;

  @RabbitListener(queues = "${silenteight.bridge.amqp.ingoing.historical-decisions-model-persisted-queue}")
  void onModelChange(ModelPersisted modelPersisted) {
    log.info(
        "Received ModelPersisted for HistoricalDecisions model type={}",
        modelPersisted.getModelType());

    var modelInfo = convertToModelInfo(modelPersisted);
    historicalDecisionsModelManager.transferModelToJenkins(modelInfo);
  }

  private ModelInfo convertToModelInfo(ModelPersisted modelPersisted) {
    var version = modelPersisted.getModelVersion();
    var type = modelPersisted.getModelType().name();
    return ModelInfo.builder()
        .name(version)
        .url(address + "/model/export/" + type + "/" + version)
        .type(type)
        .changeType(ChangeType.MINOR.name())
        .build();
  }
}
