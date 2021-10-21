package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import java.util.List;
import java.util.Optional;

public class GfexTransactionMessage extends BaseTransactionMessage {

  public GfexTransactionMessage(MessageData messageData) {
    super(messageData);
  }

  @Override
  public Optional<String> getAccountNumber() {
    return Optional.empty();
  }

  @Override
  public List<String> getAllMatchingTexts(String tag) {
    return null;
  }

  @Override
  public List<String> getAllMatchingTagValues(String tag, String matchingText) {
    return null;
  }
}
