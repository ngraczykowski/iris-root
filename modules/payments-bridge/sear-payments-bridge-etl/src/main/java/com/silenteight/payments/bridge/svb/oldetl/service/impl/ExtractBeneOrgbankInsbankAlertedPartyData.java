package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import lombok.RequiredArgsConstructor;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;
import com.silenteight.payments.bridge.svb.oldetl.service.AlertedPartyDataFactory;
import com.silenteight.payments.bridge.svb.oldetl.service.ExtractDisposition;
import com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils;


import java.util.List;

import static com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure.UNSTRUCTURED;
import static com.silenteight.payments.bridge.svb.oldetl.util.CommonTerms.*;

@RequiredArgsConstructor
public class ExtractBeneOrgbankInsbankAlertedPartyData implements AlertedPartyDataFactory {

  private static final List<String> MESSAGE_FORMATS_FED_IAT_O_INT = List.of("FED", "IAT-O", "INT");
  private static final List<String> MESSAGE_FORMATS_IAT_I = List.of("IAT-I");


  @Override
  public AlertedPartyData extract(final ExtractDisposition extractDisposition) {
    final MessageFieldStructure messageFieldStructure = UNSTRUCTURED;
    var messageData = extractDisposition.getMessageData();
    var hitTag = extractDisposition.getHitTag();
    var applicationCode = extractDisposition.getApplicationCode();
    var messageFormat = extractDisposition.getMessageFormat();
    List<String> tagValueLines = messageData.getLines(hitTag);
    var lastLine = CommonUtils.getLastLineNotUsIndex(tagValueLines, applicationCode);

    if (tagValueLines.get(0).length() == 2) {

      String accountNumber = tagValueLines.get(LINE_2);
      String name = tagValueLines.get(LINE_3);
      String address = tagValueLines.size() > 3 ?
                       String.join(" ", tagValueLines.subList(LINE_4, lastLine)) :
                       "";
      String ctryTown = tagValueLines.size() > 3 ?
                        tagValueLines.get(lastLine).trim() :
                        "";
      String nameAddress = String.join(" ", tagValueLines.subList(LINE_3, lastLine + 1));

      return AlertedPartyData.builder()
          .accountNumber(accountNumber.trim().toUpperCase())
          .name(name.trim())
          .address(address.trim())
          .ctryTown(ctryTown.trim())
          .nameAddress(nameAddress.trim())
          .messageFieldStructure(messageFieldStructure)
          .build();
    } else if (tagValueLines.get(0).length() > 2) {
      if (MESSAGE_FORMATS_FED_IAT_O_INT.contains(messageFormat)) {
        String accountNumber = "";
        String name = tagValueLines.get(LINE_1);
        String address = tagValueLines.size() > 1 ?
                         String.join(" ", tagValueLines.subList(LINE_2, lastLine)) :
                         "";
        String ctryTown = tagValueLines.size() > 3 ?
                          tagValueLines.get(lastLine) :
                          "";
        String nameAddress = String.join(" ", tagValueLines.subList(LINE_1, lastLine + 1));

        return AlertedPartyData.builder()
            .accountNumber(accountNumber.trim().toUpperCase())
            .name(name.trim())
            .address(address.trim())
            .ctryTown(ctryTown.trim())
            .nameAddress(nameAddress.trim())
            .messageFieldStructure(messageFieldStructure)
            .build();
      } else if (MESSAGE_FORMATS_IAT_I.contains(messageFormat)) {
        if (tagValueLines.size() == 4) {
          return AlertedPartyData.builder()
              .accountNumber("")
              .name(tagValueLines.get(LINE_1).trim())
              .address(String.join(" ", tagValueLines.subList(LINE_2, lastLine)).trim())
              .ctryTown(tagValueLines.get(lastLine).trim())
              .nameAddress(String.join(" ", tagValueLines.subList(LINE_1, lastLine + 1)).trim())
              .messageFieldStructure(messageFieldStructure)
              .build();
        } else if (tagValueLines.size() > 4) {
          return AlertedPartyData.builder()
              .accountNumber(tagValueLines.get(LINE_1).trim().toUpperCase())
              .name(tagValueLines.get(LINE_2).trim())
              .address(String.join(" ", tagValueLines.subList(LINE_3, lastLine)).trim())
              .ctryTown(tagValueLines.get(lastLine).trim())
              .nameAddress(
                  String.join(" ", tagValueLines.subList(LINE_2, lastLine + 1)).trim())
              .messageFieldStructure(messageFieldStructure)
              .build();
        }
      }
    }
    return AlertedPartyData.builder().messageFieldStructure(messageFieldStructure).build();
  }
}
