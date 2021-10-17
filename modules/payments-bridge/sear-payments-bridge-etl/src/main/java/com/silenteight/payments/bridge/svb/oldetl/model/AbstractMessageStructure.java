package com.silenteight.payments.bridge.svb.oldetl.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import java.util.List;
import java.util.Optional;

import static com.silenteight.payments.bridge.svb.oldetl.service.FieldValueExtractor.extractMatchfieldFromNonScstarMessage;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@Slf4j
public abstract class AbstractMessageStructure {

  protected static final String C_CREDITOR = "C_CREDITOR";
  private final String type;
  private final String apTag;
  private final String messageData;

  public abstract boolean checkMessageWithoutAccountNum();

  public abstract boolean checkMessageFormatF();

  public abstract boolean checkMessageFormatUnstructured();

  public abstract boolean checkMessageFormatUnstructuredLastlineName();

  public abstract boolean checkMessageFormatUnstructuredLastlineEmail();

  public abstract Optional<String> getAccountNumber(GetAccountNumberRequest request);

  public MessageFieldStructure getMessageFieldStructure() {
    if (checkMessageFormatF())
      return MessageFieldStructure.NAMEADDRESS_FORMAT_F;
    else if (checkMessageFormatUnstructured())
      return MessageFieldStructure.NAMEADDRESS_FORMAT_UNSTRUCTURED;
    else if (checkMessageFormatUnstructuredLastlineName())
      return MessageFieldStructure.NAMEADDRESS_FORMAT_LASTLINE_NAME;
    else if (checkMessageFormatUnstructuredLastlineEmail())
      return MessageFieldStructure.NAMEADDRESS_FORMAT_LASTLINE_EMAIL;
    else
      return MessageFieldStructure.UNSTRUCTURED;
  }

  protected static String extractAccountNumberOrFirstLine(
      List<String> accountNumTags, String messageData, String matchingfield) {

    for (String accountNumTag : accountNumTags) {
      String extractedMatchingFieldFromNonScstarMessage =
          extractMatchfieldFromNonScstarMessage(accountNumTag, messageData);
      if (!extractedMatchingFieldFromNonScstarMessage.equals("Matching field not extracted")) {
        return extractedMatchingFieldFromNonScstarMessage;
      }
    }

    String[] splittedMatchingField = matchingfield.split("\n");
    return splittedMatchingField[0];
  }
}
