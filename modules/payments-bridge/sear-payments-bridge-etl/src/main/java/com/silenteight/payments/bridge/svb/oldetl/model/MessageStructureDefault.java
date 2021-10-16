package com.silenteight.payments.bridge.svb.oldetl.model;

import java.util.Optional;

public class MessageStructureDefault extends AbstractMessageStructure {

  public MessageStructureDefault(String type, String apTag, String messageData) {
    super(type, apTag, messageData);
  }

  @Override
  public boolean checkMessageWithoutAccountNum() {
    return false;
  }

  @Override
  public boolean checkMessageFormatF() {
    return false;
  }

  @Override
  public boolean checkMessageFormatUnstructured() {
    return false;
  }

  @Override
  public boolean checkMessageFormatUnstructuredLastlineName() {
    return false;
  }

  @Override
  public boolean checkMessageFormatUnstructuredLastlineEmail() {
    return false;
  }

  @Override
  public Optional<String> getAccountNumber(GetAccountNumberRequest request) {
    return Optional.empty();
  }
}
