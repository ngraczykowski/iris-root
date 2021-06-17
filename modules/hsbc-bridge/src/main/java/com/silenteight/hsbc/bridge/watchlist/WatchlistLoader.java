package com.silenteight.hsbc.bridge.watchlist;

import java.io.InputStream;
import java.net.URI;

public interface WatchlistLoader {

  InputStream load(URI uri) throws WatchlistLoadingException;

  class WatchlistLoadingException extends RuntimeException {

    private static final long serialVersionUID = -7946374920659451308L;

    public WatchlistLoadingException(Throwable cause) {
      super(cause);
    }
  }
}
