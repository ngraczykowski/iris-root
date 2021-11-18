package com.silenteight.payments.bridge.app.integration.firco;

import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.port.RecommendationCompletedPublisherPort;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = RecommendationCompletedEvent.CHANNEL)
@SuppressWarnings("unused")
interface RecommendationCompletedGateway extends RecommendationCompletedPublisherPort {}
