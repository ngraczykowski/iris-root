package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import java.util.ArrayList;
import java.util.List;

import static com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils.processMessage;
import static java.util.Arrays.copyOfRange;
import static java.util.Collections.emptyList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MessageFieldExtractor {

  public static MessageNameAddressResult extractNameAddress(
      List<List<String>> fieldValues, MessageFieldStructure messageFieldStructure,
      boolean noAcctNum) {

    if (fieldValues.size() == 2) {
      var messageNameAddressResult =
          new MessageNameAddressResult(
              new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
              noAcctNum, 0, 0);

      List<String> mainTagFieldValueList = fieldValues.get(0);
      List<String> nextTagFieldValueList = fieldValues.get(1);

      for (var i = 0; i < mainTagFieldValueList.size(); i++) {

        MessageNameAddressResult partMessageNameAddressResult =
            MessageNameAddressResultExtractorHelper.extractNameAddressDtpMessages(
                mainTagFieldValueList.get(i),
                nextTagFieldValueList.get(i));
        if (partMessageNameAddressResult.getNames() != null) {
          messageNameAddressResult.getNames().addAll(partMessageNameAddressResult.getNames());
        }
        if (partMessageNameAddressResult.getNameAddresses() != null) {
          messageNameAddressResult
              .getNameAddresses()
              .addAll(partMessageNameAddressResult.getNameAddresses());
        }
        if (partMessageNameAddressResult.getAddresses() != null) {
          messageNameAddressResult
              .getAddresses()
              .addAll(partMessageNameAddressResult.getAddresses());
        }
        if (partMessageNameAddressResult.getCtryTowns() != null) {
          messageNameAddressResult
              .getCtryTowns()
              .addAll(partMessageNameAddressResult.getCtryTowns());
        }
      }
      return messageNameAddressResult;
    } else {
      String fieldValue = processMessage(fieldValues.get(0).get(0));
      return extractNameAddressHelper(fieldValue, messageFieldStructure, noAcctNum);
    }
  }

  static MessageNameAddressResult extractNameAddressHelper(
      String tag, MessageFieldStructure messageFieldStructure, boolean noAcctNum) {
    String processedMessage = processMessage(tag);
    String[] splitMessage = processedMessage.split("\n");
    int numOfLines = getNumOfLines(tag);

    if ("".equals(processedMessage)) {
      return MessageNameAddressResult.empty();
    }

    String[] nameAddresses;
    if (noAcctNum || splitMessage.length < 2)
      nameAddresses = splitMessage;
    else
      nameAddresses = copyOfRange(splitMessage, 1, splitMessage.length);

    if (nameAddresses.length == 0) {
      return MessageNameAddressResult.empty();
    }

    if (MessageFieldStructure.NAMEADDRESS_FORMAT_F == messageFieldStructure) {
      return decorateMetadata(
          MessageNameAddressResultExtractorHelper.extractNameAddressSwift103Field50FAnd59F(
              nameAddresses), noAcctNum, numOfLines, processedMessage.length());
    }

    if (MessageFieldStructure.NAMEADDRESS_FORMAT_UNSTRUCTURED == messageFieldStructure) {
      return decorateMetadata(
          MessageNameAddressResultExtractorHelper.extractNameAddressSwift103field50KAnd59(
              nameAddresses), noAcctNum, numOfLines, processedMessage.length());
    }

    if (MessageFieldStructure.NAMEADDRESS_FORMAT_LASTLINE_NAME == messageFieldStructure) {
      return decorateMetadata(
          MessageNameAddressResultExtractorHelper.extractNameAddressFieldLastLineName(
              nameAddresses), noAcctNum, numOfLines, processedMessage.length());
    }

    if (MessageFieldStructure.NAMEADDRESS_FORMAT_LASTLINE_EMAIL == messageFieldStructure) {
      return decorateMetadata(
          MessageNameAddressResultExtractorHelper.extractNameAddressFieldLastLineEmail(
              nameAddresses), noAcctNum, numOfLines, processedMessage.length());
    }

    return MessageNameAddressResult.empty();
  }

  private static int getNumOfLines(String message) {
    return message.split("\n").length;
  }

  private static MessageNameAddressResult decorateMetadata(
      MessageNameAddressResult messageNameAddressResult,
      boolean noAcctNum, int numOfLines, int length) {

    messageNameAddressResult.setNoAcctNumFlag(noAcctNum);
    messageNameAddressResult.setNumOfLines(numOfLines);
    messageNameAddressResult.setLength(length);

    return messageNameAddressResult;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MessageNameAddressResult {

    private List<String> nameAddresses;
    private List<String> names;
    private List<String> addresses;
    private List<String> ctryTowns;
    private boolean noAcctNumFlag;
    private int numOfLines;
    private int length;

    public static MessageNameAddressResult of(
        List<String> nameAddresses, List<String> names, List<String> addresses,
        List<String> ctryTowns) {

      var messageNameAddressResult = new MessageNameAddressResult();
      messageNameAddressResult.setNameAddresses(nameAddresses);
      messageNameAddressResult.setNames(names);
      messageNameAddressResult.setAddresses(addresses);
      messageNameAddressResult.setCtryTowns(ctryTowns);
      return messageNameAddressResult;
    }

    public static MessageNameAddressResult of(
        List<String> nameAddresses, List<String> names, List<String> addresses,
        List<String> ctryTowns, boolean noAcctNumFlag, int numOfLines, int length) {

      var messageNameAddressResult = MessageNameAddressResult.of(
          nameAddresses,
          names,
          addresses,
          ctryTowns);

      messageNameAddressResult.setNoAcctNumFlag(noAcctNumFlag);
      messageNameAddressResult.setNumOfLines(numOfLines);
      messageNameAddressResult.setLength(length);

      return messageNameAddressResult;
    }

    static MessageNameAddressResult empty() {
      return new MessageNameAddressResult(
          emptyList(), emptyList(), emptyList(), emptyList(), false, 0, 0);
    }
  }

}
