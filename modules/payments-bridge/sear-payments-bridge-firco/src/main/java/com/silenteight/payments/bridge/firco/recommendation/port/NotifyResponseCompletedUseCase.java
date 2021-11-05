package com.silenteight.payments.bridge.firco.recommendation.port;

import com.silenteight.proto.payments.bridge.internal.v1.event.ResponseCompleted;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.RESPONSE_COMPLETED_OUTBOUND;

@MessagingGateway
public interface NotifyResponseCompletedUseCase {

  @Gateway(requestChannel = RESPONSE_COMPLETED_OUTBOUND)
  void notify(ResponseCompleted responseCompleted);
}
