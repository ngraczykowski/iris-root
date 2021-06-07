package com.silenteight.hsbc.bridge.worldcheck;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.watchlist.WatchlistIdentifier;
import com.silenteight.hsbc.bridge.watchlist.WorldCheckNotifierServiceClient;

import java.util.List;

@Slf4j
class WorldCheckServiceClientMock implements WorldCheckNotifierServiceClient {

  @Override
  public void notify(List<WatchlistIdentifier> identifiers) {
    identifiers.forEach(e -> log.info("WorldCheck received URI -> " + e.getUri()));
  }
}
