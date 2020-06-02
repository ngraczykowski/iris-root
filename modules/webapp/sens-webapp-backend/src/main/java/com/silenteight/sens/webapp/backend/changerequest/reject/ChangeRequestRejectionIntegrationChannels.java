package com.silenteight.sens.webapp.backend.changerequest.reject;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChangeRequestRejectionIntegrationChannels {

  public static final String REJECT_CHANGE_REQUEST_OUTBOUND_CHANNEL =
      "rejectChangeRequestOutboundChannel";
  public static final String REJECT_CHANGE_REQUEST_INBOUND_CHANNEL =
      "rejectChangeRequestInboundChannel";
}
