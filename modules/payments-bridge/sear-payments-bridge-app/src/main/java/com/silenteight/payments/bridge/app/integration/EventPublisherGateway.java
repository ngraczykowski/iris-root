package com.silenteight.payments.bridge.app.integration;

import com.silenteight.payments.bridge.event.EventPublisher;

import org.springframework.integration.annotation.MessagingGateway;

import static com.silenteight.payments.bridge.app.integration.EventRouterConfiguration.EVENT_GATEWAY_CHANNEL;

@MessagingGateway(defaultRequestChannel = EVENT_GATEWAY_CHANNEL)
@SuppressWarnings("unused")
interface EventPublisherGateway extends EventPublisher {}
