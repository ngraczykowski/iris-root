package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ConfigurationProperties(prefix = "ae.analysis.agent-exchange.agent-request-handler")
@Data
@Validated
class AgentRequestHandlerProperties {

  private static final int DEFAULT_MAX_MESSAGE_SIZE = 1_024;

  /**
   * The maximum number of feature values requested in a single AgentExchangeRequest message.
   * <p/>
   * Each message has the list of features and the list of matches. The total number of requested
   * feature values is therefore a product of the number of features times the number of matches.
   * <p/>
   * This property sets the maximum value of that product (and defaults to 1024), i.e., Adjudication
   * Engine will never request more than 1024 feature values with a single request.
   */
  @Min(1)
  @Max(131_072)
  private int maxMessageSize = DEFAULT_MAX_MESSAGE_SIZE;
}
