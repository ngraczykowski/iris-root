package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.transfer.ModelClient.Model;
import com.silenteight.hsbc.bridge.transfer.TransferClient;

public class TransferClientMock implements TransferClient {

  @Override
  public void transfer(Model model) {
    // TODO GRPC .proto contract should be defined by Governance soon !!!
    throw new UnsupportedOperationException();
  }
}
