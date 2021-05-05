package com.silenteight.hsbc.bridge.transfer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransferModelProcessManager implements ProcessManager {

  private final ModelClient modelClient;
  private final TransferServiceClient transferServiceClient;

  @Override
  public void execute(ModelInfo modelInfo) {
    var model = modelClient.getModel(modelInfo);
    transferServiceClient.transfer(model);
  }
}
