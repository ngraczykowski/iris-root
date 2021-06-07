package com.silenteight.hsbc.bridge.watchlist;

import lombok.Value;

import java.io.InputStream;

@Value(staticConstructor = "of")
class RawWatchlistData {

  InputStream inputStream;
  String name;
}
