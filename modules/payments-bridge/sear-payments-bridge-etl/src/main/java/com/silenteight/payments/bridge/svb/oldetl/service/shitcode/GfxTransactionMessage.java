package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import java.util.Optional;

public class GfxTransactionMessage extends BaseTransactionMessage {

  public GfxTransactionMessage(MessageData messageData) {
    super(messageData);
  }

  @Override
  public Optional<String> getAccountNumber(String tag) {
    var lines = getMessageData().getLines(tag);

    if (lines.size() < 3 || lines.size() > 7)
      return Optional.empty();

    return Optional.of(getMessageData().getLines(tag).get(1));
  }
}
