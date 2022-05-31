package com.silenteight.adjudication.engine.solving.application.listener;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.process.AgentResponseProcess;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@ConditionalOnProperty(
    value = "ae.solving.enabled",
    havingValue = "true"
)
class ReceiveAgentResponseListener {

  private final AgentResponseProcess agentResponseProcess;

  ReceiveAgentResponseListener(final AgentResponseProcess agentResponseProcess) {
    this.agentResponseProcess = agentResponseProcess;
  }

  @RabbitListener(queues = "ae.agent-response", concurrency = "10-10")
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  void onReceive(AgentExchangeResponse agentResponse) {
    log.info(
        "Received {} agent exchange responses for {}", agentResponse.getAgentOutputsCount(),
        agentResponse
            .getAgentOutputsList()
            .stream()
            .flatMap(f -> f.getFeaturesList().stream())
            .collect(
                Collectors.toList()));
    agentResponseProcess.processMatchesFeatureValue(agentResponse);
  }
}
