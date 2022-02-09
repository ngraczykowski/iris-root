package com.silenteight.payments.bridge.common.agents;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.agents.contextual")
public class ContextualLearningProperties {

  private int numberOfTokensLeft = 1;
  private int numberOfTokensRight = 1;
  private int minTokens = 1;
  private boolean lineBreaks = false;

  public ContextualAlertedPartyIdModel.ContextualAlertedPartyIdModelBuilder getModelBuilder() {
    return ContextualAlertedPartyIdModel.builder()
        .numberOfTokensLeft(numberOfTokensLeft)
        .numberOfTokensRight(numberOfTokensRight)
        .minTokens(minTokens)
        .lineBreaks(lineBreaks);
  }
}
