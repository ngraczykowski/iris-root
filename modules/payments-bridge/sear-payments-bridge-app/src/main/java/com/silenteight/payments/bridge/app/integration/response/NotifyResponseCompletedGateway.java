package com.silenteight.payments.bridge.app.integration.response;

import com.silenteight.payments.bridge.firco.recommendation.port.NotifyResponseCompletedPort;

import org.springframework.integration.annotation.MessagingGateway;

import static com.silenteight.payments.bridge.app.integration.response.FircoOutboundAmqpIntegrationConfiguration.RESPONSE_COMPLETED_OUTBOUND;

@MessagingGateway(defaultRequestChannel = RESPONSE_COMPLETED_OUTBOUND)
@SuppressWarnings("unused")
interface NotifyResponseCompletedGateway extends NotifyResponseCompletedPort {}
