package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyType;

import java.util.List;

@RequiredArgsConstructor
class WatchlistPartyRequestCreator extends HistoricalDecisionsRequestCreator {

  private final MatchData matchData;

  @Override
  GetHistoricalDecisionsRequestDto createRequest() {
    return GetHistoricalDecisionsRequestDto.builder()
        .modelKeys(getModelKeys())
        .build();
  }

  private List<ModelKeyDto> getModelKeys() {
    return List.of(
        ModelKeyDto.builder()
            .modelKeyType(ModelKeyType.WATCHLIST_PARTY)
            .modelKeyValue(getWatchlist(matchData))
            .build()
    );
  }
}
