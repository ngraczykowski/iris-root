package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import java.util.Optional;

public class PepTransactionMessage extends BaseTransactionMessage {

  public PepTransactionMessage(MessageData messageData) {
    super(messageData);
  }

  @Override
  public Optional<String> getAccountNumber(String tag) {
    var lines = getMessageData().getLines(tag);

    if (lines.size() < 5 || lines.size() > 6)
      return Optional.empty();

    return Optional.of(lines.get(0));
  }
}
