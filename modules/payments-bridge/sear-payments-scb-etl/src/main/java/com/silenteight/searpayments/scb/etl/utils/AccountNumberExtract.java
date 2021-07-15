package com.silenteight.searpayments.scb.etl.utils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.MessageFieldStructure;
import com.silenteight.searpayments.scb.etl.utils.AbstractMessageStructure.MessageStructureSts;

import java.util.List;
import java.util.Optional;

import static com.silenteight.searpayments.scb.etl.utils.CommonUtils.createOneElementList;
import static com.silenteight.searpayments.scb.etl.utils.FieldValueExtractor.extractMatchfieldFromNonScstarMessage;


@RequiredArgsConstructor
public class AccountNumberExtract {

  @NonNull private final String sourceSystem;
  @NonNull private final String tag;
  @NonNull private final String message;
  @NonNull private final List<String> matchingFields;
  @NonNull private final MessageFieldStructure messageFieldStructure;

  private String accountNumber;
  private String matchingField;
  private String trimmedTag;

  public Optional<String> invoke() {
    extractAccountNumber();
    cleanAccountNumber();
    return Optional.ofNullable(accountNumber);
  }

  @SuppressWarnings("java:S126")
  private void extractAccountNumber() {
    matchingField = matchingFields.stream()
        .findFirst()
        .orElse("");
    trimmedTag = tag.trim();
    accountNumber = null;

    if (sourceSystem.contains("STA") || sourceSystem.contains("AMX")) {
      processStaAmx();
    } else if (sourceSystem.contains("MTS")) {
      processMts();
    } else if (sourceSystem.contains("STS")) {
      processSts();
    } else if (sourceSystem.contains("NBP")) {
      processNbp();
    } else if (sourceSystem.contains("DTP")) {
      processDtp();
    }
  }

  private void processDtp() {
    if (List
        .of("C_AGENTCO", "C_CARRIER", "C_OTHBANK", "C_APLICANT", "C_SHIPPING", "C_BENFICRY",
            "C_DRAWEE", "C_DRAWER").contains(trimmedTag)) {
      accountNumber = matchingField.split(",")[0];
    }
  }

  @SuppressWarnings("java:S126")
  private void processNbp() {
    if (List.of("C_CREDITOR", "UC_CRED").contains(trimmedTag)) {
      accountNumber = extractAccountNumberOrFirstLine(
          List.of("C_CREDACCT", "C_CAGTCLRC"), message, matchingField, null);
    } else if ("PD_DEBTOR".equals(trimmedTag)) {
      accountNumber = extractAccountNumberOrFirstLine(
          createOneElementList("PD_ACCOUNT"), message, matchingField, null);
    }
  }

  @SuppressWarnings("java:S126")
  private void processSts() {
    if (List.of("C_CREDITOR", "S_CREDIT").contains(trimmedTag)) {
      accountNumber = extractAccountNumberOrFirstLine(createOneElementList("C_CREDACCT"), message,
          matchingField, null);
    } else if ("PD_DEBTOR".equals(trimmedTag)) {
      accountNumber = extractAccountNumberOrFirstLine(createOneElementList("PD_ACCOUNT"), message,
          matchingField, null);
    } else if ("S_DEBTOR".equals(trimmedTag)) {
      accountNumber = extractAccountNumberOrFirstLine(createOneElementList("S_DEBTACC"), message,
          matchingField, null);
    } else if ("S_UDEBTOR".equals(trimmedTag)) {
      accountNumber = extractAccountNumberOrFirstLine(createOneElementList("UD_UDEBTID"), message,
          matchingField, "S_UDEBTOR");
    } else if ("UD_UDEBTOR".equals(trimmedTag)) {
      accountNumber = extractAccountNumberOrFirstLine(createOneElementList("UD_UDEBTID"), message,
          matchingField, null);
    }
  }

  @SuppressWarnings("java:S126")
  private void processMts() {
    if (List
        .of("SWF_4_50F", "SWF_4_59F", "SWF_4_59", "SWF_4_50K", "CHP_502")
        .contains(trimmedTag)) {
      accountNumber = matchingField.split("\n")[0];
    } else if ("MTS_OPI".equals(trimmedTag)) {
      accountNumber = extractAccountNumberOrFirstLine(createOneElementList("MTS_OPD"),
          message, matchingField, null);
    } else if ("MTS_BPI".equals(trimmedTag)) {
      accountNumber = extractAccountNumberOrFirstLine(createOneElementList("MTS_BPD"),
          message, matchingField, null);
    } else if ("MTS_BBI".equals(trimmedTag)) {
      accountNumber = extractAccountNumberOrFirstLine(createOneElementList("MTS_BBD"),
          message, matchingField, null);
    } else if ("MTS_OBI".equals(trimmedTag)) {
      accountNumber = extractAccountNumberOrFirstLine(createOneElementList("MTS_OBD"),
          message, matchingField, null);
    }
  }

  private void processStaAmx() {
    if (List
        .of(
            MessageFieldStructure.NAMEADDRESS_FORMAT_F,
            MessageFieldStructure.NAMEADDRESS_FORMAT_UNSTRUCTURED)
        .contains(messageFieldStructure)) {
      accountNumber = matchingField.split("\n")[0];
    }
  }

  private static String extractAccountNumberOrFirstLine(
      List<String> accountNumTags, String messageData, String matchingfield,
      String addditionalTag) {

    for (String accountNumTag : accountNumTags) {
      String extractedMatchingFieldFromNonScstarMessage =
          extractMatchfieldFromNonScstarMessage(accountNumTag, messageData);
      if (!extractedMatchingFieldFromNonScstarMessage.equals("Matching field not extracted")) {
        return extractedMatchingFieldFromNonScstarMessage;
      }
    }
    String[] splittedMatchingField = matchingfield.split("\n");
    if (MessageStructureSts.S_UDEBTOR.equals(addditionalTag)) {

      return splittedMatchingField[splittedMatchingField.length - 1];
    }

    return splittedMatchingField[0];
  }

  private void cleanAccountNumber() {
    if (accountNumber != null) {
      accountNumber = AccountNumberCleaner.clean(accountNumber, tag);
    }
  }

}
