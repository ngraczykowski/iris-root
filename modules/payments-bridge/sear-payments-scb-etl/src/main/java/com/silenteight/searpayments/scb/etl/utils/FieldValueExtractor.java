package com.silenteight.searpayments.scb.etl.utils;

import com.silenteight.tsaas.bridge.etl.utils.AbstractMessageStructure.MessageStructureDtp;
import com.silenteight.tsaas.bridge.etl.utils.AbstractMessageStructure.MessageStructureScstar;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.silenteight.tsaas.bridge.etl.utils.CommonUtils.escapeRegex;
import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldValueExtractor {

  public static String extractFieldValue(String sourceSystem, String tag, String messageData) {
    if (sourceSystem.contains("STA") || sourceSystem.contains("AMH")) {
      return extractMatchfieldFromScstarMessage(tag, messageData);
    } else {
      return extractMatchfieldFromNonScstarMessage(tag, messageData);
    }
  }

  @NotNull
  public static List<List<String>> extractFieldValues(
      AbstractMessageStructure messageStructure, String messageData) {
    List<List<String>> fieldValues;
    if (messageStructure instanceof MessageStructureScstar) {
      fieldValues = extractFieldValues("STA", messageStructure, messageData);
    } else if (messageStructure instanceof MessageStructureDtp) {
      fieldValues = extractFieldValues("DTP", messageStructure, messageData);
    } else {
      fieldValues = extractFieldValues("", messageStructure, messageData);
    }
    return fieldValues;
  }

  public static List<List<String>> extractFieldValues(
      String sourceSystem, AbstractMessageStructure messageStructure, String messageData) {
    String tag = messageStructure.getApTag();
    List<List<String>> fieldValues = new ArrayList<>();
    if (sourceSystem.contains("STA") || sourceSystem.contains("AMH")) {
      String mainTagFieldValue = extractMatchfieldFromScstarMessage(tag, messageData);
      fieldValues.add(singletonList(mainTagFieldValue));
    } else if (sourceSystem.contains("DTP")) {
      if (MessageStructureDtp.DTP_PAIRS_SCOPE.contains(tag)) {
        MessageStructureDtp messageStructureDtp = (MessageStructureDtp) messageStructure;
        List<String> mainTagFieldValueList = messageStructureDtp.getMainTagFieldValues();
        List<String> nextTagFieldValueList = messageStructureDtp.getNextTagFieldValues();
        fieldValues.add(mainTagFieldValueList);
        fieldValues.add(nextTagFieldValueList);
      } else {
        String mainTagFieldValue = extractMatchfieldFromNonScstarMessage(tag, messageData);
        fieldValues.add(singletonList(mainTagFieldValue));
      }
    } else {
      String mainTagFieldValue = extractMatchfieldFromNonScstarMessage(tag, messageData);
      fieldValues.add(singletonList(mainTagFieldValue));
    }
    return fieldValues;
  }

  static String extractMatchfieldFromNonScstarMessage(
      @NotNull String tag, @NotNull String messageData) {
    tag = tag.strip();
    int spaceLength = 15 - tag.length();
    if (tag.contains("STRIP")) {
      tag = Pattern.quote(tag);
    }
    String regex = "(?s)(?:(?<=\\n\\[" + escapeRegex(tag) + "[\\s\\d]{" +
        spaceLength + "}\\]))(.*?\\n)(?=\\[[^]]+\\]|$)";
    return extractFieldOnRegex(messageData, regex);
  }

  static String extractMatchfieldFromScstarMessage(
      @NotNull String tag, @NotNull String messageData) {
    tag = tag.strip();
    if (tag.contains("STRIP")) {
      tag = Pattern.quote(tag);
    }
    String regex = "(?s)(?:(?<=\\n:" + escapeRegex(tag) + ":))(.*?\\n)(?=:[0-9]{2}\\w?:|-})";
    return extractFieldOnRegex(messageData, regex);
  }

  @NotNull
  private static String extractFieldOnRegex(String messageData, String regex) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(messageData);
    StringBuilder matches = new StringBuilder();
    boolean found = false;
    while (matcher.find()) {
      matches.append(matcher.group(1));
      found = true;
    }
    if (found)
      return matches.toString();
    else
      return "Matching field not extracted";
  }

}
