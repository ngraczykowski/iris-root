package com.silenteight.serp.governance.qa.manage.check;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;

@Value
@ConfigurationProperties("serp.governance.qa.viewing")
@ConstructorBinding
public class QaScheduledViewingDecisionProperties {

  @NotNull
  Integer maxStateResetDelayMs;
}
