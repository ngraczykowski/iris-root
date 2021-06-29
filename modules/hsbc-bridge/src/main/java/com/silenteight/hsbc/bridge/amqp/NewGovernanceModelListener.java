package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.transfer.GovernanceModelManager;
import com.silenteight.hsbc.bridge.model.transfer.ModelInfo;
import com.silenteight.model.api.v1.ModelPromotedForProduction;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.silenteight.hsbc.bridge.model.transfer.ChangeType.MAJOR;
import static com.silenteight.hsbc.bridge.model.transfer.ModelType.MODEL;

@RequiredArgsConstructor
class NewGovernanceModelListener {

  private final String address;
  private final GovernanceModelManager governanceModelManager;

  @RabbitListener(queues = "${silenteight.bridge.amqp.ingoing.model-promoted-queue}")
  void onModelChange(ModelPromotedForProduction modelPromoted) {
    var modelInfo = convertToModelInfo(modelPromoted);
    governanceModelManager.transferModelToJenkins(modelInfo);
  }

  private ModelInfo convertToModelInfo(ModelPromotedForProduction modelPromoted) {
    var name = modelPromoted.getName();
    return ModelInfo.builder()
        .name(name)
        .url(address + "/model/export/" + name)
        .type(MODEL.name())
        .changeType(MAJOR.name())
        .build();
  }
}
