package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import antlr.StringUtils;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class GtexTransactionMessage extends BaseTransactionMessage {

  public GtexTransactionMessage(MessageData messageData) {
    super(messageData);
  }

  @Override
  public Optional<String> getAccountNumber(String tag) {
    var lines = getMessageData().getLines(tag);

    if (lines.size() < 1)
      return empty();

    var accountNumberLine = lines.get(0);

    if (!accountNumberLine.startsWith("/"))
      return empty();

    var accountNumber = StringUtils.stripFront(accountNumberLine, "/");
    return of(accountNumber);
  }
}
