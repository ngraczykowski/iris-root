package com.silenteight.adjudication.engine.solve.agentexchange;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "ae.solve")
class SolveProperties {

  private Duration agentTimeout = Duration.ofMinutes(1);
  private Duration cleanTimedOutAgentExchangesInterval = Duration.ofMinutes(1);
  private int agentRequestChunkSize = 100;
  private String agentExchange = "agent.request";
}
