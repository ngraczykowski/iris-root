package com.silenteight.payments.bridge.svb.oldetl.model;

import lombok.Getter;

import java.util.List;
import java.util.Optional;

import static com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils.createOneElementList;
import static java.util.Arrays.asList;

public class MessageStructureMts extends MessageStructureDefault {

  private static final String CHP_502 = "CHP_502";
  private static final String MTS_BBI = "MTS_BBI";
  private static final String MTS_BPI = "MTS_BPI";
  private static final String MTS_OBI = "MTS_OBI";
  private static final String MTS_OPI = "MTS_OPI";
  private static final List<String> MTS_APTAG_SCOPE =
      List.of(CHP_502, MTS_BBI, MTS_BPI, MTS_OBI, MTS_OPI, "SWF_4_59", "SWF_4_59F", "SWF_4_50K",
          "SWF_4_50F");
  @Getter
  private final String apMatchingField;

  public MessageStructureMts(
      String type, String apTag, String messageData, String apMatchingField) {
    super(type, apTag, messageData);
    this.apMatchingField = apMatchingField;
  }

  @Override
  public boolean checkMessageWithoutAccountNum() {
    return asList(MTS_BBI, MTS_BPI, MTS_OBI, MTS_OPI).contains(getApTag());
  }

  @Override
  public boolean checkMessageFormatF() {
    boolean formatFString = extractRegexFormatF(getApMatchingField());
    return asList("SWF_4_50F", "SWF_4_59F").contains(getApTag())
        || asList(CHP_502, MTS_BPI, MTS_OPI).contains(getApTag()) && formatFString;
  }

  private static boolean extractRegexFormatF(String field) {
    String pattern = "^\\s*(\\d/[\\S\\s]+?(\n\\d)[\\S\\s]+?)";
    return field != null && field.matches(pattern);
  }

  @Override
  public boolean checkMessageFormatUnstructured() {
    return MTS_APTAG_SCOPE.contains(getApTag()) && !checkMessageFormatF();
  }

  @Override
  public Optional<String> getAccountNumber(GetAccountNumberRequest request) {
    var trimmedTag = request.getTag().trim();
    var matchingField = request.getMatchingFields().stream()
        .findFirst()
        .orElse("");
    var message = getMessageData();
    String accountNumTag = null;
    if (List
        .of("SWF_4_50F", "SWF_4_59F", "SWF_4_59", "SWF_4_50K", "CHP_502")
        .contains(trimmedTag)) {
      return Optional.of(matchingField.split("\n")[0]);
    } else if ("MTS_OPI".equals(trimmedTag)) {
      accountNumTag = "MTS_OPD";
    } else if ("MTS_BPI".equals(trimmedTag)) {
      accountNumTag = "MTS_BPD";
    } else if ("MTS_BBI".equals(trimmedTag)) {
      accountNumTag = "MTS_BBD";
    } else if ("MTS_OBI".equals(trimmedTag)) {
      accountNumTag = "MTS_BBD";
    }
    return accountNumTag == null ? Optional.empty() : Optional.of(
        extractAccountNumberOrFirstLine(createOneElementList(accountNumTag),
            message, matchingField));
  }
}
