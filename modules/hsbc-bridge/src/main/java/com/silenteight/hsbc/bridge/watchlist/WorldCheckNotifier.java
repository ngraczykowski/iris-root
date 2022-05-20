package com.silenteight.hsbc.bridge.watchlist;

import java.util.Set;

public interface WorldCheckNotifier {

  void notify(Set<WatchlistIdentifier> identifiers);
}
