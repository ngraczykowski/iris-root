package com.silenteight.serp.governance.qa.sampling.generator;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import javax.validation.constraints.NotNull;

@Value
@ConfigurationProperties("serp.governance.qa.sampling.generator")
@ConstructorBinding
class AlertsGeneratorProperties {

  @NotNull
  Long sampleCount;

  @NotNull
  List<String> groupingFields;
}
