package com.silenteight.hsbc.datasource.extractors.historical;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

abstract class HistoricalDecisionsRequestCreator {

  abstract GetHistoricalDecisionsRequestDto createRequest();

  protected static WatchlistPartyDto getWatchlist(MatchData matchData) {
    var build = WatchlistPartyDto.builder();
    var watchlistId = matchData.getWatchlistId().orElse("");
    var watchlistType = matchData.getWatchlistType();

    watchlistType.ifPresent(e -> build.type(e.getLabel()));
    return build
        .id(watchlistId)
        .build();
  }
}
