package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;
import com.silenteight.payments.bridge.svb.oldetl.service.AlertedPartyDataFactory;
import com.silenteight.payments.bridge.svb.oldetl.service.ExtractDisposition;
import com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils;

import static com.silenteight.payments.bridge.common.dto.common.CommonTerms.*;
import static com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure.UNSTRUCTURED;

@RequiredArgsConstructor
public class ExtractOriginatorAlertedPartyData implements AlertedPartyDataFactory {

  @Override
  public AlertedPartyData extract(final ExtractDisposition extractDisposition) {
    var hitTag = extractDisposition.getHitTag();
    var applicationCode = extractDisposition.getApplicationCode();
    var messageFormat = extractDisposition.getMessageFormat();
    var messageData = extractDisposition.getMessageData();

    final MessageFieldStructure messageFieldStructure = UNSTRUCTURED;

    var lines = messageData.getLines(TAG_ORIGINATOR);
    var lastLine = CommonUtils.getLastLineNotUsIndex(lines, applicationCode);
    var firstLineLength = lines.get(0).length();

    var alertedPartyDataBuilder = AlertedPartyData.builder()
        .messageFieldStructure(messageFieldStructure);

    if (lines.size() == 2)
      return alertedPartyDataBuilder
          .name(lines.get(LINE_1).trim())
          .nameAddress(lines.get(LINE_1).trim())
          .build();

    if (firstLineLength == 2) {
      if (lines.size() == 7 && !lines.get(LINE_5).contains(",")) {
        return alertedPartyDataBuilder
            .accountNumber(lines.get(LINE_2).trim().toUpperCase())
            .name(String.join(" ", lines.subList(LINE_3, LINE_4 + 1)).trim())
            .address(String.join(" ", lines.subList(LINE_5, lastLine)).trim())
            .nameAddress(String.join(" ", lines.subList(LINE_3, lastLine + 1)).trim())
            .ctryTown(lines.get(lastLine).trim())
            .build();
      } else {
        return alertedPartyDataBuilder
            .accountNumber(lines.get(LINE_2).trim().toUpperCase())
            .name(lines.get(LINE_3).trim())
            .address(String.join(" ", lines.subList(LINE_4, lastLine)).trim())
            .nameAddress(String.join(" ", lines.subList(LINE_3, lastLine + 1)).trim())
            .ctryTown(lines.get(lastLine).trim())
            .build();
      }
    }
    if (FIRCO_FORMAT_FED.equals(messageFormat))
      return alertedPartyDataBuilder
          .name(lines.get(LINE_1).trim())
          .address(String.join(" ", lines.subList(LINE_2, lastLine)).trim())
          .nameAddress(String.join(" ", lines.subList(LINE_1, lastLine + 1)).trim())
          .ctryTown(lines.get(lastLine).trim())
          .build();

    return alertedPartyDataBuilder
        .accountNumber(lines.get(LINE_1).trim().toUpperCase())
        .name(lines.get(LINE_2).trim())
        .address(String.join(" ", lines.subList(LINE_3, lastLine)).trim())
        .nameAddress(String.join(" ", lines.subList(LINE_2, lastLine + 1)).trim())
        .ctryTown(lines.get(lastLine).trim())
        .build();
  }
}
