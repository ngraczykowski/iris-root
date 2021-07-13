package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.transfer.GovernanceModelManager;
import com.silenteight.hsbc.bridge.model.transfer.ModelInfo;
import com.silenteight.hsbc.bridge.model.transfer.ModelType;
import com.silenteight.model.api.v1.ModelPromotedForProduction;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.silenteight.hsbc.bridge.model.transfer.ChangeType.MAJOR;

@RequiredArgsConstructor
class NewGovernanceModelListener {

  private final String address;
  private final GovernanceModelManager governanceModelManager;

  private static final String MODEL = ModelType.MODEL.name();

  @RabbitListener(queues = "${silenteight.bridge.amqp.ingoing.model-promoted-queue}")
  void onModelChange(ModelPromotedForProduction modelPromoted) {
    var modelInfo = convertToModelInfo(modelPromoted);
    governanceModelManager.transferModelToJenkins(modelInfo);
  }

  private ModelInfo convertToModelInfo(ModelPromotedForProduction modelPromoted) {
    var name = modelPromoted.getName();
    var type = MODEL;
    return ModelInfo.builder()
        .name(name)
        .url(address + "/model/export/" + type + "/" + name)
        .type(type)
        .changeType(MAJOR.name())
        .build();
  }
}
