package com.silenteight.hsbc.datasource.extractors.historical;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto;
import com.silenteight.hsbc.datasource.dto.historical.WatchlistPartyDto;

abstract class HistoricalDecisionsRequestCreator {

  abstract ModelKeyDto create();

  protected static WatchlistPartyDto getWatchlist(MatchData matchData) {
    var build = WatchlistPartyDto.builder();
    var watchlistId = matchData.getWatchlistId().orElse("");
    var watchlistType = matchData.getWatchlistType();

    watchlistType.ifPresentOrElse(e -> build.type(e.getLabel()), () -> build.type(""));
    return build
        .id(watchlistId)
        .build();
  }
}
