package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto;
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto.ModelKeyType;

@RequiredArgsConstructor
class WatchlistPartyRequestCreator extends HistoricalDecisionsRequestCreator {

  private final MatchData matchData;

  @Override
  ModelKeyDto create() {
    return ModelKeyDto.builder()
        .modelKeyType(ModelKeyType.WATCHLIST_PARTY)
        .modelKeyValue(getWatchlist(matchData))
        .build();
  }
}
