package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ChangeType;
import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.transfer.WorldCheckModelManager;
import com.silenteight.proto.worldcheck.api.v1.ModelPersisted;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
@RequiredArgsConstructor
class NewWorldCheckModelListener {

  private final String address;
  private final WorldCheckModelManager worldCheckModelManager;

  @RabbitListener(queues = "${silenteight.bridge.amqp.ingoing.worldcheck-model-persisted-queue}")
  void onModelChange(ModelPersisted modelPersisted) {
    log.info(
        "Received ModelPersisted for WorldCheck model type={}", modelPersisted.getModelType());

    var modelInfo = convertToModelInfo(modelPersisted);
    worldCheckModelManager.transferModelToJenkins(modelInfo);
  }

  private ModelInfo convertToModelInfo(ModelPersisted modelPersisted) {
    var version = modelPersisted.getModelVersion();
    var type = modelPersisted.getModelType().name();
    var name = modelPersisted.getModelName();
    return ModelInfo.builder()
        .name(name)
        .version(version)
        .url(address + "/model/export/" + type + "/" + version)
        .type(type)
        .changeType(ChangeType.MINOR.name())
        .build();
  }
}
