package com.silenteight.serp.governance.policy.solve.event;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotNull;

@Value
@ConfigurationProperties("serp.governance.featurevector.event")
@ConstructorBinding
public class FeatureVectorEventStrategyProperties {

  @NotNull
  FeatureVectorEventStrategy strategy;
}
