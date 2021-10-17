package com.silenteight.payments.bridge.svb.oldetl.model;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils.createOneElementList;
import static java.util.Arrays.asList;

public class MessageStructureSts extends MessageStructureDefault {

  public static final String S_CREDIT = "S_CREDIT";
  public static final String S_DEBTOR = "S_DEBTOR";
  public static final String S_UDEBTOR = "S_UDEBTOR";
  public static final String PD_DEBTOR = "PD_DEBTOR";
  public static final String UD_UDEBTOR = "UD_UDEBTOR";

  public MessageStructureSts(String type, String apTag, String messageData) {
    super(type, apTag, messageData);
  }

  @Override
  public boolean checkMessageWithoutAccountNum() {
    return asList(C_CREDITOR, S_CREDIT, S_DEBTOR, S_UDEBTOR, PD_DEBTOR, UD_UDEBTOR).contains(
        getApTag());
  }

  @Override
  public boolean checkMessageFormatUnstructured() {
    return asList(C_CREDITOR, S_CREDIT, S_DEBTOR, UD_UDEBTOR).contains(getApTag());
  }

  @Override
  public boolean checkMessageFormatUnstructuredLastlineName() {
    return Objects.equals(S_UDEBTOR, getApTag());
  }

  @Override
  public boolean checkMessageFormatUnstructuredLastlineEmail() {
    return Objects.equals(PD_DEBTOR, getApTag());
  }

  @Override
  public Optional<String> getAccountNumber(GetAccountNumberRequest request) {
    var matchingField = request.getMatchingFields().stream()
        .findFirst()
        .orElse("");
    var trimmedTag = request.getTag().trim();
    String accountNumTag = null;
    var message = getMessageData();

    if (List.of("C_CREDITOR", "S_CREDIT").contains(trimmedTag)) {
      accountNumTag = "C_CREDACCT";
    } else if ("PD_DEBTOR".equals(trimmedTag)) {
      accountNumTag = "PD_ACCOUNT";
    } else if ("S_DEBTOR".equals(trimmedTag)) {
      accountNumTag = "S_DEBTACC";
    } else if ("S_UDEBTOR".equals(trimmedTag)) {
      accountNumTag = "UD_UDEBTID";
    } else if ("UD_UDEBTOR".equals(trimmedTag)) {
      accountNumTag = "UD_UDEBTID";
    }
    return accountNumTag == null ? Optional.empty() : Optional.of(
        extractAccountNumberOrFirstLine(createOneElementList("UD_UDEBTID"), message,
            matchingField));
  }
}
