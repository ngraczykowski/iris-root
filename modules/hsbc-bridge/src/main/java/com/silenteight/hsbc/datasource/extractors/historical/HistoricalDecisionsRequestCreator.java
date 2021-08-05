package com.silenteight.hsbc.datasource.extractors.historical;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

abstract class HistoricalDecisionsRequestCreator {

  private static final String INDIVIDUAL = "I";
  private static final String ENTITY = "C";

  abstract GetHistoricalDecisionsRequestDto createRequest();

  protected static WatchlistPartyDto getWatchlist(MatchData matchData) {
    var watchlistId = matchData.getWatchlistId().orElse("");
    var watchlistType = matchData.isIndividual() ? INDIVIDUAL : ENTITY;

    return WatchlistPartyDto.builder()
        .id(watchlistId)
        .type(watchlistType)
        .build();
  }
}
