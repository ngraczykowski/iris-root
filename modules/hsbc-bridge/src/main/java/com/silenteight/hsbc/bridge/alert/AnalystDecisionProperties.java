package com.silenteight.hsbc.bridge.alert;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;

@Value
@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.alert.decision")
class AnalystDecisionProperties {

  Map<String, String> map;
}
