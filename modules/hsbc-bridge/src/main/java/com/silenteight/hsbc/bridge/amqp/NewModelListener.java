package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.transfer.ModelInfo;
import com.silenteight.hsbc.bridge.transfer.ProcessManager;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RequiredArgsConstructor
class NewModelListener {

  private final ProcessManager processManager;

  // ModuleInfo will be replaced by Governance object and mapped to ModuleInfo
  @RabbitListener(queues = "${silenteight.governance.model_promoted.queue}")
  void onModelChange(ModelInfo modelInfo) {
    processManager.execute(modelInfo);
  }
}
