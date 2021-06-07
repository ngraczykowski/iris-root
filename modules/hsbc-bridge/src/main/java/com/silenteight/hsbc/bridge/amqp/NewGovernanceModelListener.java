package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.transfer.GovernanceModelManager;
import com.silenteight.hsbc.bridge.model.transfer.ModelInfo;
import com.silenteight.model.api.v1.ModelPromotedForProduction;

import static com.silenteight.hsbc.bridge.model.transfer.ChangeType.MAJOR;
import static com.silenteight.hsbc.bridge.model.transfer.ModelType.MODEL;

@RequiredArgsConstructor
class NewGovernanceModelListener {

  private final GovernanceModelManager governanceModelManager;

  /*@RabbitListener(queues = "${silenteight.governance.model_promoted.queue}")*/
  void onModelChange(ModelPromotedForProduction modelPromoted) {
    var modelInfo = convertToModelInfo(modelPromoted);
    governanceModelManager.transferModelToJenkins(modelInfo);
  }

  private ModelInfo convertToModelInfo(ModelPromotedForProduction modelPromoted) {
    return ModelInfo.builder()
        .name(modelPromoted.getName())
        .url(modelPromoted.getExportUrl())
        .type(MODEL.name())
        .changeType(MAJOR.name())
        .build();
  }
}
