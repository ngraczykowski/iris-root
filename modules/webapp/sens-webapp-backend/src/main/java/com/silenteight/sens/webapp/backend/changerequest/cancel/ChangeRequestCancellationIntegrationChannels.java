package com.silenteight.sens.webapp.backend.changerequest.cancel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChangeRequestCancellationIntegrationChannels {

  public static final String CANCEL_CHANGE_REQUEST_OUTBOUND_CHANNEL =
      "cancelChangeRequestOutboundChannel";
  public static final String CANCEL_CHANGE_REQUEST_INBOUND_CHANNEL =
      "cancelChangeRequestInboundChannel";
}
