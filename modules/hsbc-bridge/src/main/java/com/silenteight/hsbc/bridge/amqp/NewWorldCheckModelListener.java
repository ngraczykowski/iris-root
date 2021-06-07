package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.transfer.ModelInfo;
import com.silenteight.hsbc.bridge.model.transfer.WorldCheckModelManager;
import com.silenteight.worldcheck.api.v1.ModelPersisted;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.silenteight.hsbc.bridge.model.transfer.ChangeType.MINOR;

@RequiredArgsConstructor
class NewWorldCheckModelListener {

  private final WorldCheckModelManager worldCheckModelManager;

  @RabbitListener(queues = "${silenteight.bridge.model.persisted.queue}")
  void onModelChange(ModelPersisted modelPersisted) {
    var modelInfo = convertToModelInfo(modelPersisted);
    worldCheckModelManager.transferModelToJenkins(modelInfo);
  }

  private ModelInfo convertToModelInfo(ModelPersisted modelPersisted) {
    return ModelInfo.builder()
        .name(modelPersisted.getModelName())
        .url(modelPersisted.getModelUri())
        .type(modelPersisted.getModelType().name())
        .changeType(MINOR.name())
        .build();
  }
}
