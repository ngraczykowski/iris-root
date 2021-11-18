package com.silenteight.payments.bridge.app.integration.firco;

import com.silenteight.payments.bridge.event.AlertRejectedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertRejectedPublisherPort;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = AlertRejectedEvent.CHANNEL)
@SuppressWarnings("unused")
interface AlertRejectedGateway extends AlertRejectedPublisherPort {}
