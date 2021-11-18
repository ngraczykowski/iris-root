package com.silenteight.payments.bridge.app.integration.firco;

import com.silenteight.payments.bridge.event.AlertStoredEvent;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertStoredPublisherPort;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = AlertStoredEvent.CHANNEL)
@SuppressWarnings("unused")
interface AlertStoredGateway extends AlertStoredPublisherPort {}
