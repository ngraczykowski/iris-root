package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import java.util.Optional;

public class PepTransactionMessage extends BaseTransactionMessage {

  public PepTransactionMessage(MessageData messageData) {
    super(messageData);
  }

  @Override
  public Optional<String> getAccountNumber(String tag) {
    return Optional.empty();
  }
}
