package com.silenteight.scb.ingest.adapter.incomming.common.domain;

import lombok.Value;

@Value
public class GnsSyncDeltaProjection {

  String alertExternalId;
  String watchlistId;
  Integer decisionsCount;
}
