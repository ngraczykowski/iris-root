package com.silenteight.sens.webapp.backend.changerequest.create;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChangeRequestCreationIntegrationChannels {

  public static final String CREATE_CHANGE_REQUEST_OUTBOUND_CHANNEL =
      "createChangeRequestOutboundChannel";
  public static final String CREATE_CHANGE_REQUEST_INBOUND_CHANNEL =
      "createChangeRequestInboundChannel";
  public static final String CREATED_CHANGE_REQUEST_OUTBOUND_CHANNEL =
      "createdChangeRequestOutboundChannel";
}
