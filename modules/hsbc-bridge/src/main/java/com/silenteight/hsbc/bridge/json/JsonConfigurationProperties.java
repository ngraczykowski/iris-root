package com.silenteight.hsbc.bridge.json;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.json")
class JsonConfigurationProperties {

  int alertLimit;
}
