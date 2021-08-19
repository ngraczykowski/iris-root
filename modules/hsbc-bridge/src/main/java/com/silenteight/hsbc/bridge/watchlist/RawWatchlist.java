package com.silenteight.hsbc.bridge.watchlist;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.unpacker.UnzippedObject;

@Value
@Builder
class RawWatchlist {

  UnzippedObject core;
  UnzippedObject nameAliases;
  UnzippedObject keywords;
  UnzippedObject coreChecksum;
  UnzippedObject nameAliasesChecksum;
}
