package com.silenteight.hsbc.bridge.transfer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransferModelProcessManager implements ProcessManager {

  private final ModelClient modelClient;
  private final TransferClient transferClient;

  @Override
  public void execute(ModelInfo modelInfo) {
    var model = modelClient.getModel(modelInfo);
    transferClient.transfer(model);
  }
}
