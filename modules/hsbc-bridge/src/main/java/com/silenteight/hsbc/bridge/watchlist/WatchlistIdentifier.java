package com.silenteight.hsbc.bridge.watchlist;

import lombok.Value;

import com.silenteight.proto.worldcheck.api.v1.WatchlistType;


@Value
public class WatchlistIdentifier {

  String uri;
  WatchlistType type;
}
