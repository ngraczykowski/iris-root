package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import java.util.Optional;

public class GfxTransactionMessage extends BaseTransactionMessage {

  // Mateusz
  public GfxTransactionMessage(MessageData messageData) {
    super(messageData);
  }

  @Override
  public Optional<String> getAccountNumber(String tag) {
    return Optional.of(getMessageData().getLines(tag).get(1));
  }
}
