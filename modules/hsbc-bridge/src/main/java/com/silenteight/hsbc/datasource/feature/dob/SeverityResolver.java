package com.silenteight.hsbc.datasource.feature.dob;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.date.SeverityMode;

import java.util.Map;

import static com.silenteight.hsbc.datasource.dto.date.SeverityMode.NORMAL;
import static com.silenteight.hsbc.datasource.dto.date.SeverityMode.STRICT;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

@RequiredArgsConstructor
class SeverityResolver {

  private final MatchData matchData;

  private static final Map<String, String> WL_TYPES = ofEntries(
      entry("AE-MEWOLF", "SAN"),
      entry("MENA-GREY", "SAN"),
      entry("MEWOLF", "SAN"),
      entry("SAN", "SAN"),
      entry("US-HBUS", "SAN"),
      entry("AML", "AML"),
      entry("CTF-P2", "AML"),
      entry("INNIA", "AML"),
      entry("MX-AML", "AML"),
      entry("MX-SHCP", "AML"),
      entry("PEP", "PEP"),
      entry("SCION", "EXITS"),
      entry("SSC", "SSC")
  );

  SeverityMode resolve() {
    return isSanctioned(matchData.getCaseInformation().getExtendedAttribute5()) ? STRICT : NORMAL;
  }

  private static boolean isSanctioned(String extendedAttribute5) {
    return WL_TYPES.entrySet().stream()
        .filter(entry -> entry.getKey().equals(extendedAttribute5))
        .anyMatch(entry -> entry.getValue().equals("SAN"));
  }
}
