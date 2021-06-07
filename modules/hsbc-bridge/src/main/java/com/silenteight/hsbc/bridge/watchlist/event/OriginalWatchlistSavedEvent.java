package com.silenteight.hsbc.bridge.watchlist.event;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OriginalWatchlistSavedEvent {

  String coreWatchlistUri;
  String aliasesWatchlistUri;
  String keywordsWatchlistUri;
}
