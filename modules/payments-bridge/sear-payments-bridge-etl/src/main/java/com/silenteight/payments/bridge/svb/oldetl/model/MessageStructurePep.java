package com.silenteight.payments.bridge.svb.oldetl.model;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils.createOneElementList;

public class MessageStructurePep extends MessageStructureDefault {

  private static final Pattern COMPILE = Pattern.compile("\\s+");

  public MessageStructurePep(String type, String apTag, String messageData) {
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

    return Optional.of(COMPILE.matcher(accountNumber.split("\n")[0]).replaceAll(""));
  }
}
