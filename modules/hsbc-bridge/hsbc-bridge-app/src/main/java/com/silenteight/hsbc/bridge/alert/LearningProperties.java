package com.silenteight.hsbc.bridge.alert;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.learning")
class LearningProperties {

  int batchSize;
}
