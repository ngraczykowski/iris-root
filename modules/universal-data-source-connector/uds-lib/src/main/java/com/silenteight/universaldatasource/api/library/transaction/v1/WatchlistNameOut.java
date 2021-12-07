package com.silenteight.universaldatasource.api.library.transaction.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.transaction.v1.WatchlistName;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class WatchlistNameOut {

  String name;
  NameTypeOut type;

  static WatchlistNameOut createFrom(WatchlistName input) {
    return WatchlistNameOut.builder()
        .name(input.getName())
        .type(NameTypeOut.valueOf(input.getType().name()))
        .build();
  }
}
