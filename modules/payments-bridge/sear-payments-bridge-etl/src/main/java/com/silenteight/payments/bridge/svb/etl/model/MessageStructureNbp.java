package com.silenteight.payments.bridge.svb.etl.model;

import java.util.List;
import java.util.Optional;

import static com.silenteight.payments.bridge.svb.etl.util.CommonUtils.createOneElementList;
import static java.util.Arrays.asList;

public class MessageStructureNbp extends MessageStructureDefault {

  public MessageStructureNbp(String type, String apTag, String messageData) {
    super(type, apTag, messageData);
  }

  @Override
  public boolean checkMessageWithoutAccountNum() {
    return asList(C_CREDITOR, "UC_CRED").contains(getApTag());
  }

  @Override
  public boolean checkMessageFormatUnstructured() {
    return asList(C_CREDITOR, "UC_CRED").contains(getApTag());
  }

  @Override
  public Optional<String> getAccountNumber(GetAccountNumberRequest request) {
    var matchingField = request.getMatchingFields().stream()
        .findFirst()
        .orElse("");
    var trimmedTag = request.getTag().trim();
    var message = request.getMessage();

    if (List.of("C_CREDITOR", "UC_CRED").contains(trimmedTag)) {
      return Optional.of(extractAccountNumberOrFirstLine(
          List.of("C_CREDACCT", "C_CAGTCLRC"), message, matchingField, null));
    } else if ("PD_DEBTOR".equals(trimmedTag)) {
      return Optional.of(extractAccountNumberOrFirstLine(
          createOneElementList("PD_ACCOUNT"), message, matchingField, null));
    }
    return Optional.empty();
  }
}
