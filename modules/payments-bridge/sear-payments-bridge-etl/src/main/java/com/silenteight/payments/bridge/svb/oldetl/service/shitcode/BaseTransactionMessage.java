package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public abstract class BaseTransactionMessage implements TransactionMessage {

  @Getter(AccessLevel.PROTECTED)
  private final MessageData messageData;

  @Override
  public Optional<String> getAccountNumber(String tag) {
    return Optional.empty();
  }

  @Override
  public List<String> getAllMatchingTexts(String tag) {
    return List.of();
  }

  @Override
  public List<String> getAllMatchingTagValues(String tag, String matchingText) {
    return messageData.findAllValues(tag).filter(s -> s.contains(matchingText)).collect(toList());
  }

  @Override
  public String getHitTagValue(String tag) {
    return messageData.getValue(tag);
  }
}
