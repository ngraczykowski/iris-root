package com.silenteight.payments.bridge.svb.learning.reader.port;

import com.silenteight.payments.bridge.common.model.FindRegisteredAlertRequest;
import com.silenteight.payments.bridge.common.model.RegisteredAlert;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.util.List;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED_REQUEST_CHANNEL;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED_RESPONSE_CHANNEL;

@MessagingGateway
public interface CheckAlertRegisteredPort {

  @Gateway(requestChannel = ALERT_REGISTERED_REQUEST_CHANNEL,
      replyChannel = ALERT_REGISTERED_RESPONSE_CHANNEL)
  List<RegisteredAlert> findAlertRegistered(List<FindRegisteredAlertRequest> registeredAlert);

}
