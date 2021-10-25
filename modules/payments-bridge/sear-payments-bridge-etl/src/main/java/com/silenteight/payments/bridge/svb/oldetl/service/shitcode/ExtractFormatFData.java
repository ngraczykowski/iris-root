package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
class ExtractFormatFData {

  private static final String NAME_ROW_PREFIX = "1/";
  private static final String ADDRESS_ROW_PREFIX = "2/";
  private static final String COUNTRY_ROW_PREFIX = "3/";

  private static final String COUNTRY_TOWN = "countryTwn";
  private static final String ADDRESS = "address";
  private static final String NAME = "name";

  private static final Map<String, String> PREFIX_NAME_MAPPING = new HashMap<>() {{
      put(NAME_ROW_PREFIX, NAME);
      put(ADDRESS_ROW_PREFIX, ADDRESS);
      put(COUNTRY_ROW_PREFIX, COUNTRY_TOWN);
    }};

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

    return AlertedPartyData.builder()
        .accountNumber(tagValueLines.get(accountLine).trim())
        .name(name)
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
