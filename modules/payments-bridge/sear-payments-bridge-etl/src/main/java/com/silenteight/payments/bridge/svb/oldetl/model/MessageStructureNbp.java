package com.silenteight.payments.bridge.svb.oldetl.model;

import java.util.List;
import java.util.Optional;

import static com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils.createOneElementList;
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

    if (List.of("C_CREDITOR", "UC_CRED").contains(trimmedTag)) {
      return Optional.of(extractAccountNumberOrFirstLine(
          List.of("C_CREDACCT", "C_CAGTCLRC"), getMessageData(), matchingField));
    } else if ("PD_DEBTOR".equals(trimmedTag)) {
      return Optional.of(extractAccountNumberOrFirstLine(
          createOneElementList("PD_ACCOUNT"), getMessageData(), matchingField));
    }
    return Optional.empty();
  }
}
