package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChangeRequestApprovalIntegrationChannels {

  public static final String APPROVE_CHANGE_REQUEST_INBOUND_CHANNEL =
      "approveChangeRequestInboundChannel";
  public static final String APPROVE_CHANGE_REQUEST_OUTBOUND_CHANNEL =
      "approveChangeRequestOutboundChannel";
}
