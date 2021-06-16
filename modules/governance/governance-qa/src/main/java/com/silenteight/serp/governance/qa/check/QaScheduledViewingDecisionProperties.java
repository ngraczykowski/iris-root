package com.silenteight.serp.governance.qa.check;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;

@Value
@ConfigurationProperties("serp.governance.qa.scheduled.viewing-decision")
@ConstructorBinding
public class QaScheduledViewingDecisionProperties {

  @NotNull
  Integer maxMs;
}
