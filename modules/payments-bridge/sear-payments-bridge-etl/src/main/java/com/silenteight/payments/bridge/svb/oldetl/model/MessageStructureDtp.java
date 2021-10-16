package com.silenteight.payments.bridge.svb.oldetl.model;

import lombok.Getter;

import java.util.List;
import java.util.Optional;

public class MessageStructureDtp extends MessageStructureDefault {

  public static final List<String> DTP_PAIRS_SCOPE =
      List.of("C_AGENTCO", "C_CARRIER", "C_OTHBANK", "C_APLICANT", "C_SHIPPING", "C_BENFICRY",
          "C_DRAWEE", "C_DRAWER");

  @Getter
  private final String matchtext;

  @Getter
  private final List<String> mainTagFieldValues;

  @Getter
  private final List<String> nextTagFieldValues;

  public MessageStructureDtp(
      String type, String apTag, String messageData, String matchtext,
      List<String> mainTagFieldValues,
      List<String> nextTagFieldValues) {
    super(type, apTag, messageData);
    this.matchtext = matchtext;
    this.mainTagFieldValues = mainTagFieldValues;
    this.nextTagFieldValues = nextTagFieldValues;
  }

  @Override
  public boolean checkMessageWithoutAccountNum() {
    return DTP_PAIRS_SCOPE.contains(getApTag());
  }

  @Override
  public boolean checkMessageFormatUnstructured() {
    return DTP_PAIRS_SCOPE.contains(getApTag());
  }

  @Override
  public Optional<String> getAccountNumber(GetAccountNumberRequest request) {
    var matchingField = request.getMatchingFields().stream()
        .findFirst()
        .orElse("");
    var trimmedTag = request.getTag().trim();

    if (List
        .of("C_AGENTCO", "C_CARRIER", "C_OTHBANK", "C_APLICANT", "C_SHIPPING", "C_BENFICRY",
            "C_DRAWEE", "C_DRAWER").contains(trimmedTag)) {
      return Optional.of(matchingField.split(",")[0]);
    }
    return Optional.empty();
  }
}
