package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ChangeType;
import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.dto.ModelType;
import com.silenteight.hsbc.bridge.model.transfer.GovernanceModelManager;
import com.silenteight.model.api.v1.ModelPromotedForProduction;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
@RequiredArgsConstructor
class NewGovernanceModelListener {

  private final String address;
  private final GovernanceModelManager governanceModelManager;

  private static final String MODEL = ModelType.MODEL.name();

  @RabbitListener(queues = "${silenteight.bridge.amqp.ingoing.model-promoted-queue}")
  void onModelChange(ModelPromotedForProduction modelPromoted) {
    log.info(
        "Received ModelPromotedForProduction for Governance model with version={}",
        modelPromoted.getVersion());

    var modelInfo = convertToModelInfo(modelPromoted);
    governanceModelManager.transferModelToJenkins(modelInfo);
  }

  private ModelInfo convertToModelInfo(ModelPromotedForProduction modelPromoted) {
    var version = modelPromoted.getVersion();
    var type = MODEL;
    return ModelInfo.builder()
        .name(version)
        .url(address + "/model/export/" + type + "/" + version)
        .type(type)
        .changeType(ChangeType.MAJOR.name())
        .build();
  }
}
