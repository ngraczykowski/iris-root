package com.silenteight.payments.bridge.agents.service.contextual;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.agent.contextual")
class ContextualLearningProperties {

  private int numberOfTokensLeft = 1;
  private int numberOfTokensRight = 1;
  private int minTokens = 1;
  private boolean lineBreaks = false;

}
