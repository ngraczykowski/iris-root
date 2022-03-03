package com.silenteight.customerbridge.common.domain;

import lombok.Value;

@Value
public class GnsSyncDeltaProjection {

  String alertExternalId;
  String watchlistId;
  Integer decisionsCount;
}
