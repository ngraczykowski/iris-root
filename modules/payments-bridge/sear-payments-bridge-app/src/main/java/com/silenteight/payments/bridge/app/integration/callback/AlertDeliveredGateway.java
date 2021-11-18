package com.silenteight.payments.bridge.app.integration.callback;

import com.silenteight.payments.bridge.event.AlertDeliveredEvent;
import com.silenteight.payments.bridge.firco.callback.port.AlertDeliveredPublisherPort;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = AlertDeliveredEvent.CHANNEL)
@SuppressWarnings("unused")
interface AlertDeliveredGateway extends AlertDeliveredPublisherPort {}
