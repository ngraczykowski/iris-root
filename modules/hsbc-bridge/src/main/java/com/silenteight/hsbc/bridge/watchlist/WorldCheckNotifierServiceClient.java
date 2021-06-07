package com.silenteight.hsbc.bridge.watchlist;

import java.util.List;

public interface WorldCheckNotifierServiceClient {

  void notify(List<WatchlistIdentifier> identifiers);
}
