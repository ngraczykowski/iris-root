package com.silenteight.hsbc.bridge.analysis;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.analysis")
@Value
class AnalysisProperties {

  Duration alertTimeoutDuration;
}
