package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.silenteight.payments.bridge.svb.oldetl.util.CommonTerms.*;

@RequiredArgsConstructor
class ExtractFormatFData {

  private static final Map<String, String> PREFIX_NAME_MAPPING =
      Map.of(NAME_ROW_PREFIX, NAME, ADDRESS_ROW_PREFIX, ADDRESS, COUNTRY_ROW_PREFIX, COUNTRY_TOWN);

  private final MessageFieldStructure messageFieldStructure;

  public AlertedPartyData extract(List<String> tagValueLines, int accountLine, int nameLine) {

    var valueMap = new HashMap<String, String>();

    for (var value : tagValueLines) {
      var prefix = value.substring(0, 2);
      if (PREFIX_NAME_MAPPING.containsKey(prefix)) {
        var nameMapping = PREFIX_NAME_MAPPING.get(prefix);
        if (valueMap.containsKey(nameMapping)) {
          valueMap.put(
              nameMapping, String.join(" ", valueMap.get(nameMapping), value.substring(2)));
          continue;
        }
        valueMap.put(nameMapping, value.substring(2));
      }
    }

    var name = valueMap.containsKey(NAME) ? valueMap.get(NAME) : tagValueLines.get(nameLine);

    var unprocessedAccountNumberLine = tagValueLines.get(accountLine);
    String accountNumber = unprocessedAccountNumberLine.charAt(0) == '/' ?
                           unprocessedAccountNumberLine.substring(1) :
                           unprocessedAccountNumberLine;

    return AlertedPartyData.builder()
        .accountNumber(accountNumber.trim().toUpperCase())
        .name(name.trim())
        .address(valueMap.get(ADDRESS).trim())
        .ctryTown(valueMap.get(COUNTRY_TOWN).trim())
        .nameAddress(String.join(
            " ",
            name,
            valueMap.get(ADDRESS).trim(),
            valueMap.get(COUNTRY_TOWN).trim()))
        .messageFieldStructure(messageFieldStructure)
        .build();
  }
}
