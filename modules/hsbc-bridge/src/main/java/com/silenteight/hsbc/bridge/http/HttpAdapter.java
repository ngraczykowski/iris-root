package com.silenteight.hsbc.bridge.http;

import com.silenteight.hsbc.bridge.watchlist.WatchlistLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

class HttpAdapter implements WatchlistLoader {

  @Override
  public InputStream load(URI uri) throws WatchlistLoadingException {
    try {
      return uri.toURL().openStream();
    } catch (IOException e) {
      throw new WatchlistLoadingException(e);
    }
  }
}
