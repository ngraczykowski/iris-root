package com.silenteight.hsbc.bridge.worldcheck;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.watchlist.WatchlistIdentifier;
import com.silenteight.hsbc.bridge.watchlist.WorldCheckNotifierServiceClient;

import java.util.List;

@Slf4j
class WorldCheckAdapter implements WorldCheckNotifierServiceClient {

  @Override
  public void notify(List<WatchlistIdentifier> identifiers) {
    // TODO (smrozowski) Send file URI to WorldCheck API
    log.warn("WorldCheck API was not implemented yet - use dev profile instead");
  }
}
