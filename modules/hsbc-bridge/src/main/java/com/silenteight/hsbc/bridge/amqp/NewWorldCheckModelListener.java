package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.transfer.WorldCheckModelManager;
import com.silenteight.worldcheck.api.v1.ModelPersisted;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.silenteight.hsbc.bridge.model.dto.ChangeType.MINOR;

@RequiredArgsConstructor
class NewWorldCheckModelListener {

  private final String address;
  private final WorldCheckModelManager worldCheckModelManager;

  @RabbitListener(queues = "${silenteight.bridge.amqp.ingoing.model-persisted-queue}")
  void onModelChange(ModelPersisted modelPersisted) {
    var modelInfo = convertToModelInfo(modelPersisted);
    worldCheckModelManager.transferModelToJenkins(modelInfo);
  }

  private ModelInfo convertToModelInfo(ModelPersisted modelPersisted) {
    var name = modelPersisted.getModelName();
    var type = modelPersisted.getModelType().name();
    return ModelInfo.builder()
        .name(name)
        .url(address + "/model/export/" + type + name)
        .type(type)
        .changeType(MINOR.name())
        .build();
  }
}
