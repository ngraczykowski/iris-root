/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class SourceApplicationValues {

  @NonNull
  private String alertLevel;
  @NonNull
  private String watchlistLevel;

  public String getSourceApplicationValue(boolean watchlistLevel) {
    return watchlistLevel ? this.watchlistLevel : alertLevel;
  }
}
