package com.silenteight.hsbc.bridge.watchlist;

import java.io.InputStream;
import java.net.URI;

public interface WatchlistLoader {

  InputStream loadWatchlist(URI uri) throws LoadingException;
}
