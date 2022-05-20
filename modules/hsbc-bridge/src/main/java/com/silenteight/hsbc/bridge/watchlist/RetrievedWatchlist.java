package com.silenteight.hsbc.bridge.watchlist;

import lombok.Builder;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
class RetrievedWatchlist {

  InputStream core;
  InputStream aliases;
  String keywordsUri;
}
