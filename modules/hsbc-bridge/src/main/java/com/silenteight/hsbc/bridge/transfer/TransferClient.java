package com.silenteight.hsbc.bridge.transfer;

import com.silenteight.hsbc.bridge.transfer.ModelClient.Model;

public interface TransferClient {

  void transfer(Model model);
}
