package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.historical.AlertedPartyDto;
import com.silenteight.hsbc.datasource.dto.historical.MatchDto;
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto;
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto.ModelKeyType;

@RequiredArgsConstructor
class MatchRequestCreator extends HistoricalDecisionsRequestCreator {

  private final MatchData matchData;
  private final String alertedPartyId;

  @Override
  ModelKeyDto create() {
    return ModelKeyDto.builder()
        .modelKeyType(ModelKeyType.MATCH)
        .modelKeyValue(getMatch())
        .build();
  }

  private MatchDto getMatch() {
    return MatchDto.builder()
        .alertedParty(AlertedPartyDto.builder().id(alertedPartyId).build())
        .watchlistParty(getWatchlist(matchData))
        .build();
  }
}
