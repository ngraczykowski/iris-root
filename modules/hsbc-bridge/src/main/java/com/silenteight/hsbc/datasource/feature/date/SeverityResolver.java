package com.silenteight.hsbc.datasource.feature.date;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.date.SeverityMode;

import java.util.List;
import java.util.Map;

import static com.silenteight.hsbc.datasource.dto.date.SeverityMode.NORMAL;
import static com.silenteight.hsbc.datasource.dto.date.SeverityMode.STRICT;

@RequiredArgsConstructor
class SeverityResolver {

  private final MatchData matchData;
  private final Map<String, List<String>> watchlistTypes;
  private static final String SAN = "SAN";

  SeverityMode resolve() {
    return isSanctioned(matchData.getCaseInformation().getExtendedAttribute5()) ? STRICT : NORMAL;
  }

  private boolean isSanctioned(String extendedAttribute5) {
    return watchlistTypes.entrySet().stream()
        .filter(entry -> entry.getValue().contains(extendedAttribute5))
        .map(Map.Entry::getKey)
        .anyMatch(SAN::equals);
  }
}
