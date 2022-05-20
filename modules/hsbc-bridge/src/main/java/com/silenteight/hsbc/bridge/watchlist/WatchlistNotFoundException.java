package com.silenteight.hsbc.bridge.watchlist;

class WatchlistNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -1888313097292422184L;

  WatchlistNotFoundException(String message) {
    super(message);
  }

  WatchlistNotFoundException(Throwable cause) {
    super(cause);
  }
}
