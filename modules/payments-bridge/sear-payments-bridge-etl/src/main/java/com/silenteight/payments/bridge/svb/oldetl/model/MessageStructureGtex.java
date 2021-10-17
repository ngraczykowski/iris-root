package com.silenteight.payments.bridge.svb.oldetl.model;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.silenteight.payments.bridge.svb.oldetl.service.FieldValueExtractor.extractMatchfieldFromScstarMessage;

public class MessageStructureGtex extends MessageStructureDefault {

  private static final List<String> ACCOUNT_NUMBER_TAGS = List.of("50A", "50F", "50K");
  private static final Pattern COMPILE = Pattern.compile("[^\\d.]");

  public MessageStructureGtex(String type, String apTag, String messageData) {
    super(type, apTag, messageData);
  }

  @Override
  public Optional<String> getAccountNumber(GetAccountNumberRequest request) {

    for (var tag : ACCOUNT_NUMBER_TAGS) {
      var accountNumber = extractMatchfieldFromScstarMessage(
          tag,
          getMessageData());
      if (!accountNumber.equals("Matching field not extracted")) {
        return Optional.of(COMPILE.matcher(accountNumber.split("\n")[0]).replaceAll(""));
      }
    }

    return Optional.empty();
  }
}
