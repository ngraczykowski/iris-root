package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import java.util.List;

@RequiredArgsConstructor
public class ExtractBeneOrgbankInsbankAlertedPartyData {

  private static final List<String> MESSAGE_FORMATS_FED_IAT_O_INT = List.of("FED", "IAT-O", "INT");
  private static final List<String> MESSAGE_FORMATS_IAT_I = List.of("IAT-I");

  private static final int FIRST_LINE = 0;
  private static final int SECOND_LINE = 1;
  private static final int THIRD_LINE = 2;
  private static final int FOURTH_LINE = 3;

  private final MessageData messageData;
  private final String hitTag;
  private final String messageFormat;

  public AlertedPartyData extract(MessageFieldStructure messageFieldStructure) {
    List<String> tagValueLines = messageData.getLines(hitTag);
    var lastLine = tagValueLines.size() - 1;

    if (tagValueLines.get(0).length() == 2) {

      String accountNumber = tagValueLines.get(SECOND_LINE).trim();
      String name = tagValueLines.get(THIRD_LINE).trim();
      String address = tagValueLines.size() > 3 ?
                       String.join(" ", tagValueLines.subList(FOURTH_LINE, lastLine)) :
                       "";
      String ctryTown = tagValueLines.size() > 3 ?
                        tagValueLines.get(lastLine).trim() :
                        "";
      String nameAddresses = String.join(" ", tagValueLines.subList(THIRD_LINE, lastLine + 1));

      return AlertedPartyData.builder()
          .accountNumber(accountNumber)
          .name(name)
          .address(address)
          .ctryTown(ctryTown)
          .nameAddress(nameAddresses)
          .messageFieldStructure(messageFieldStructure)
          .build();
    } else if (tagValueLines.get(0).length() > 2) {
      if (MESSAGE_FORMATS_FED_IAT_O_INT.contains(messageFormat)) {
        String accountNumber = null;
        String name = tagValueLines.get(FIRST_LINE).trim();
        String address = tagValueLines.size() > 1 ?
                         String.join(" ", tagValueLines.subList(SECOND_LINE, lastLine)) :
                         "";
        String ctryTown = tagValueLines.size() > 3 ?
                          tagValueLines.get(lastLine).trim() :
                          "";
        String nameAddress = String.join(" ", tagValueLines.subList(FIRST_LINE, lastLine + 1));

        return AlertedPartyData.builder()
            .accountNumber(accountNumber)
            .name(name)
            .address(address)
            .ctryTown(ctryTown)
            .nameAddress(nameAddress)
            .messageFieldStructure(messageFieldStructure)
            .build();
      } else if (MESSAGE_FORMATS_IAT_I.contains(messageFormat)) {
        if (tagValueLines.size() == 4) {
          return AlertedPartyData.builder()
              .accountNumber("")
              .name(tagValueLines.get(FIRST_LINE).trim())
              .address(String.join(" ", tagValueLines.subList(SECOND_LINE, lastLine)))
              .ctryTown(tagValueLines.get(lastLine).trim())
              .nameAddress(String.join(" ", tagValueLines.subList(FIRST_LINE, lastLine + 1)))
              .messageFieldStructure(messageFieldStructure)
              .build();
        } else if (tagValueLines.size() > 4) {
          return AlertedPartyData.builder()
              .accountNumber(tagValueLines.get(FIRST_LINE).trim())
              .name(tagValueLines.get(SECOND_LINE).trim())
              .address(String.join(" ", tagValueLines.subList(THIRD_LINE, lastLine)))
              .ctryTown(tagValueLines.get(lastLine).trim())
              .nameAddress(String.join(" ", tagValueLines.subList(SECOND_LINE, lastLine + 1)))
              .messageFieldStructure(messageFieldStructure)
              .build();
        }
      }
    }
    return AlertedPartyData.builder().messageFieldStructure(messageFieldStructure).build();
  }
}
