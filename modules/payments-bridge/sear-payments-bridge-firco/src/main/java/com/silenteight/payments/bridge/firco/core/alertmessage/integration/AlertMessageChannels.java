package com.silenteight.payments.bridge.firco.core.alertmessage.integration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertMessageChannels {

  public static final String ALERT_MESSAGE_STORED_REQUEST_CHANNEL =
      "alertMessageStoredRequestChannel";

  public static final String ALERT_MESSAGE_STORED_REQUEST_OUTBOUND_CHANNEL =
      "alertMessageStoredRequestOutboundChannel";

  public static final String ALERT_MESSAGE_RESPONSE_CHANNEL =
      "alertMessageResponseChannel";

  public static final String ALERT_MESSAGE_RESPONSE_OUTBOUND_CHANNEL =
      "alertMessageResponseOutboundChannel";

}
