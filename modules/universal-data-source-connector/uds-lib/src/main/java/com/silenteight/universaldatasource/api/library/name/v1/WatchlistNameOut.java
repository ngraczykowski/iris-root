package com.silenteight.universaldatasource.api.library.name.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.name.v1.WatchlistName;

@Value
@Builder
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
