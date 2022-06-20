/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain;

import lombok.Value;

@Value
public class GnsSyncDeltaProjection {

  String alertExternalId;
  String watchlistId;
  Integer decisionsCount;
}
