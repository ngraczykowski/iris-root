package com.silenteight.serp.governance.qa.sampling.job;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;

@Value
@ConfigurationProperties("serp.governance.qa.sampling.schedule")
@ConstructorBinding
class QaScheduledSamplingProperties {

  @NotNull
  Boolean enabled;
  @NotNull
  String auditCron;
  @NotNull
  String alertGeneratorCron;
}
