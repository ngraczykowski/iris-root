package com.silenteight.payments.bridge.svb.oldetl.model;

import java.util.Optional;

import static com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils.createOneElementList;

public class MessageStructureGfx extends MessageStructureDefault {

  public MessageStructureGfx(String type, String apTag, String messageData) {
    super(type, apTag, messageData);
  }

  @Override
  public Optional<String> getAccountNumber(GetAccountNumberRequest request) {
    var matchingField = request.getMatchingFields().stream()
        .findFirst()
        .orElse("");

    var accountNumber = extractAccountNumberOrFirstLine(createOneElementList("ORIGINATOR"),
        getMessageData(), matchingField);

    if (accountNumber == null)
      return Optional.empty();

    return Optional.of(accountNumber.split("\n")[1]);
  }
}
