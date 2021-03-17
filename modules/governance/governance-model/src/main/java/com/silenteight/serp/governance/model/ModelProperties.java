package com.silenteight.serp.governance.model;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;

@Value
@ConfigurationProperties("serp.governance.model")
@ConstructorBinding
public class ModelProperties {

  @NotNull
  String agentConfigurationSource;
  @NotNull
  String featureSetSource;
  @NotNull
  String agentDetailsSource;
  @NotNull
  String categorySource;
}
