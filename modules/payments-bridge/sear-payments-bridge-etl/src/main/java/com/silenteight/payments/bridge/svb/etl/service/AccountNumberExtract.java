package com.silenteight.payments.bridge.svb.etl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.etl.model.AbstractMessageStructure.MessageStructureSts;
import com.silenteight.payments.bridge.svb.etl.model.GetAccountNumberRequest;
import com.silenteight.payments.bridge.svb.etl.port.GetAccountNumberUseCase;
import com.silenteight.payments.bridge.svb.etl.response.MessageFieldStructure;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.silenteight.payments.bridge.svb.etl.service.FieldValueExtractor.extractMatchfieldFromNonScstarMessage;
import static com.silenteight.payments.bridge.svb.etl.util.CommonUtils.createOneElementList;


@RequiredArgsConstructor
@Service
@Slf4j
public class AccountNumberExtract implements GetAccountNumberUseCase {

  @Override
  public Optional<String> getAccountNumber(GetAccountNumberRequest request) {
    var accountNumber = extractAccountNumber(request);
    return accountNumber == null ? Optional.empty() : Optional.of(accountNumber);
  }

  @SuppressWarnings("java:S126")
  private String extractAccountNumber(GetAccountNumberRequest request) {
    var matchingField = request.getMatchingFields().stream()
        .findFirst()
        .orElse("");
    var trimmedTag = request.getTag().trim();
    var sourceSystem = request.getApplicationCode();

    if (sourceSystem.contains("STA") || sourceSystem.contains("AMX")) {
      return processStaAmx(request.getMessageFieldStructure(), matchingField);
    } else if (sourceSystem.contains("MTS")) {
      return processMts(trimmedTag, matchingField, request.getMessage());
    } else if (sourceSystem.contains("STS")) {
      return processSts(trimmedTag, matchingField, request.getMessage());
    } else if (sourceSystem.contains("NBP")) {
      return processNbp(trimmedTag, matchingField, request.getMessage());
    } else if (sourceSystem.contains("DTP")) {
      return processDtp(trimmedTag, matchingField);
    }

    log.warn("couldn't match source system to extract account number");

    return null;
  }

  private String processDtp(String trimmedTag, String matchingField) {
    if (List
        .of("C_AGENTCO", "C_CARRIER", "C_OTHBANK", "C_APLICANT", "C_SHIPPING", "C_BENFICRY",
            "C_DRAWEE", "C_DRAWER").contains(trimmedTag)) {
      return matchingField.split(",")[0];
    }
    return null;
  }

  @SuppressWarnings("java:S126")
  private String processNbp(String trimmedTag, String matchingField, String message) {
    if (List.of("C_CREDITOR", "UC_CRED").contains(trimmedTag)) {
      return extractAccountNumberOrFirstLine(
          List.of("C_CREDACCT", "C_CAGTCLRC"), message, matchingField, null);
    } else if ("PD_DEBTOR".equals(trimmedTag)) {
      return extractAccountNumberOrFirstLine(
          createOneElementList("PD_ACCOUNT"), message, matchingField, null);
    }
    return null;
  }

  @SuppressWarnings("java:S126")
  private String processSts(String trimmedTag, String matchingField, String message) {
    if (List.of("C_CREDITOR", "S_CREDIT").contains(trimmedTag)) {
      return extractAccountNumberOrFirstLine(createOneElementList("C_CREDACCT"), message,
          matchingField, null);
    } else if ("PD_DEBTOR".equals(trimmedTag)) {
      return extractAccountNumberOrFirstLine(createOneElementList("PD_ACCOUNT"), message,
          matchingField, null);
    } else if ("S_DEBTOR".equals(trimmedTag)) {
      return extractAccountNumberOrFirstLine(createOneElementList("S_DEBTACC"), message,
          matchingField, null);
    } else if ("S_UDEBTOR".equals(trimmedTag)) {
      return extractAccountNumberOrFirstLine(createOneElementList("UD_UDEBTID"), message,
          matchingField, "S_UDEBTOR");
    } else if ("UD_UDEBTOR".equals(trimmedTag)) {
      return extractAccountNumberOrFirstLine(createOneElementList("UD_UDEBTID"), message,
          matchingField, null);
    }
    return null;
  }

  @SuppressWarnings("java:S126")
  private String processMts(String trimmedTag, String matchingField, String message) {
    if (List
        .of("SWF_4_50F", "SWF_4_59F", "SWF_4_59", "SWF_4_50K", "CHP_502")
        .contains(trimmedTag)) {
      return matchingField.split("\n")[0];
    } else if ("MTS_OPI".equals(trimmedTag)) {
      return extractAccountNumberOrFirstLine(createOneElementList("MTS_OPD"),
          message, matchingField, null);
    } else if ("MTS_BPI".equals(trimmedTag)) {
      return extractAccountNumberOrFirstLine(createOneElementList("MTS_BPD"),
          message, matchingField, null);
    } else if ("MTS_BBI".equals(trimmedTag)) {
      return extractAccountNumberOrFirstLine(createOneElementList("MTS_BBD"),
          message, matchingField, null);
    } else if ("MTS_OBI".equals(trimmedTag)) {
      return extractAccountNumberOrFirstLine(createOneElementList("MTS_OBD"),
          message, matchingField, null);
    }
    return null;
  }

  private String processStaAmx(MessageFieldStructure messageFieldStructure, String matchingField) {
    if (List
        .of(
            MessageFieldStructure.NAMEADDRESS_FORMAT_F,
            MessageFieldStructure.NAMEADDRESS_FORMAT_UNSTRUCTURED)
        .contains(messageFieldStructure)) {
      return matchingField.split("\n")[0];
    }
    return null;
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
}
