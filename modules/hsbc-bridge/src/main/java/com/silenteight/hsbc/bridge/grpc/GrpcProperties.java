package com.silenteight.hsbc.bridge.grpc;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@Value
@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.grpc")
public class GrpcProperties {

  Duration defaultDeadline;
  Duration recommendationDeadline;

  public long deadlineInSeconds() {
    return defaultDeadline.toSecondsPart();
  }

  public long recommendationDeadlineInSeconds() {
    return recommendationDeadline.toSeconds();
  }
}
