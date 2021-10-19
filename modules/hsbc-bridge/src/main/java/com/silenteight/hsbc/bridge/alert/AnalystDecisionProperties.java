package com.silenteight.hsbc.bridge.alert;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import javax.validation.constraints.NotEmpty;

@Validated
@Value
@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.alert.decision")
class AnalystDecisionProperties {

  @NotEmpty
  Map<String, String> map;
}
