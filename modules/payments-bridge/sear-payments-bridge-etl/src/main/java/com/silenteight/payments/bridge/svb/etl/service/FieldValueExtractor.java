package com.silenteight.payments.bridge.svb.etl.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.svb.etl.model.AbstractMessageStructure;
import com.silenteight.payments.bridge.svb.etl.model.AbstractMessageStructure.MessageStructureDtp;
import com.silenteight.payments.bridge.svb.etl.model.AbstractMessageStructure.MessageStructureScstar;
import com.silenteight.payments.bridge.svb.etl.model.ExtractFieldStructureValue;
import com.silenteight.payments.bridge.svb.etl.port.ExtractFieldValueUseCase;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.silenteight.payments.bridge.svb.etl.util.CommonUtils.escapeRegex;
import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Service
class FieldValueExtractor implements ExtractFieldValueUseCase {

  @Override
  public String extractFieldValue(ExtractFieldStructureValue request) {
    if (request.getSourceSystem().contains("STA") || request.getSourceSystem().contains("AMH")) {
      return extractMatchfieldFromScstarMessage(request.getTag(), request.getMessageData());
    } else {
      return extractMatchfieldFromNonScstarMessage(request.getTag(), request.getMessageData());
    }
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

  @Override
  public List<List<String>> extractFieldValues(AbstractMessageStructure messageStructure) {
    List<List<String>> fieldValues;
    if (messageStructure instanceof MessageStructureScstar) {
      fieldValues =
          extractFieldValues("STA", messageStructure);
    } else if (messageStructure instanceof MessageStructureDtp) {
      fieldValues =
          extractFieldValues("DTP", messageStructure);
    } else {
      fieldValues = extractFieldValues("", messageStructure);
    }
    return fieldValues;
  }

  @Override
  public List<List<String>> extractFieldValues(
      String sourceSystem, AbstractMessageStructure messageStructure) {
    String tag = messageStructure.getApTag();
    List<List<String>> fieldValues = new ArrayList<>();
    if (sourceSystem.contains("STA") || sourceSystem.contains("AMH")) {
      String mainTagFieldValue =
          extractMatchfieldFromScstarMessage(tag, messageStructure.getMessageData());
      fieldValues.add(singletonList(mainTagFieldValue));
    } else if (sourceSystem.contains("DTP")) {
      if (MessageStructureDtp.DTP_PAIRS_SCOPE.contains(tag)) {
        MessageStructureDtp messageStructureDtp = (MessageStructureDtp) messageStructure;
        List<String> mainTagFieldValueList = messageStructureDtp.getMainTagFieldValues();
        List<String> nextTagFieldValueList = messageStructureDtp.getNextTagFieldValues();
        fieldValues.add(mainTagFieldValueList);
        fieldValues.add(nextTagFieldValueList);
      } else {
        String mainTagFieldValue =
            extractMatchfieldFromNonScstarMessage(tag, messageStructure.getMessageData());
        fieldValues.add(singletonList(mainTagFieldValue));
      }
    } else {
      String mainTagFieldValue =
          extractMatchfieldFromNonScstarMessage(tag, messageStructure.getMessageData());
      fieldValues.add(singletonList(mainTagFieldValue));
    }
    return fieldValues;
  }
}
