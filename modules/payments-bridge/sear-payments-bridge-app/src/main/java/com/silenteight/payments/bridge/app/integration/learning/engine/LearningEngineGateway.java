package com.silenteight.payments.bridge.app.integration.learning.engine;


import com.silenteight.payments.bridge.svb.newlearning.port.HistoricalDecisionLearningEnginePort;

import org.springframework.integration.annotation.MessagingGateway;

import static com.silenteight.payments.bridge.app.integration.learning.engine.LearningEngineOutboundAmqpIntegrationConfiguration.LEARNING_ENGINE_OUTBOUND;

@MessagingGateway(defaultRequestChannel = LEARNING_ENGINE_OUTBOUND)
@SuppressWarnings("unused")
interface LearningEngineGateway extends HistoricalDecisionLearningEnginePort {

}
