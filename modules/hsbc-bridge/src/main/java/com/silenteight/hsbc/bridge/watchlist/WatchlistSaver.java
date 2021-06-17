package com.silenteight.hsbc.bridge.watchlist;

import java.io.InputStream;
import java.net.URI;

public interface WatchlistSaver {

  URI save(InputStream inputStream, String name) throws WatchlistSavingException;

  class WatchlistSavingException extends RuntimeException {

    private static final long serialVersionUID = -3180672309079128603L;

    public WatchlistSavingException(Throwable cause) {
      super(cause);
    }
  }
}
