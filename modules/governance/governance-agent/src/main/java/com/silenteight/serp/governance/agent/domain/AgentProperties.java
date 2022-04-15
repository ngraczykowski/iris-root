package com.silenteight.serp.governance.agent.domain;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;

@Value
@ConfigurationProperties("serp.governance.agent")
@ConstructorBinding
public class AgentProperties {

  @NotNull
  String agentConfigurationSource;
  @NotNull
  String agentDetailsSource;
}
