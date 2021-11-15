package com.silenteight.payments.bridge.app.integration.store;

import com.silenteight.payments.bridge.firco.alertmessage.port.MessageStoredPublisherPort;

import org.springframework.integration.annotation.MessagingGateway;

import static com.silenteight.payments.bridge.app.integration.store.MessageStoredOutboundAmqpIntegrationConfiguration.MESSAGE_STORED_OUTBOUND;

@MessagingGateway(defaultRequestChannel = MESSAGE_STORED_OUTBOUND)
@SuppressWarnings("unused")
interface MessageStoredGateway extends MessageStoredPublisherPort {}
