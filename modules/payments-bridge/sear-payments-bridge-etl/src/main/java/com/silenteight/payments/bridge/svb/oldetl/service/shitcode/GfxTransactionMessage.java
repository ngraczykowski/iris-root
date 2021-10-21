package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import java.util.Optional;

public class GfxTransactionMessage extends BaseTransactionMessage {

  public GfxTransactionMessage(MessageData messageData) {
    super(messageData);
  }

  @Override
  public Optional<String> getAccountNumber(String tag) {
    //var lines = messageData.getLines(tag);
    //for (int line = 0; line < lines.size(); line++) {
    //  if ("AC".equals(lines.get(line))) {
    //    if (line + 1 < lines.size())
    //      return Optional.of(lines.get(line + 1));
    //    else
    //      return Optional.empty();
    //  }
    //}
    return Optional.of(getMessageData().getLines(tag).get(1));
  }
}
